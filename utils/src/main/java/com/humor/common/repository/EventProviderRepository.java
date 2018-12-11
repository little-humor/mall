package com.humor.common.repository;

import com.humor.common.entity.EventInfo;
import com.humor.domain.common.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhangshaoze
 * @create 2018-11-16 11:15 AM
 */
public interface EventProviderRepository extends JpaRepository<EventInfo,Long>,JpaSpecificationExecutor<EventInfo> {

    List<EventInfo> findByEventStatus(EventStatus eventStatus);

}
