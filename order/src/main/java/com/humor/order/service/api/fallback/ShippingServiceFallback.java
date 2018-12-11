package com.humor.order.service.api.fallback;

import com.humor.domain.common.ResponseCode;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Shipping;
import com.humor.order.service.api.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhangshaoze
 */
@Slf4j
@Component
public class ShippingServiceFallback implements ShippingService {
    @Override
    public ServerResponse<Shipping> shippingById(Long userId, Long shippingId) {

        ServerResponse<Shipping> serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.SERVER_DOWN.getCode(), ResponseCode.SERVER_DOWN.getDesc());
        return serverResponse;
    }
}
