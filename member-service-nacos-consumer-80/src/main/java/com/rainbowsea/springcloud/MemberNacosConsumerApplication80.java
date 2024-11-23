package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 表示引入 Nacos 注册中心，启动 nacos 发现注解
public class MemberNacosConsumerApplication80 {

    public static void main(String[] args) {
        SpringApplication.run(MemberNacosConsumerApplication80.class, args);
    }
}
