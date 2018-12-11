package com.humor.user;

import com.humor.domain.common.SnowFlakeIdGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author zhangshaoze
 */
@ComponentScans(@ComponentScan("com.humor.common"))
@EntityScan({"com.humor.domain.entity","com.humor.configuration.entity"})
@EnableJpaAuditing
@EnableEurekaClient
@SpringBootApplication
@EnableAspectJAutoProxy
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    public SnowFlakeIdGenerator snowFlakeIdGenerator(){
        return new SnowFlakeIdGenerator(4,4);
    }

}
