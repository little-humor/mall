package com.humor.common.configuration.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zhangshaoze
 * @date 2018/11/21 2:17 PM
 */
@Data
@Component
@PropertySource("classpath:jdbc.properties")
public class CommonDataSourceProperties {

    @Value("${url}")
    private String url;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${driverClassName}")
    private String driverClassName;
    @Value("${initialSize}")
    private int initialSize;
    @Value("${minIdle}")
    private int minIdle;
    @Value("${maxActive}")
    private int maxActive;
    @Value("${maxWait}")
    private int maxWait;
    @Value("${timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${validationQuery}")
    private String validationQuery;
    @Value("${testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${testOnReturn}")
    private boolean testOnReturn;
    @Value("${poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${filters}")
    private String filters;
    @Value("${connectionProperties}")
    private String connectionProperties;

}
