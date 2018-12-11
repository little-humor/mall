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
@Table(name="category")
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    private Long id;

    private Long parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;


}