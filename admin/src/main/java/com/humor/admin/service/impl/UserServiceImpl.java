package com.humor.admin.service.impl;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.entity.user.User;
import com.humor.admin.repository.user.UserRepository;
import com.humor.admin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ServerResponse findByUserName(String userName, int page, int limit){

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"createTime");
        Page<User> users = userRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList();
            if (userName != null) {
                list.add(cb.like(root.get("userName").as(String.class), "%" + userName + "%"));
            }
            Predicate[] predicates = new Predicate[list.size()];
            query.where(list.toArray(predicates));
            return query.getRestriction();
        }, new PageRequest(page, limit, new Sort(order)));

        ServerResponse<List<User>> serverResponse = ServerResponse.createBySuccess(users.getContent());
        serverResponse.setCount(users.getTotalElements());
        return  serverResponse;
    }

}
