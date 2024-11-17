package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient  // 标注为 Eureka Client 角色
public class GateWayApplication20000 {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication20000.class, args);
    }
}
