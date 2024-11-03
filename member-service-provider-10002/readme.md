演示 80 客户端的的：跳转别名为: MEMBER-SERVICE-PROVIDER
注意需要加上：才能负载均衡

```
java
   

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




```

效果：
**交替访问 member服务说明：** 
1. 注解@LoadBalanced(在RestTemplate 配置类的@Bean方法上加上该 @LoadBalanced注解的)
底层是 Ribbon 支持算法
2. Ribbon 和 Eureka 整合后 consumer 直接调用服务而不用再关心地址和端口号，且该服务还有负载功能

# 获取 Eureka Server 服务注册信息-DiscoveryClient

**这里我们以服务消费方 80端口，去获取 Eureka Server 的服务注册信息为例**
```
java

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

```

获取到的结果：
```
json
{"services":["member-service-provider","member-service-consumer-80"],"order":0}
```

##  注意事项
在引入 DiscoveryClient 时，不要引入错误的包
正确的包: import org.springframework.cloud.client.discovery.DiscoveryClient; 
错误的包: import com.netflix.discovery.DiscoveryClient;
演示的是在服务消费方使用 DiscoveryClient 来完成服务发现，同样，在服务提供 方/模块也 OK

# Eureka 后续说明
Eureka 停更说明: https://github.com/Netflix/eureka/wiki
对于一些早期使用 Eureka 项目，掌握老师讲解技术基本可以应付了(这也是老师为什么还要讲Eureka的原因)
3. 目前替代Eureka做服务注册和发现均衡负载的最佳组件是
,Nacos ,  后面老师会重点讲解
Spring Cloud Alibaba
4. 虽然 Eureka 停更，目前用的不多，但是它的服务注册和发现均衡负载的思想是优先
的，有了Eureka的基础，我们学习Spring Cloud Alibaba Nacos会轻松很多