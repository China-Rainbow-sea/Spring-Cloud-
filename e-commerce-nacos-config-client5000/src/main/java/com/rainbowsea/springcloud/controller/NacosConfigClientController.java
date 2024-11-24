package com.rainbowsea.springcloud.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope  // 实现配置自动更新： Spring Cloud 原生注解,实现了配置数据的自动刷新
public class NacosConfigClientController {

    /*
    特别说明:
    1. client 会拉取 nacos server 的 e-commerce-nacos-config-client-dev.yaml
    - config:
        ip: "122.22.22.22"
        name: "RainbowSea"
    2. @Value("${config.ip}") 会将 config.ip 赋给 configIP
    3. 这里的@"${config.ip}" 不能乱写，要有依据,如果有一点不一致的都会,报错，无法运行的
    是在: org.springframework.beans.factory.annotation.Value 包下的
     */
    //@Value("${config:ip}")  // 使用 : 也是可以的
    @Value("${config.ip}")  // 使用 : 也是可以的
    private String configIp;

    @Value("${config.name}")
    private String configName;


    @GetMapping("/nacos/config/ip")
    public String getConfigIp() {
        return configIp;
    }

    @GetMapping("/nacos/config/name")
    public String getConfigName() {
        return configName;
    }
}
