package com.humor.order.service.api;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Shipping;
import com.humor.order.service.api.fallback.ShippingServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangshaoze
 */
@FeignClient(name="shipping-server",path = "shipping-server/shipping",fallback = ShippingServiceFallback.class)
public interface ShippingService {

    @GetMapping("/shippingById.do")
    ServerResponse<Shipping> shippingById(@RequestParam("userId") Long userId, @RequestParam("shippingId") Long shippingId);

}
