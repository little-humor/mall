package com.humor.common.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhangshaoze
 * @date 2018/11/18 3:17 PM
 */
@Data
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class PartitionOffset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String topics;

    private Integer partitions;

    private Long offsets;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}
