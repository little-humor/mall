package com.humor.product.repository;

import com.humor.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zhangshaoze
 */
public interface ProductRepository extends JpaRepository<Product,Long>,JpaSpecificationExecutor<Product> {

    List<Product> findByIdAndNameLike(Long id,String productName);

    Page<Product> findByNameLike(String productName, Pageable pageable);

    Page<Product> findByCategoryIdIn(List<Long> categoryList,Pageable pageable);

    Page<Product> findByNameLikeAndCategoryIdIn(String productName, List<Long> categoryList, Pageable pageable);

    @Modifying
    @Query("update Product set stock = stock - ?2 where id = ?1 and stock > ?2")
    int updateStock(Integer stock,Long id);

}
