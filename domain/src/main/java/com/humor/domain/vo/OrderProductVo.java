package com.humor.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by humor
 * @author zhangshaoze
 */
@Data
public class OrderProductVo {

    private List<OrderItemVo> orderItemVoList;
    private BigDecimal productTotalPrice;
    private String imageHost;

}
