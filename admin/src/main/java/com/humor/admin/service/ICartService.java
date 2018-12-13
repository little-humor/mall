package com.humor.admin.service;

import com.humor.admin.common.ServerResponse;

public interface ICartService {
    ServerResponse findByProductIdAndUserId(Long productId, Long userId, int page, int limit);
}
