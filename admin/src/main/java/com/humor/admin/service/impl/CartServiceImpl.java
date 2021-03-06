package com.humor.admin.service.impl;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.entity.cart.Cart;
import com.humor.admin.repository.cart.CartRepository;
import com.humor.admin.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public ServerResponse findByProductIdAndUserId(Long productId,Long userId,int page,int limit){

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"createTime");
        Page<Cart> carts = cartRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList();
            if (productId != null) {
                list.add(cb.equal(root.get("productId").as(Long.class), productId));
            }
            if (userId != null) {
                list.add(cb.equal(root.get("userId").as(Long.class), userId));
            }
            Predicate[] predicates = new Predicate[list.size()];
            query.where(list.toArray(predicates));
            return query.getRestriction();
        }, new PageRequest(page, limit,new Sort(order)));

        ServerResponse<List<Cart>> serverResponse = ServerResponse.createBySuccess(carts.getContent());
        serverResponse.setCount(carts.getTotalElements());
        return  serverResponse;
    }

}
