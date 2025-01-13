package com.rainbowsea.springcloud.controller;


import com.rainbowsea.springcloud.entity.Member;
import com.rainbowsea.springcloud.entity.Result;
import com.rainbowsea.springcloud.service.MemberOpenFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class MemberNacosConsumerController {

    private static final String MEMBER_SERVICE_NACOS_PROVIDER_URL = "http://member-service-nacos-provider"; //
    // 后面这里地方会修改成提供服务模块的注册别名，可以在 nacos 当中查看到，对应的服务名


    // 装配 RestTemplate bean/对象
    @Resource
    private RestTemplate restTemplate;
    @Resource // 装配到 Spring ioc 容器当中
    private DiscoveryClient discoveryClient;


    // 装配 MemberOpenFeignService
    @Resource
    private MemberOpenFeignService memberOpenFeignService;


    // 编写方法通过openfeign 实现远程调用
    @GetMapping("/member/openfeign/consumer/get/{id}")
    public Result<Member> getMemberOpenfeignById(@PathVariable("id") Long id) {
        // 这里我们使用openfeign 接口方式远程调用
        log.info("调用方式是 openfeign...");
        return memberOpenFeignService.getMemberById(id);
    }


    @PostMapping("/member/nacos/consumer/save")
    public Result<Member> save(Member member) {

        log.info("member-service-consumer-80 save member={}", member);
        return restTemplate.postForObject
                (MEMBER_SERVICE_NACOS_PROVIDER_URL + "/member/save", member, Result.class);

    }


    // 注意：有两个是这个包下的 ： org.springframework.cloud.client.discovery.DiscoveryClient

    /**
     * 方法/接口，调用服务接口，返回 member 对象信息
     *
     * @param id
     * @return
     */
    @GetMapping("/member/nacos/consumer/get/{id}")
    public Result<Member> getMemberById(@PathVariable("id") Long id) {

        // 这里就用两个参数
        // 第一个参数，因为是查询，所以这里我们直接字符串拼接上去
        // 这里通过
        return restTemplate.getForObject(MEMBER_SERVICE_NACOS_PROVIDER_URL + "/member/get/" + id, Result.class);
    }

    // 注意：想要获取到服务端信息，还需要在SpringBoot 启动场景上加上：@EnableDiscoveryClient // 启用服务发现 注解才行
    @GetMapping("/member/consumer/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();

        // 遍历 services
        for (String service : services) {
            log.info("服务名==={}", service);
            // 再根据服务名获取更加详细的信息
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                log.info("id={},host={},uri={}", instance.getServiceId(), instance.getHost(), instance.getUri());
            }
        }


        return this.discoveryClient;
    }
}
