package com.humor.product.repository;

import com.humor.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zhangshaoze
 * @create 2018-11-09 1:31 PM
 */
public interface CategoryRepository extends JpaRepository<Category,Long>,JpaSpecificationExecutor<Category> {

    List<Category> findCategoryByParentId(Long parentId);

    @Query(nativeQuery = true,value = "select id from category where parent_id = ?1")
    List<Long> findIdByParentId(Long parentId);

}
