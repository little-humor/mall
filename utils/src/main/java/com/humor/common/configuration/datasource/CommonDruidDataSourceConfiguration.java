package com.humor.common.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.humor.common.configuration.config.CommonDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author zhangshaoze
 * @date 2018/11/20 2:35 PM
 */

@Slf4j
@Configuration
public class CommonDruidDataSourceConfiguration {

    @Autowired
    private CommonDataSourceProperties commonDataSourceProperties;

    @Bean("commonDataSource")
    public DataSource commonDataSource() {

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(commonDataSourceProperties.getUrl());
        datasource.setUsername(commonDataSourceProperties.getUsername());
        datasource.setPassword(commonDataSourceProperties.getPassword());
        datasource.setDriverClassName(commonDataSourceProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(commonDataSourceProperties.getInitialSize());
        datasource.setMinIdle(commonDataSourceProperties.getMinIdle());
        datasource.setMaxActive(commonDataSourceProperties.getMaxActive());
        datasource.setMaxWait(commonDataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(commonDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(commonDataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(commonDataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(commonDataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(commonDataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(commonDataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(commonDataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(commonDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(commonDataSourceProperties.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(commonDataSourceProperties.getConnectionProperties());
        log.info("common DataSource 初始化成功");
        return datasource;
    }

}


