package com.humor.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.vo.OrderVo;

import java.util.List;

/**
 * Created by humor
 */
public interface IOrderService {
    ServerResponse queryOrderPayStatus(Long userId, Long orderNo);
    ServerResponse createOrder(Long userId, Long shippingId) throws JsonProcessingException;
    ServerResponse<String> cancel(Long userId, Long orderNo);
    ServerResponse getOrderCartProduct(Long userId);
    ServerResponse<OrderVo> getOrderDetail(Long userId, Long orderNo);
    ServerResponse getOrderList(Long userId, int pageNum, int pageSize);



    //backend
    ServerResponse<List<OrderVo>> manageList(int pageNum, int pageSize);
    ServerResponse<OrderVo> manageDetail(Long orderNo);
    ServerResponse manageSearch(Long orderNo, int pageNum, int pageSize);
    ServerResponse<String> manageSendGoods(Long orderNo);


}
