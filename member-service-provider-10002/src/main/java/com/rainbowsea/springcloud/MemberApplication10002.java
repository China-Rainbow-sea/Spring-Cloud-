package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


//@EnableEurekaClient 表示将该程序/项目/模块 标识为 eureka-client 端
@EnableEurekaClient
@SpringBootApplication
public class MemberApplication10002 {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication10002.class, args);
    }
}
