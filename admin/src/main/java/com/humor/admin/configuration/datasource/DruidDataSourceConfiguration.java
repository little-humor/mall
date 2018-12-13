package com.humor.admin.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.humor.admin.configuration.config.CartDruidDataSourceProperties;
import com.humor.admin.configuration.config.ProductDruidDataSourceProperties;
import com.humor.admin.configuration.config.UserDruidDataSourceProperties;
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
    private UserDruidDataSourceProperties userDruidDataSourceProperties;

    @Autowired
    private ProductDruidDataSourceProperties productDruidDataSourceProperties;

    @Autowired
    private CartDruidDataSourceProperties cartDruidDataSourceProperties;


    @Bean("userDataSource")
    @Primary
    public DataSource userDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(userDruidDataSourceProperties.getUserUrl());
        datasource.setUsername(userDruidDataSourceProperties.getUsername());
        datasource.setPassword(userDruidDataSourceProperties.getPassword());
        datasource.setDriverClassName(userDruidDataSourceProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(userDruidDataSourceProperties.getInitialSize());
        datasource.setMinIdle(userDruidDataSourceProperties.getMinIdle());
        datasource.setMaxActive(userDruidDataSourceProperties.getMaxActive());
        datasource.setMaxWait(userDruidDataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(userDruidDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(userDruidDataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(userDruidDataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(userDruidDataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(userDruidDataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(userDruidDataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(userDruidDataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(userDruidDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(userDruidDataSourceProperties.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(userDruidDataSourceProperties.getConnectionProperties());
        log.info("user admin datasource 初始化成功");
        return datasource;
    }

    @Bean("productDataSource")
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

    @Bean("cartDataSource")
    public DataSource cartDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(cartDruidDataSourceProperties.getCartUrl());
        datasource.setUsername(cartDruidDataSourceProperties.getUsername());
        datasource.setPassword(cartDruidDataSourceProperties.getPassword());
        datasource.setDriverClassName(cartDruidDataSourceProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(cartDruidDataSourceProperties.getInitialSize());
        datasource.setMinIdle(cartDruidDataSourceProperties.getMinIdle());
        datasource.setMaxActive(cartDruidDataSourceProperties.getMaxActive());
        datasource.setMaxWait(cartDruidDataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(cartDruidDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(cartDruidDataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(cartDruidDataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(cartDruidDataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(cartDruidDataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(cartDruidDataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(cartDruidDataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(cartDruidDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(cartDruidDataSourceProperties.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(cartDruidDataSourceProperties.getConnectionProperties());
        log.info("cart admin datasource 初始化成功");
        return datasource;
    }

}
