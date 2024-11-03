package com.rainbowsea.springcloud.controller;

import com.rainbowsea.springcloud.entity.Member;
import com.rainbowsea.springcloud.entity.Result;
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
public class MemberConsumerController {


    // 定义 MEMBER_SERVICE_PROVIDER_URL 这是一个基础 url地址
    // 使用 shift+ctrl+u 进行字母大小写的切换
    //private static final String MEMBER_SERVICE_PROVIDER_URL = "http://localhost:10000";
    // 负载均衡统一别名，修改为提供服务模块的注册别名: 不需要配置指明端口号，因为是一个负载集群了。不是特指某个了Service 服务来处理了
    /*
    server:
  port: 10000
spring:
  application:
    name: member-service-provider # 配置应用的名称
     */
    /*
    说明:
    1. MEMBER-SERVICE-PROVIDER 就是服务提供方【集群】，注册到Eureka Server 的名称
    2. 也就是服务提供方【集群】对外暴露的名称为 MEMBER-SERVICE-PROVIDER
    3. MEMBER-SERVICE-PROVIDER 目前有两个 	Availability Zones 	UP (2) - localhost:member-service-provider:10000 , localhost:member-service-provider:10002
      还有一个 member-service-provider:10002
      需要增加一个注解 @LoadBalanced 赋予 RestTemplate 才能有负载均衡的能力，也就是会根据你的负载均衡
      来选择某个服务去访问，默认是“轮询算法”，当然我们也可以自己配置负载均衡算法
    4. 在对应的 RestTemplate 配置类上的，@Bean注解下面加上 @LoadBalanced 注解。
     */
    private static final String MEMBER_SERVICE_PROVIDER_URL = "http://MEMBER-SERVICE-PROVIDER";


    // 装配 RestTemplate bean/对象
    @Resource
    private RestTemplate restTemplate;


    @PostMapping("/member/consumer/save")
    public Result<Member> save(Member member) {
        // 1.第1个参数: 就是请求的完整的url:MEMBER_SERVICE_PROVIDER_URL + "/member/save" => http://localhost:10000/member/save
        // 表示你向将该请求，发送给那个微服务处理，注意无论是返回值，还是参数， @PostMapping() 请求方式都要一一对应上对应处理的微服务上的内容
        //2. 第2个参数: member : 就是通过 restTemplate 发出的 post 请求携带数据或者对象
        //3. 第3个参数: Result.class ，微服务当中的对应处理的方法的，返回值，也就是返回对象类型
        // 注意：坑点
        // 这里通过 restTemplate 调用服务模块的接口，就是一个远程调用
        //
        log.info("member-service-consumer-80 save member={}", member);
        return restTemplate.postForObject
                (MEMBER_SERVICE_PROVIDER_URL + "/member/save", member, Result.class);

    }

    /*
    1.与
     @PostMapping("/member/save")
    public Result save(Member member) {
        int affected = memberService.save(member);
        if (affected > 0) { // 说明添加成功
            return Result.success("添加会员成功", affected);
        } else {
            return Result.error("401", "添加会员失败");
        }
    }
     */


    /**
     * 方法/接口，调用服务接口，返回 member 对象信息
     *
     * @param id
     * @return
     */
    @GetMapping("/member/consumer/get/{id}")
    public Result<Member> getMemberById(@PathVariable("id") Long id) {

        // 这里就用两个参数
        // 第一个参数，因为是查询，所以这里我们直接字符串拼接上去
        // 这里通过
        return restTemplate.getForObject(MEMBER_SERVICE_PROVIDER_URL + "/member/get/" + id, Result.class);
    }

    /*
    保持一致：
      * 提交方式
      * 返回类型
      * 参数
      * 路径映射要对应上
     @GetMapping("/member/get/{id}")
    public Result getMemberById(@PathVariable("id") Long id) {
        Member member = memberService.queryMemberById(id);

        // 使用 Result 把查询到的结果返回
        if (member != null) {
            return Result.success("查询会员成功", member);
        } else {
            return Result.error("402", "ID" + id + "不存在");
        }
    }

     */


    // 注意：有两个是这个包下的 ： org.springframework.cloud.client.discovery.DiscoveryClient

    @Resource // 装配到 Spring ioc 容器当中
    private DiscoveryClient discoveryClient;


    // 注意：想要获取到服务端信息，还需要在SpringBoot 启动场景上加上：@EnableDiscoveryClient // 启用服务发现 注解才行
    @GetMapping("/member/consumer/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();

        // 遍历 services
        for (String service : services) {
            log.info("服务名==={}",service);
            // 再根据服务名获取更加详细的信息
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                log.info("id={},host={},uri={}",instance.getServiceId(),instance.getHost(),instance.getUri());
            }
        }


        return this.discoveryClient;
    }
}
