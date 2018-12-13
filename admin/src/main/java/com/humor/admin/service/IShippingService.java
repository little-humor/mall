package com.humor.admin.service;

import com.humor.admin.common.ServerResponse;

public interface IShippingService {

    ServerResponse findByUserId(Long userId, int page, int limit);
}
