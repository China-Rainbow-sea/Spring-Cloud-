package com.rainbowsea.springcloud.service;


import com.rainbowsea.springcloud.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "member-service-nacos-provider", fallback = MemberFeignFallbackService.class)
// 指定为 FeignClient 客户端
// .value 指明提供 provider
// 服务的集群
// (注意该值:是nacos里面查询到的)
// fallback 处理 fallback 负责 Java 异常/业务异常,包含服务之间调用上的延时异常之类的.
public interface MemberOpenFeignService {


    /**
     * 解读
     *
     * @param id
     * @return 1. 远程调用方式是 get
     * 2. 远程调用的url 为 http://member-service-nacos-prvoider/member/get/{id}
     * 3. member-service-nacos-prvoider 是 nacos 注册中心服务名
     * 4. openfeign 会根据负载均衡算法来决定调用的是: 10004/10006,默认是轮询算法
     * 5. openfeign 是通过接口方式调用服务
     */
    @GetMapping("/member/get/{id}")
    Result getMemberById(@PathVariable("id") Long id);
}
