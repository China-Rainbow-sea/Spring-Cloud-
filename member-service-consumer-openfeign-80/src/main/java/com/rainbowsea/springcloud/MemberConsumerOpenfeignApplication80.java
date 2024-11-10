package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient  // 表示作为 Eureka Client 角色
@EnableFeignClients  // 启动 OpenFeignClient
public class MemberConsumerOpenfeignApplication80 {


    public static void main(String[] args) {

        SpringApplication.run(MemberConsumerOpenfeignApplication80.class, args);
    }
}
