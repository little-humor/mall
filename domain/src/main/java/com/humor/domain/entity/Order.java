package com.humor.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangshaoze
 */
@Data
@Entity
@Table(name = "order_main")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    private Long id;

    private Long userId;

    private Long shippingId;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}