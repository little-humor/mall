package com.humor.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhangshaoze
 */
@Data
@Entity
@Table(name = "cart")
@EntityListeners(AuditingEntityListener.class)
public class Cart {
    @Id
    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

    private Integer checked;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}