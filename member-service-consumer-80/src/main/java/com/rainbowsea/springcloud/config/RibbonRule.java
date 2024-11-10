package com.rainbowsea.springcloud.config;


import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonRule {

    @Bean // 配置注入自己的负载均衡算法
    public IRule myRibbonRule() {
        // 这里老师返回的是 RandomRule，大家也可以指定其他的内容。
        //return new WeightedResponseTimeRule();
        return new RandomRule();  // 随机算法

    }
}
