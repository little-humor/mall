package com.humor.order.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.humor.order.configuration.config.DruidDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author zhangshaoze
 * @date 2018/11/22 9:38 AM
 */
@Slf4j
@Configuration
public class DruidDataSourceConfiguration {

    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties;

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(druidDataSourceProperties.getUrl());
        datasource.setUsername(druidDataSourceProperties.getUsername());
        datasource.setPassword(druidDataSourceProperties.getPassword());
        datasource.setDriverClassName(druidDataSourceProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(druidDataSourceProperties.getInitialSize());
        datasource.setMinIdle(druidDataSourceProperties.getMinIdle());
        datasource.setMaxActive(druidDataSourceProperties.getMaxActive());
        datasource.setMaxWait(druidDataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(druidDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(druidDataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(druidDataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(druidDataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(druidDataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(druidDataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(druidDataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(druidDataSourceProperties.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(druidDataSourceProperties.getConnectionProperties());
        log.info("order datasource 初始化成功");
        return datasource;
    }

}
