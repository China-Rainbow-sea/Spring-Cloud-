package com.rainbowsea.springcloud.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


/**
 * 常规配置: MyBatis 和 dao 关联
 */
@Configuration
@MapperScan({"com.rainbowsea.springcloud.dao"})
public class MyBatisConfig {
}
