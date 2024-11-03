package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer // @EnableEurekaServer 表示该项目/该模块/该程序作为 Eureka Server 角色进行运行
@SpringBootApplication
public class EurekaApplication9001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication9001.class, args);
    }
}
