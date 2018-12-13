package com.humor.admin.service.impl;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.entity.shipping.Shipping;
import com.humor.admin.repository.shipping.ShippingRepository;
import com.humor.admin.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public ServerResponse findByUserId(Long userId, int page, int limit){

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"createTime");
        Page<Shipping> shippings = shippingRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList();
            if (userId != null) {
                list.add(cb.equal(root.get("userId").as(Long.class), userId));
            }
            Predicate[] predicates = new Predicate[list.size()];
            query.where(list.toArray(predicates));
            return query.getRestriction();
        }, new PageRequest(page, limit, new Sort(order)));

        ServerResponse<List<Shipping>> serverResponse = ServerResponse.createBySuccess(shippings.getContent());
        serverResponse.setCount(shippings.getTotalElements());
        return  serverResponse;
    }

}
