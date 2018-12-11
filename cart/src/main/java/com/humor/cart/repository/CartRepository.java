package com.humor.cart.repository;

import com.humor.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhangshaoze
 * @create 2018-11-11 10:03 PM
 */
public interface CartRepository extends JpaRepository<Cart,Long>,JpaSpecificationExecutor<Cart> {

    Cart findByIdAndUserId(Long id,Long userId);

    Cart findByUserIdAndProductId(Long userId,Long productId);

    List<Cart> findByUserId(Long userId);

    List<Cart> findByUserIdAndChecked(Long userId,Integer checked);

    void deleteByUserIdAndProductIdIn(Long userId,List productIdList);

    void deleteByIdIn(List<Long> ids);
}
