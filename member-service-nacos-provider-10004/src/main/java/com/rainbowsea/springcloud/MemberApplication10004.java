package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


//@EnableDiscoveryClient  标注引入的是 Nacos 发现注解
@EnableDiscoveryClient
@SpringBootApplication
public class MemberApplication10004 {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication10004.class, args);
    }
}
