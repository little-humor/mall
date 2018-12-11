package com.humor.order.service.api.fallback;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Product;
import com.humor.order.service.api.ProductService;
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
        return serverResponse;
    }

    @Override
    public void updateProductById(Product product) {
        log.warn("更新商品操作--服务降级");
    }
}
