package com.humor.shipping.service;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Shipping;
import org.springframework.data.domain.Page;

/**
 * @author zhangshaoze
 */
public interface IShippingService {

    ServerResponse add(Long userId, Shipping shipping);
    ServerResponse<String> del(Long userId, Long shippingId);
    ServerResponse update(Long userId, Shipping shipping);
    ServerResponse<Shipping> select(Long userId, Long shippingId);
    ServerResponse<Page<Shipping>> list(Long userId, int pageNum, int pageSize);

}
