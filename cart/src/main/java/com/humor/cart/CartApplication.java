package com.humor.cart;

import com.humor.domain.common.SnowFlakeIdGenerator;
import feign.Logger;
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
public class CartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }

    @Bean
    public SnowFlakeIdGenerator snowFlakeIdGenerator(){
        return new SnowFlakeIdGenerator(2,2);
    }

    /**
     * NONE:不记录任何信息（默认）
     * Basic：仅记录请求方法/URl以及响应状态码和执行时间
     * headers：除了记录basic级别的信息外，还会记录请求和响应的头信息
     * full：记录所有请求与响应的明细，包括头信息，请求体，元数据
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}
