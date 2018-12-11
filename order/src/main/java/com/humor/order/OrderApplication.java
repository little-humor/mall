package com.humor.order;

import com.humor.domain.common.SnowFlakeIdGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * @author zhangshaoze
 */
@ComponentScans(@ComponentScan("com.humor.common"))
@EntityScan({"com.humor.domain.entity","com.humor.common.entity"})
@EnableJpaAuditing
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public SnowFlakeIdGenerator snowFlakeIdGenerator(){
        return new SnowFlakeIdGenerator(0,0);
    }


}
