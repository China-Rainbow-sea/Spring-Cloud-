package com.rainbowsea.springcloud.config;


import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 1.这里很重要: 配置数据源的代理是 seata 也就是使用 seata 代理数据源
 * 2. DataSourceProxy 是引入的 io.seata.rm.datasource
 */
@Configuration
public class DataSourceProxyConfig {

    @Value("${mybatis.mapperLocations}")  // 读取applicaton.yaml 配置文件当中的信息，通过前缀名
    private String mapperLocations;

    //配置druidDataSource
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        /*DruidDataSource druidDataSource = new DruidDataSource();
        System.out.println("druidDataSource.hashcoder = " + druidDataSource.hashCode());
        return druidDataSource;*/
        return new DruidDataSource();
    }

    //配置DataSourceProxy- 使用seata代理数据源
    @Bean
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
        // 注意是: io.seata.rm.datasource.DataSourceProxy; 包下的内容
        /*System.out.println("dataSourceProxy()中的 datasour.hashcode = " + dataSource.hashCode());
        DataSourceProxy dataSourceProxy = new DataSourceProxy(dataSource);
        System.out.println("dataSourceProxy()中的dataSourceProxy。hashcode= " + dataSourceProxy.hashCode());
        return dataSourceProxy;*/
        return new DataSourceProxy(dataSource);
    }

    //配置SqlSessionFactory-常规写法
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSourceProxy dataSourceProxy)
            throws Exception {
        //System.out.println("sqlSessionFactoryBean 中的 dataSourceProxy.hashcode = " + dataSourceProxy.hashCode());
        SqlSessionFactoryBean sqlSessionFactoryBean =
                new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSourceProxy);
        sqlSessionFactoryBean.setMapperLocations
                (new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        sqlSessionFactoryBean.setTransactionFactory
                (new SpringManagedTransactionFactory());
        return sqlSessionFactoryBean.getObject();
    }
}
