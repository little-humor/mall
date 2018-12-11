package com.humor.order.service.api;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Product;
import com.humor.order.service.api.fallback.ProductServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangshaoze
 */
@FeignClient(name = "product-server",path = "product-server/product",fallback = ProductServiceFallback.class)
public interface ProductService {

    @GetMapping("productInfoById.do")
    ServerResponse<Product> productInfoById(@RequestParam("productId") Long productId);


    @PutMapping("updateProductById.do")
    void updateProductById(@RequestBody Product product);



}
