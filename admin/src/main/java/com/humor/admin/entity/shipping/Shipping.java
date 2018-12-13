package com.humor.admin.entity.shipping;

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
@Table(name="shipping")
@EntityListeners(AuditingEntityListener.class)
public class Shipping {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}