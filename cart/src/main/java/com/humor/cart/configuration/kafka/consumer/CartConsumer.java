package com.humor.cart.configuration.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humor.cart.repository.CartRepository;
import com.humor.common.repository.PartitionOffsetRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangshaoze
 * @date 2018/11/16 4:30 PM
 */
@Slf4j
@Component
public class CartConsumer {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PartitionOffsetRepository partitionOffsetRepository;

    /**
     * 监听->清空购物车事件
     * @param recordList
     * @throws IOException
     */
    @KafkaListener(id = "clearCart",
                   topics = {"clear_cart_by_id"},
                   containerFactory = "kafkaListenerContainerFactory",
                   errorHandler = "consumerAwareListenerErrorHandler")
    @SendTo({"forwarding_topic"})
    public void clearCartById(List<ConsumerRecord<String,String>> recordList,
                              Consumer<String,String> consumer) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            for(ConsumerRecord<String,String> record : recordList){
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                List<Long> list = objectMapper.readValue(record.value(), List.class);
                log.info("消息主题：{} 分区：{} 偏移量：{} 内容：{}",topic,partition,offset,list);
                //记录处理
                cartRepository.deleteByIdIn(list);
                //持久化记录的偏移量
                partitionOffsetRepository.updateOffsetsByTopicsAndPartitions(topic,partition,offset);
                consumer.commitAsync();
            }
        }catch (WakeupException e){
            //忽略该异常，正常关闭消费者
        }catch (Exception e){
            log.error("清空购物车事件 error",e);
        }finally {
            try {
                consumer.commitSync();
            }finally {
                consumer.close();
                log.info("consumer closed...");
            }
        }
    }


    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareListenerErrorHandler(){

        return (message,exception,consumer) -> {
            log.info("kafka异常处理器······",message.getPayload().toString());
            return null;
        };

    }

}
