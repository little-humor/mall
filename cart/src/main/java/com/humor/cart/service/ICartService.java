package com.humor.cart.service;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Cart;
import com.humor.domain.vo.CartVo;

import java.util.List;

/**
 * Created by humor
 */
public interface ICartService {
    ServerResponse<CartVo> add(Long userId, Long productId, Integer count);
    ServerResponse<CartVo> update(Long userId, Long productId, Integer count);
    ServerResponse<CartVo> deleteProduct(Long userId, String productIds);

    ServerResponse<CartVo> list(Long userId);
    ServerResponse<CartVo> selectOrUnSelect(Long userId, Long productId, Integer checked);
    ServerResponse<Integer> getCartProductCount(Long userId);



    //服务间调用
    ServerResponse<List<Cart>> checkedCartByUserId(Long userId);

    void deleteCartById(Long cartId);
}
