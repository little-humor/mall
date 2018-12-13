package com.humor.admin.repository.shipping;

import com.humor.admin.entity.shipping.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShippingRepository extends JpaRepository<Shipping,Long>,JpaSpecificationExecutor<Shipping> {
}
