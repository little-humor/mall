package com.humor.admin.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @author zhangshaoze
 */
@Data
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    @Id
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(unique = true,nullable = false)
    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}