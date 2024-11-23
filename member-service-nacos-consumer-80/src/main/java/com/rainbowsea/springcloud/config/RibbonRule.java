//package com.rainbowsea.springcloud.config;
//
//
//import com.netflix.loadbalancer.IRule;
//import com.netflix.loadbalancer.RandomRule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///*
//特别说明: Nacos 不需要额外的在场景启动器上指明该使用的“负载均衡”算法。
//因为 Nacos 本身就包含了 这些，直接使用注解即可，直接使用配置类上加上注解就行
// */
//@Configuration
//public class RibbonRule {
//    //配置注入自己的负载均衡算法
//    @Bean
//    public IRule myRibbonRule() {
////这里老师返回的是 RandomRule,当然小伙伴也可以自己指定
//        return new RandomRule();
//
//    }
//}
