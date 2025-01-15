package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication( exclude = DataSourceAutoConfiguration.class) //
// 排除自动配置数据源，让seata 代理配置
public class SeataOrderMicroServiceApplication10008 {
    public static void main(String[] args) {
        SpringApplication.run(SeataOrderMicroServiceApplication10008.class, args);
    }
}
