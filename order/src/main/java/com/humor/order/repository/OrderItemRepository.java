package com.humor.order.repository;

import com.humor.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhangshaoze
 * @create 2018-11-11 10:52 PM
 */
public interface OrderItemRepository extends JpaRepository<OrderItem,Long>,JpaSpecificationExecutor<OrderItem> {

    List<OrderItem> findByUserIdAndOrderNo(Long userId,Long orderNo);

    List<OrderItem> findByOrderNo(Long orderNo);

}
