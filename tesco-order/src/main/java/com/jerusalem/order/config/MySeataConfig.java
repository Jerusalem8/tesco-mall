package com.jerusalem.order.config;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;


/****
 * @Author: jerusalem
 * @Description: MySeataConfig
 * 分布式事务Seata相关配置
 * @Date 2020/11/11 20:25
 *****/
@Configuration
public class MySeataConfig {

    @Autowired
    DataSourceProperties dataSourceProperties;

    /***
     * 数据源相关配置
     * @param dataSourceProperties
     * @return
     */
    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties){
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (StringUtils.hasText(dataSourceProperties.getName())){
            dataSource.setPoolName(dataSourceProperties.getName());
        }
        return new DataSourceProxy(dataSource);
    }
}
