package com.humor.cart.configuration.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangshaoze
 * @date 2018/11/18 7:43 PM
 */
@Component
@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * 生产者配置
     */
    public Map<String,Object> providerConfig(){
        HashMap<String, Object> map = new HashMap<>(16);
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        //指定了有多少分区副本收到消息，生产者才会认为写入成功
        map.put(ProducerConfig.ACKS_CONFIG,1);
        //当有多个消息需要被发送到同一个分区时，生产者会把它们放在同一个批次。（有助于客户端和服务器的性能）
        //该参数指定了一个批次可以使用的内存大小，按照字节计算。
        map.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        //设置生产者在发送批次之前，等待更多消息加入批次的时间。生产者会在批次填满（batch-size）或者linger.ms达到上限时把批次发送出去
        map.put(ProducerConfig.LINGER_MS_CONFIG,10);
        //设置生产者内存缓冲区大小，生产者用它缓冲要送到服务器的消息。
        //如果应用程序发送消息的速度超过发送到服务器的速度，则生产者send（）将被阻塞，max.block.ms之后将抛出异常
        map.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        //用户提供的序列化程序或分区程序中的阻塞不会计入此超时
        map.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,50);
        //任意字符串，服务器会用它来识别消息的来源，还可以用在日志和配额指标里
        map.put(ProducerConfig.CLIENT_ID_CONFIG,"cart-producer");
        //由google发明，占用较少的cpu，却能提供较好的性能和相当可观的压缩比。
        map.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");
        //默认每次重试间隔100ms，可以通过retry.backoff.ms参数来改变这个时间间隔。
        //如果不设置max.in.flight.requests.per.connection=1可能会更改发生重试记录的顺序，因为将两个批次发送到同一分区，第一个批次失败并重试；另一个批次发送成功，则第二个批次中的记录在分区中的偏移量可能相对小。
        map.put(ProducerConfig.RETRIES_CONFIG,2);
        //重试时间间隔，默认100
        map.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,100);
        //设置生产者在收到服务器响应之前可以发送多少个消息。
        //值越高，就会占用越多的内存，相应的吞吐量提高。如果设置为1可以保证消息是按照发送的顺序写入服务器的（即使发生重试）
        map.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,5);
        //设置生产者在发送数据时等待服务器返回响应的最大时间，该值应大于replica.lag.time.max.ms(默认值10000)，以减少由于重试而导致的消息重复的可能性
        map.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,1000);
        //设置生产者发送请求的最大大小（单位：字节）。指单个消息的最大值或者单个请求（批次）中所有消息总的大小。broker对可接受的消息最大值也有自己的限制（message.max.bytes）,所以这两边的配置最好可以匹配，避免生产者发送的消息被broker拒绝。
        map.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,1048576);
        return map;
    }

    @Bean
    public ProducerFactory<String,String> producerFactory(){
        DefaultKafkaProducerFactory<String, String> factory
                = new DefaultKafkaProducerFactory<>(providerConfig());
        //开启事务功能
        factory.transactionCapable();
        factory.setTransactionIdPrefix("tran-");
        return factory;
    }

    @Bean
    public KafkaTransactionManager kafkaTransactionManager(){
        return new KafkaTransactionManager(producerFactory());
    }

    @Bean
    public KafkaTemplate<String,String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }


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
                            kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        //并发消费
        factory.setConcurrency(5);
        //批量消费
        factory.setBatchListener(true);
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }


}
