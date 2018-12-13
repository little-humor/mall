package com.humor.admin.repository.cart;

import com.humor.admin.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartRepository extends JpaRepository<Cart,Long>, JpaSpecificationExecutor<Cart> {



}
