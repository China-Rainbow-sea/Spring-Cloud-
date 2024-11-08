package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// 如果错误: 加入排除 DataSourceAutoConfiguration 自动配置
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableEurekaClient 表示将项目/模块/程序 标注作为 Eureka Client
@EnableEurekaClient
@SpringBootApplication
@EnableDiscoveryClient // 启用服务发现
public class MemberConsumerApplication80 {
    public static void main(String[] args) {
        SpringApplication.run(MemberConsumerApplication80.class, args);
    }
}
