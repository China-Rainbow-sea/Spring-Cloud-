package com.rainbowsea.springcloud.config;


import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {


    @Bean
    public Logger.Level loggerLever() {
        // /日志级别指定为 FULL
        return Logger.Level.FULL;
    }
}
