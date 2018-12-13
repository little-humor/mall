package com.humor.admin.repository.order;

import com.humor.admin.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order,Long>,JpaSpecificationExecutor<Order> {
}
