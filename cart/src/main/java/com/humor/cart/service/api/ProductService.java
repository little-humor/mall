package com.humor.cart.service.api;

import com.humor.cart.service.api.fallback.ProductServiceFallback;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangshaoze
 */
@FeignClient(name = "product-server",path = "product-server/product",fallback = ProductServiceFallback.class)
public interface ProductService {

    @GetMapping("/productInfoById.do")
    ServerResponse<Product> productInfoById(@RequestParam("productId") Long productId);


}
