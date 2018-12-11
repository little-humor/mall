package com.humor.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangshaoze
 */
@Data
public class OrderItemVo {

    private Long orderNo;

    private Long productId;

    private String productName;
    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String createTime;

}
