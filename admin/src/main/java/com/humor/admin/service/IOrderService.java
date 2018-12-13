package com.humor.admin.service;

import com.humor.admin.common.ServerResponse;

public interface IOrderService {

    ServerResponse findAll(Long userId, Integer status, int page, int limit);
}
