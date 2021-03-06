package com.humor.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangshaoze
 */
@Data
public class OrderVo {

    private Long id;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;
    private Integer postage;

    private Integer status;


    private String statusDesc;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    /**
     * 订单的明细
     */
    private List<OrderItemVo> orderItemVoList;

    private String imageHost;
    private Long shippingId;
    private String receiverName;

    private ShippingVo shippingVo;

}
