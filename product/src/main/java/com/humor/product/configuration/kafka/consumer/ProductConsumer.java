package com.humor.product.configuration.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humor.product.repository.ProductRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangshaoze
 * @date 2018/11/16 4:30 PM
 */
@Slf4j
@Component
public class ProductConsumer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PartitionOffsetRepository partitionOffsetRepository;


    /**
     * 监听->「减库存」事件
     * @param recordList
     * @throws IOException
     */
    @SendTo({"forwarding_topic"})
    @KafkaListener(id = "subStock",
                   topics = {"subtract_stock"},
                   containerFactory = "kafkaListenerContainerFactory",
                   errorHandler = "consumerAwareListenerErrorHandler")
    @Transactional(rollbackFor = Exception.class)
    public void subStock(List<ConsumerRecord<String,String>> recordList,
                              Consumer<String,String> consumer) throws IOException {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            for(ConsumerRecord<String,String> record : recordList){
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                Map<Long,Integer> map = objectMapper.readValue(record.value(), Map.class);

                map.forEach((key,value) -> {
                    //记录处理
                    productRepository.updateStock(value,key);
                });
                //持久化记录的偏移量
                partitionOffsetRepository.updateOffsetsByTopicsAndPartitions(topic,partition,offset);
                consumer.commitAsync();
            }
        }catch (WakeupException e){
            //忽略该异常，正常关闭消费者
        }catch (Exception e){
            log.error("「减库存」事件 error",e);
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
