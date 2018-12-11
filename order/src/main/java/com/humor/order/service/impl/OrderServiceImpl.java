package com.humor.order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.humor.common.BigDecimalUtil;
import com.humor.common.DateTimeUtil;
import com.humor.common.JsonUtil;
import com.humor.common.repository.EventProviderRepository;
import com.humor.domain.common.Const;
import com.humor.domain.common.EventType;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.common.SnowFlakeIdGenerator;
import com.humor.domain.entity.*;
import com.humor.domain.vo.OrderItemVo;
import com.humor.domain.vo.OrderProductVo;
import com.humor.domain.vo.OrderVo;
import com.humor.domain.vo.ShippingVo;
import com.humor.order.repository.OrderItemRepository;
import com.humor.order.repository.OrderRepository;
import com.humor.order.service.IOrderService;
import com.humor.order.service.api.CartService;
import com.humor.order.service.api.ProductService;
import com.humor.order.service.api.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhangshaoze
 */
@Slf4j
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SnowFlakeIdGenerator snowFlakeIdGenerator;

    @Autowired
    private EventProviderRepository eventProviderRepository;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;



    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShippingService shippingService;


    @Override
    @Transactional(rollbackFor = {Exception.class},isolation = Isolation.READ_UNCOMMITTED)
    public ServerResponse createOrder(Long userId,Long shippingId) throws JsonProcessingException {

        //从购物车中获取数据
        ServerResponse<List<Cart>> cartResponse = cartService.checkedCartByUserId(userId);
        if(!cartResponse.isSuccess()){
            return cartResponse;
        }
        List<Cart> cartList = cartResponse.getData();

        //生成订单明细
        ServerResponse serverResponse = this.getCartOrderItem(userId,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList = (List<OrderItem>)serverResponse.getData();
        //订单总金额
        BigDecimal payment = orderItemList.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //生成订单
        Order order = this.assembleOrder(userId,shippingId,payment);
        if(order == null){
            return ServerResponse.createByErrorMessage("生成订单错误");
        }

        for(OrderItem orderItem : orderItemList){
            orderItem.setId(snowFlakeIdGenerator.nextId());
            orderItem.setOrderNo(order.getId());
        }
        //批量插入订单明细
        orderItemList.stream().forEach(orderItem ->
            entityManager.persist(orderItem)
        );
        entityManager.flush();
        entityManager.close();

        //生成成功,减少产品的库存
        this.reduceProductStock(orderItemList);
        //从购物车清空已选中的商品
        this.cleanCart(cartList);

        //返回给前端数据

        OrderVo orderVo = assembleOrderVo(order,orderItemList);
        log.info("生成订单--------------------end--------------------");
        return ServerResponse.createBySuccess(orderVo);
    }



    private OrderVo assembleOrderVo(Order order,List<OrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        orderVo.setId(order.getId());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        ServerResponse<Shipping> serverResponseShipping = shippingService.shippingById(Long.valueOf(1),order.getShippingId());
        Shipping shipping = serverResponseShipping.getData();
        if(shipping != null){
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(assembleShippingVo(shipping));
        }

        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));


        orderVo.setImageHost("");


        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for(OrderItem orderItem : orderItemList){
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }


    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }



    private ShippingVo assembleShippingVo(Shipping shipping){
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }

//    private void cleanCart(List<Cart> cartList) throws JsonProcessingException {
//        ArrayList<Long> list = new ArrayList<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        for(Cart cart : cartList){
//            list.add(cart.getId());
//        }
//        EventInfo eventInfo = EventInfo.builder().id(0L)
//                .content(objectMapper.writeValueAsString(list))
//                .eventType(EventType.CLEAR_CART_BY_ID)
//                .eventStatus(EventStatus.NEW)
//                .build();
//        //save根据productId清空购物车
//        eventProviderRepository.save(eventInfo);
//
//    }

    private void cleanCart(List<Cart> cartList) throws JsonProcessingException {
        ArrayList<Long> list = new ArrayList<>();
        for(Cart cart : cartList){
            list.add(cart.getId());
        }
        String event = JsonUtil.obj2String(list);
        //清空购物车事件发布到kafka
        kafkaTemplate.send(EventType.CLEAR_CART_BY_ID.name().toLowerCase(),event);

    }

//    private void reduceProductStock(List<OrderItem> orderItemList) throws JsonProcessingException {
//        Map<Long,Integer> reduceStock = new HashMap<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        for(OrderItem orderItem : orderItemList){
//            reduceStock.put(orderItem.getProductId(),orderItem.getQuantity());
//        }
//        EventInfo eventInfo = EventInfo.builder().id(0L)
//                .content(objectMapper.writeValueAsString(reduceStock))
//                .eventType(EventType.SUBTRACT_STOCK)
//                .eventStatus(EventStatus.NEW)
//                .build();
//        //save减库存事件
//        eventProviderRepository.save(eventInfo);
//    }

    /**
     * 商品减库存
     * @param orderItemList
     * @throws JsonProcessingException
     */
    private void reduceProductStock(List<OrderItem> orderItemList) throws JsonProcessingException {
        Map<Long,Integer> reduceStock = new HashMap<>(16);
        ObjectMapper objectMapper = new ObjectMapper();
        for(OrderItem orderItem : orderItemList){
            reduceStock.put(orderItem.getProductId(),orderItem.getQuantity());
        }
        String event = objectMapper.writeValueAsString(reduceStock);
        //减库存事件发布到kafka
        kafkaTemplate.send(EventType.SUBTRACT_STOCK.name().toLowerCase(),event);
    }


    private Order assembleOrder(Long userId,Long shippingId,BigDecimal payment){
        Order order = new Order();
        long orderNo = snowFlakeIdGenerator.nextId();
        order.setId(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);

        order.setUserId(userId);
        order.setShippingId(shippingId);
        //发货时间等等
        //付款时间等等
        Order save = orderRepository.save(order);
        if(save!=null){
            return order;
        }
        return null;
    }


    private long generateOrderNo(){
        long currentTime =System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }



    private ServerResponse getCartOrderItem(Long userId,List<Cart> cartList){
        List<OrderItem> orderItemList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createByErrorMessage("购物车为空");
        }

        //校验购物车的数据,包括产品的状态和数量
        for(Cart cartItem : cartList){
            OrderItem orderItem = new OrderItem();
            ServerResponse<Product> productServerResponse = productService.productInfoById(cartItem.getProductId());
            Product product = productServerResponse.getData();
            if(product!=null){
                if(Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()){
                    return ServerResponse.createByErrorMessage("产品："+product.getName()+"已下架");
                }

                //校验库存
                if(cartItem.getQuantity() > product.getStock()){
                    return ServerResponse.createByErrorMessage("产品"+product.getName()+"库存不足");
                }
                orderItem.setUserId(userId);
                orderItem.setProductId(product.getId());
                orderItem.setProductName(product.getName());
                orderItem.setProductImage(product.getMainImage());
                orderItem.setCurrentUnitPrice(product.getPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity()));
                orderItemList.add(orderItem);
            }
        }
        return ServerResponse.createBySuccess(orderItemList);
    }





    @Override
    public ServerResponse<String> cancel(Long userId, Long orderNo){

        Order order  = orderRepository.findByUserIdAndId(userId,orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("该用户此订单不存在");
        }
        if(order.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()){
            return ServerResponse.createByErrorMessage("已付款,无法取消订单");
        }
        Session session = (Session)entityManager.getDelegate();
        session.evict(order);
        order.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
        Order save = orderRepository.save(order);
        if(save !=null){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }




    @Override
    public ServerResponse getOrderCartProduct(Long userId){
        OrderProductVo orderProductVo = new OrderProductVo();
        //从购物车中获取数据

        ServerResponse<List<Cart>> serverResponseCart = cartService.checkedCartByUserId(userId);
        List<Cart> cartList = serverResponseCart.getData();
        ServerResponse serverResponse =  this.getCartOrderItem(userId,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList =( List<OrderItem> ) serverResponse.getData();

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost("");
        return ServerResponse.createBySuccess(orderProductVo);
    }


    @Override
    public ServerResponse<OrderVo> getOrderDetail(Long userId, Long orderNo){

        Order order = orderRepository.findByUserIdAndId(userId,orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemRepository.findByUserIdAndOrderNo(userId,orderNo);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return  ServerResponse.createByErrorMessage("没有找到该订单");
    }


    @Override
    public ServerResponse getOrderList(Long userId, int pageNum, int pageSize){
        List<Order> orderList = orderRepository.findByUserId(userId,PageRequest.of(pageNum,pageSize));
        List<OrderVo> orderVoList = assembleOrderVoList(orderList,userId);
        return ServerResponse.createBySuccess(orderVoList);
    }


    private List<OrderVo> assembleOrderVoList(List<Order> orderList,Long userId) {
        List<OrderVo> orderVoList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = Lists.newArrayList();
            if (userId == null) {
                //todo 管理员查询的时候 不需要传userId
                orderItemList = orderItemRepository.findByOrderNo(order.getId());
            } else {
                orderItemList = orderItemRepository.findByUserIdAndOrderNo(userId,order.getId());
            }
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    @Override
    public ServerResponse queryOrderPayStatus(Long userId, Long orderNo){
        Order order = orderRepository.findByUserIdAndId(userId,orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse<List<OrderVo>> manageList(int pageNum, int pageSize){
        Page<Order> orderPage = orderRepository.findAll(new PageRequest(pageNum,pageSize));
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderPage.getContent(),null);

        return ServerResponse.createBySuccess(orderVoList);
    }


    @Override
    public ServerResponse<OrderVo> manageDetail(Long orderNo){
        Order order = orderRepository.findById(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemRepository.findByOrderNo(orderNo);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }



    @Override
    public ServerResponse manageSearch(Long orderNo, int pageNum, int pageSize){
        Order order = orderRepository.findById(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemRepository.findByOrderNo(orderNo);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);

            return ServerResponse.createBySuccess(orderVo);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }


    @Override
    public ServerResponse<String> manageSendGoods(Long orderNo){
        Order order = orderRepository.findById(orderNo);
        if(order != null){
            if(order.getStatus() == Const.OrderStatusEnum.PAID.getCode()){
                order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                return ServerResponse.createBySuccess("发货成功");
            }
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

























}
