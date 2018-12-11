package com.humor.order.service.api;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Cart;
import com.humor.order.service.api.fallback.CartServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by humor
 */
@FeignClient(name = "cart-server",path = "cart-server/cart",fallback = CartServiceFallback.class)
public interface CartService {

    @GetMapping("/checkedCartByUserId.do")
    ServerResponse<List<Cart>> checkedCartByUserId(@RequestParam("userId") Long userId);

    @DeleteMapping("/deleteCartById.do")
    void deleteCartById(@RequestParam("cartId") Long cartId);



}
