package com.humor.common.configuration.jpa;

import org.springframework.beans.factory.annotation.Autowired;
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
 * 将数据源注入到 实体管理器工厂，配置 repository、entity 的位置
 * @author zhangshaoze
 * @date 2018/11/21 3:52 PM
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        //指定EntityManager的创建工厂Bean
        entityManagerFactoryRef = "commonEntityManagerFactory",
        //指定事物管理的Bean
        transactionManagerRef = "commonTransactionManager",
        //设置Repository所在位置
        basePackages = {"com.humor.common.repository"})
public class CommonJpaConfiguration {

    @Autowired
    private DataSource commonDataSource;

    @Autowired
    private JpaProperties jpaProperties;

    /**
     * jpa实体管理工厂
     * @param builder
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean commonEntityManagerFactory (EntityManagerFactoryBuilder builder) {
        return builder
                //设置数据源
                .dataSource(commonDataSource)
                //设置实体类所在位置.扫描所有带有 @Entity 注解的类
                .packages("com.humor.common.entity")
                .persistenceUnit("commonpersistenceUnit")
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
    public PlatformTransactionManager commonTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(commonEntityManagerFactory(builder).getObject());
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
