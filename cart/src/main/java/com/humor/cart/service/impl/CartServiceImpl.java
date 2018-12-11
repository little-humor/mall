package com.humor.cart.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.humor.cart.repository.CartRepository;
import com.humor.cart.service.ICartService;
import com.humor.cart.service.api.ProductService;
import com.humor.common.BigDecimalUtil;
import com.humor.domain.common.Const;
import com.humor.domain.common.ResponseCode;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.common.SnowFlakeIdGenerator;
import com.humor.domain.entity.Cart;
import com.humor.domain.entity.Product;
import com.humor.domain.vo.CartProductVo;
import com.humor.domain.vo.CartVo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangshaoze
 */
@Slf4j
@Service("iCartService")
@Transactional(rollbackFor = Exception.class)
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SnowFlakeIdGenerator snowFlakeIdGenerator;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ServerResponse<CartVo> add(Long userId, Long productId, Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartRepository.findByIdAndUserId(userId,productId);
        if(cart == null){
            //这个产品不在这个购物车里,需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartItem.setId(snowFlakeIdGenerator.nextId());
            cartRepository.save(cartItem);
        }else{
            //这个产品已经在购物车里了.
            //如果产品已存在,数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);

        }
        Session session = (Session) entityManager.getDelegate();
        //刷入数据库
        session.flush();
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> update(Long userId, Long productId, Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartRepository.findByUserIdAndProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> deleteProduct(Long userId, String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        List<Long> collect = productList.stream().map(Long::parseLong).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collect)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartRepository.deleteByUserIdAndProductIdIn(userId,collect);
        return this.list(userId);
    }


    @Override
    public ServerResponse<CartVo> list (Long userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }



    @Override
    public ServerResponse<CartVo> selectOrUnSelect (Long userId, Long productId, Integer checked){
        Cart cart = cartRepository.findByUserIdAndProductId(userId, productId);
        cart.setChecked(checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Long userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        List<Cart> cartList = cartRepository.findByUserId(userId);
        int count = (int)cartList.stream().count();
        return ServerResponse.createBySuccess(count);
    }

    @Override
    public ServerResponse<List<Cart>> checkedCartByUserId(Long userId) {
        if(userId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return ServerResponse.createBySuccess(cartRepository.findByUserId(userId));
    }

    @Override
    public void deleteCartById(Long cartId) {
        cartRepository.deleteById(cartId);
    }


    private CartVo getCartVoLimit(Long userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartRepository.findByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(!CollectionUtils.isEmpty(cartList)){
            for(Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());
                ServerResponse<Product> productServerResponse = productService.productInfoById(cartItem.getProductId());
                Product product = productServerResponse.getData();
                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足的时候
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartRepository.save(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                    cartProductVoList.add(cartProductVo);
                }else{
                    cartItem.setChecked(Const.Cart.UN_CHECKED);
                }

                if(cartItem.getChecked() == Const.Cart.CHECKED){
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }

            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost("");

        return cartVo;
    }

    private boolean getAllCheckedStatus(Long userId){
        if(userId == null){
            return false;
        }
        List<Cart> cartList = cartRepository.findByUserIdAndChecked(userId, 1);
        if(CollectionUtils.isEmpty(cartList)){
            return true;
        }else{
            return false;
        }

    }

}
