package com.humor.cart.service.api.fallback;

import com.humor.cart.service.api.ProductService;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhangshaoze
 */
@Slf4j
@Component
public class ProductServiceFallback implements ProductService {
    @Override
    public ServerResponse<Product> productInfoById(Long productId) {

        ServerResponse<Product> serverResponse = ServerResponse.createBySuccessMessage("该商品【"+productId+"】不存在");
        log.info("product-server-->服务降级,productId:{}",productId);
        return serverResponse;
    }

}
