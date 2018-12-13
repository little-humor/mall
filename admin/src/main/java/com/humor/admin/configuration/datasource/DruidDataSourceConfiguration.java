package com.humor.admin.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.humor.admin.configuration.config.*;
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

    @Autowired
    private ShippingDruidDataSourceProperties shippingDruidDataSourceProperties;

    @Autowired
    private OrderDruidDataSourceProperties orderDruidDataSourceProperties;

    @Primary
    @Bean("userDataSource")
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

    @Bean("shippingDataSource")
    public DataSource shippingDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(shippingDruidDataSourceProperties.getShippingUrl());
        datasource.setUsername(shippingDruidDataSourceProperties.getUsername());
        datasource.setPassword(shippingDruidDataSourceProperties.getPassword());
        datasource.setDriverClassName(shippingDruidDataSourceProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(shippingDruidDataSourceProperties.getInitialSize());
        datasource.setMinIdle(shippingDruidDataSourceProperties.getMinIdle());
        datasource.setMaxActive(shippingDruidDataSourceProperties.getMaxActive());
        datasource.setMaxWait(shippingDruidDataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(shippingDruidDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(shippingDruidDataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(shippingDruidDataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(shippingDruidDataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(shippingDruidDataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(shippingDruidDataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(shippingDruidDataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(shippingDruidDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(shippingDruidDataSourceProperties.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(shippingDruidDataSourceProperties.getConnectionProperties());
        log.info("shipping admin datasource 初始化成功");
        return datasource;
    }


    @Bean("orderDataSource")
    public DataSource orderDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(orderDruidDataSourceProperties.getOrderUrl());
        datasource.setUsername(orderDruidDataSourceProperties.getUsername());
        datasource.setPassword(orderDruidDataSourceProperties.getPassword());
        datasource.setDriverClassName(orderDruidDataSourceProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(orderDruidDataSourceProperties.getInitialSize());
        datasource.setMinIdle(orderDruidDataSourceProperties.getMinIdle());
        datasource.setMaxActive(orderDruidDataSourceProperties.getMaxActive());
        datasource.setMaxWait(orderDruidDataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(orderDruidDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(orderDruidDataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(orderDruidDataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(orderDruidDataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(orderDruidDataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(orderDruidDataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(orderDruidDataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(orderDruidDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(orderDruidDataSourceProperties.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(orderDruidDataSourceProperties.getConnectionProperties());
        log.info("order admin datasource 初始化成功");
        return datasource;
    }

}
