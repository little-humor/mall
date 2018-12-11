package com.humor.admin.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.humor.admin.configuration.config.ProductDruidDataSourceProperties;
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
    private ProductDruidDataSourceProperties productDruidDataSourceProperties;

    @Bean
    @Primary
    public DataSource productDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(productDruidDataSourceProperties.getProductUrl());
        datasource.setUsername(productDruidDataSourceProperties.getUsername());
        datasource.setPassword(productDruidDataSourceProperties.getPassword());
        datasource.setDriverClassName(productDruidDataSourceProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(productDruidDataSourceProperties.getInitialSize());
        datasource.setMinIdle(productDruidDataSourceProperties.getMinIdle());
        datasource.setMaxActive(productDruidDataSourceProperties.getMaxActive());
        datasource.setMaxWait(productDruidDataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(productDruidDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(productDruidDataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(productDruidDataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(productDruidDataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(productDruidDataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(productDruidDataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(productDruidDataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(productDruidDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(productDruidDataSourceProperties.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(productDruidDataSourceProperties.getConnectionProperties());
        log.info("product admin datasource 初始化成功");
        return datasource;
    }

}
