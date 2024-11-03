package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  //该注解标注，当前的项目/模块/程序作为 Eureka Server 角色
public class EurekaApplication9002 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication9002.class, args);
    }
}
