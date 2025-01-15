package com.rainbowsea.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// 注意: 需要取消数据源的自动配置
// 而是使用 seata 代理数据源，因为你是让 seata 进行事务上的管理的，那么你就需要将数据交给seata
// 进行管理，才能实现事务上的管理
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //  排除自动配置的数据源
@EnableDiscoveryClient
@EnableFeignClients  // 启动 opFeign 客户端
public class SeataStorageMicroServiceApplication10010 {
    public static void main(String[] args) {
        SpringApplication.run(SeataStorageMicroServiceApplication10010.class, args);
    }
}
