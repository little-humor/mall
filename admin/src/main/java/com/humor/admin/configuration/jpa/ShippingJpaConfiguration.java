package com.humor.admin.configuration.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 商品数据库 jpa
 * @author zhangshaoze
 * @date 2018/11/21 3:52 PM
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        //指定EntityManager的创建工厂Bean
        entityManagerFactoryRef = "shippingEntityManagerFactory",
        //指定事物管理的Bean
        transactionManagerRef = "shippingTransactionManager",
        //设置Repository所在位置
        basePackages = {"com.humor.admin.repository.shipping"})
public class ShippingJpaConfiguration {

    @Autowired
    @Qualifier("shippingDataSource")
    private DataSource shippingDataSource;

    @Autowired
    private JpaProperties jpaProperties;

    /**
     * jpa实体管理工厂
     * @param builder
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean shippingEntityManagerFactory (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(shippingDataSource)
                //设置实体类所在位置
                .packages("com.humor.admin.entity.shipping")
                .persistenceUnit("shippingPersistenceUnit")
                //设置hibernate通用配置
                .properties(getVendorProperties())
                .build();
    }
    /**
     * jpa事务管理
     * @param builder
     * @return
     */
    @Bean
    public PlatformTransactionManager shippingTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(shippingEntityManagerFactory(builder).getObject());
    }


    /**
     * jpa通用配置属性
     * @return
     */
    private Map<String, Object> getVendorProperties() {
        //HibernateSettings类其实就是配置列名生成策略的，我们已经在yml里配置过了，这里直接new一个空类过去就行了
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

}
