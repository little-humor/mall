package com.humor.admin.repository.product;

import com.humor.admin.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhangshaoze
 */
public interface ProductRepository extends JpaRepository<Product,Integer>,JpaSpecificationExecutor<Product> {


    /**
     * 根据关键字检索
     * @param productName
     * @param pageable
     * @return
     */
    Page<Product> findByNameLike(String productName,Pageable pageable);

    /**
     * 根据分类检索
     * @param categoryList
     * @param pageable
     * @return
     */
    Page<Product> findByCategoryIdIn(List<Integer> categoryList,Pageable pageable);

    /**
     * 根据关键字+分类检索
     * @param productName
     * @param categoryList
     * @param pageable
     * @return
     */
    Page<Product> findByNameLikeAndCategoryIdIn(String productName, List<Integer> categoryList, Pageable pageable);

}
