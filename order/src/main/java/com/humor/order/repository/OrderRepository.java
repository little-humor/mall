package com.humor.order.repository;

import com.humor.domain.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhangshaoze
 */
public interface OrderRepository extends JpaRepository<Order,Integer>,JpaSpecificationExecutor<Order> {

    Order findByUserIdAndId(Long userId,Long id);

    List<Order> findByUserId(Long userId, Pageable pageable);

    Order findById(Long id);
}
