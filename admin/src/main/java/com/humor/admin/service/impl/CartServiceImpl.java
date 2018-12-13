package com.humor.admin.service.impl;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.repository.cart.CartRepository;
import com.humor.admin.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;

public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    public ServerResponse findByProductIdAndUserId(Long productId,Long userId){

        return null;

    }

}
