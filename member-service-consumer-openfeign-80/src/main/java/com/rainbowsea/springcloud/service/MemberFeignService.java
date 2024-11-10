package com.rainbowsea.springcloud.service;


import com.rainbowsea.springcloud.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
//这里 MEMBER-SERVICE-PROVIDER 就是 Eureka Server 服务提供方注册的名称
@FeignClient(value = "MEMBER-SERVICE-PROVIDER")  // 这个 value 值就是,对应我们想要访问的 provider(提供服务/服务集群)的 name 名称
public interface MemberFeignService {



    // 注意：这里定义方法-就是远程调用的接口，建议复制过来

    /*
    1.远程调用的方式是get
    2.远程调用的 url http://MEMBER-SERVICE-PROVIDER(注册到服务当中的别名)/member/get/{id}
    3.MEMBER-SERVICE-PROVIDER 就是服务提供方法 Eureka Server 注册的服务
    4. openfeign 会根据负载均衡决定调用 10000/10002 -默认是轮询
    5.因为openfeign 好处是支持了 spring mvc 注解 + 接口解构
    6. 想要使用 OPFeign 需要在对应场景启动器的位置配置上： @EnableFeignClients  // 启动 OpenFeignClient

     */
    @GetMapping("/member/get/{id}")
    Result getMemberById(@PathVariable("id") Long id);


}
