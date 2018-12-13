package com.humor.admin.service;

import com.humor.admin.common.ServerResponse;

public interface IUserService {

    ServerResponse findByUserName(String userName, int page, int limit);
}
