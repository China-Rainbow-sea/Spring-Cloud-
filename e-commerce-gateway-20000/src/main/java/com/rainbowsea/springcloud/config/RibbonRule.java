//package com.rainbowsea.springcloud.config;
//
//
//import com.netflix.loadbalancer.IRule;
//import com.netflix.loadbalancer.RandomRule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RibbonRule {
//
//    // 配置注入自己的负载均衡算法
//
//    @Bean
//    public IRule myRibbonRule() {
//        // 这里老师返回的是 RandomRule ，当然小伙伴也可以自己指定
//        return new RandomRule();
//    }
//}
