package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 启用 Nacos 注册中心(配置中心)
public class NacosConfigClientApplication5000 {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigClientApplication5000.class, args);
    }
}
