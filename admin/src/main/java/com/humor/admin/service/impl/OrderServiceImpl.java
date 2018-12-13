package com.humor.admin.service.impl;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.entity.order.Order;
import com.humor.admin.repository.order.OrderRepository;
import com.humor.admin.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ServerResponse findAll(Long userId,Integer status, int page, int limit){

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"createTime");
        Page<Order> orders = orderRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList();
            if (userId != null) {
                list.add(cb.equal(root.get("userId").as(Long.class), userId));
            }
            if(status != null){
                list.add(cb.equal(root.get("status").as(Long.class),status));
            }

            Predicate[] predicates = new Predicate[list.size()];
            query.where(list.toArray(predicates));
            return query.getRestriction();
        }, new PageRequest(page, limit, new Sort(order)));

        ServerResponse<List<Order>> serverResponse = ServerResponse.createBySuccess(orders.getContent());
        serverResponse.setCount(orders.getTotalElements());
        return  serverResponse;
    }

}
