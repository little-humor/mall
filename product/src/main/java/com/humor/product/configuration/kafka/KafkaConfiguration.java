package com.humor.product.configuration.kafka;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangshaoze
 * @date 2018/11/18 7:43 PM
 */
@Data
@EnableKafka
@Configuration
@ConfigurationProperties("spring.kafka")
public class KafkaConfiguration {

    private String bootstrapServers;
    /**
     * 消费者配置
     */
    @Bean
    public Map<String,Object> consumerConfig(){
        HashMap<String, Object> map = new HashMap<>(16);
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        map.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        map.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,500);
        map.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        //设置消费者从broker获取记录的最小字节数。
        map.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG,1000);
        //指定了consumer等待broker返回消息的最大时间，默认值为500ms。要么在满足fetch-min-size时返回数据，要么在fetch-max-wait指定的时间后返回所有可用的数据
        map.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,100);
        //设置poll()方法向协调器发送心跳的频率，一般为session.timeout.ms的三分之一
        map.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,1000);
        //一次调用poll()时返回的最大记录条数
        map.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,500);
        //设置服务器从每个分区里返回给消费者的最大字节数。默认值1MB。该属性必须大于max.message.size(broker接收的最大消息的字节数)
        map.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,1000000);
        //设置消费者在被认为死亡之前可以与服务器断开连接的时间，默认值为3s（指定了消费者可以多久不发送心跳）
        //注意：该值必须在broker的配置group.min.session.timeout.ms（默认值6000） and group.max.session.timeout.ms（默认值：30000）范围之内！！不然启动会报超时异常
        map.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,3000);
        //分区分配策略：Range、RoundRobin
        return map;
    }

    @Bean
    public ConsumerFactory consumerFactory(){
        return new DefaultKafkaConsumerFactory<String,String>(consumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory
                            concurrentKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        //并发消费
        factory.setConcurrency(5);
        //批量消费
        factory.setBatchListener(true);
        return factory;
    }
}
