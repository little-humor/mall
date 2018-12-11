package com.humor.order.scheduler;

import com.humor.common.entity.EventInfo;
import com.humor.common.repository.EventProviderRepository;
import com.humor.domain.common.EventStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 定时任务-轮询事件表状态为new的数据发布到kafka
 * @author zhangshaoze
 * @date 2018/11/16 3:26 PM
 */
@Slf4j
@Component
public class EventPublishKafkaSchedule {

    @Autowired
    private EventProviderRepository eventProviderRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Scheduled(initialDelay = 1000,fixedDelay = 3000)
    public void publishKafka(){
        List<EventInfo> eventList = eventProviderRepository.findByEventStatus(EventStatus.NEW);
        if(!CollectionUtils.isEmpty(eventList)){
            eventList.stream().forEach(event -> {
                try {
                    kafkaTemplate.send(event.getEventType().name(),event.getContent()).get();
                    event.setEventStatus(EventStatus.FINISH);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("发布Kafka消息失败，eventPublish={}", event, e);
                }
            });
        }
    }

}
