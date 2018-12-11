package com.humor.admin.service;


import com.humor.admin.common.ServerResponse;
import com.humor.admin.entity.product.Product;

/**
 * Created by humor
 */
public interface IProductService {

    /**
     * 保存/更新
     * @param product
     * @return
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 设置商品状态
     * @param productId
     * @param status
     * @return
     */
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    /**
     * 检索商品
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize);
}
