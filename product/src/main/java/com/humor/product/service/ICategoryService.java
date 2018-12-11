package com.humor.product.service;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Category;

import java.util.List;

/**
 * Created by humor
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Long parentId);
    ServerResponse updateCategoryName(Long categoryId, String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Long categoryId);
    ServerResponse<List<Long>> selectCategoryAndChildrenById(Long categoryId);

}
