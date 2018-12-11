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
@Table(name = "order_item")
@EntityListeners(AuditingEntityListener.class)
public class OrderItem {

    @Id
    private Long id;

    private Long orderNo;

    private Long productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

    private Long userId;

}