package com.humor.admin.repository.product;

import com.humor.admin.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zhangshaoze
 * @create 2018-11-09 1:31 PM
 */
public interface CategoryRepository extends JpaRepository<Category,Integer>,JpaSpecificationExecutor<Category> {

    List<Category> findCategoryByParentId(Integer parentId);

    @Query(nativeQuery = true,value = "select id from category where parent_id = ?1")
    List<Integer> findIdByParentId(Integer parentId);

}
