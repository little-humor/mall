package com.humor.cart.configuration.kafka.impl;

import com.humor.common.entity.PartitionOffset;
import com.humor.common.repository.PartitionOffsetRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * 分区再均衡监听器
 * @author zhangshaoze
 */
@Slf4j
public class HandleRebalance implements ConsumerRebalanceListener {

    @Autowired
    private PartitionOffsetRepository partitionOffsetRepository;

    private KafkaConsumer consumer;

    public HandleRebalance(KafkaConsumer consumer){
        this.consumer = consumer;
    }

    /**
     * 再均衡之前，消费者停止读取消息之后 「执行」
     * @param collection
     */
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
        //同步提交，确保再均衡发生前提交偏移量
        consumer.commitSync();

    }

    /**
     * 再均衡之后，消费者开始读取消息之前 「执行」
     * @param collection
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        log.info("再均衡后，重新定位分区偏移量 start");
        collection.stream().forEach(topicPartition -> {
            //从数据库获取最新消费消息的偏移量offset
            PartitionOffset partitonOffset = partitionOffsetRepository.findByTopicsAndPartitions(topicPartition.topic(), topicPartition.partition());
            log.info("主题：{} 分区：{} 偏移量：{}",partitonOffset.getTopics(),partitonOffset.getPartitions(),partitonOffset.getOffsets());
            //从特定偏移量处开始处理记录
            consumer.seek(topicPartition,partitonOffset.getOffsets());
        });
        log.info("重新定位偏移量 end");
    }


}
