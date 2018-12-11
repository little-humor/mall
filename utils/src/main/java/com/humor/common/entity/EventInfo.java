package com.humor.common.entity;

import com.humor.domain.common.EventStatus;
import com.humor.domain.common.EventType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.Date;

/**
 *
 * @author zhangshaoze
 * @date 2018/11/16 10:58 AM
 */
@Data
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class EventInfo {

    @Id
    private Long id;

    private String content;

    private EventType eventType;

    private EventStatus eventStatus;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}
