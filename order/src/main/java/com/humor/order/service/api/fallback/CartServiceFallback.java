package com.humor.order.service.api.fallback;

import com.humor.domain.common.ResponseCode;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Cart;
import com.humor.order.service.api.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangshaoze
 */
@Slf4j
@Component
public class CartServiceFallback implements CartService {
    @Override
    public ServerResponse<List<Cart>> checkedCartByUserId(Long userId) {
        ServerResponse<List<Cart>> serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.SERVER_DOWN.getCode(), ResponseCode.SERVER_DOWN.getDesc());
        return serverResponse;
    }

    @Override
    public void deleteCartById(Long cartId) {
        log.warn("删除购物车操作--服务降级");
    }
}
