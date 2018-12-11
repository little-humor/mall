package com.humor.admin.entity.product;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;


}