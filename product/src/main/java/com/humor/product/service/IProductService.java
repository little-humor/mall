package com.humor.product.service;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Product;

/**
 * Created by humor
 */
public interface IProductService {

    ServerResponse<Product> getProductDetail(Long productId);

    ServerResponse getProductByKeywordCategory(String keyword, Long categoryId, int pageNum, int pageSize, String orderBy);

}
