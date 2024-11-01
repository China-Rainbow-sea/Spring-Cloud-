package com.rainbowsea.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration  // 标注配置类
public class CustomizationBean {

    // 交给 spring ioc 容器管理
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}

