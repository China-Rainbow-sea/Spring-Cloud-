# dependencyManagement 细节说明
1. Maven 使用  <dependencyManagement> 元素来提供了一种管理依赖版本号的方式，
通常在项目 packaging为POM，中使用  <dependencyManagement> 元素
2. 使用 pom.xml 中的  <dependencyManagement> 标签元素能让所有在子项目中引用一个依赖，
Maven 会沿着父子层次向上走，直到找到一个拥有  <dependencyManagement> 标签元素的项目，
然后它就会使用这个  <dependencyManagement> 元素中指定的版本号。
3.好处: 如果有多个项目都引用同一样依赖，则可以避免在每个使用的子项目里都声明一个版本号，
当升级或切换到另一个版本时，只需要在顶层父容器更新，而不需要分别在子项目的修改；另外
如果某个子项目需要另外一个版本，只需要声明 version 就可以。
4.  <dependencyManagement> 里只是声明依赖，并不实现引入，因此子项目需要显示的声明需要的依赖
5. 如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且
没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父 pom
6. 如果子项目中指定了版本号，那么会使用子项目中指定的jar版本


# 服务注册
# Eureka 的说明

1. 从上图可以看出，目前主流的服务注册/发现的组件是 Nacos，但是Eureka 作为一个老牌经典的服务
注册/发现技术还是有必要血一下：
**原因：** 
* 一些早期的分布式 微服务项目使用的是 Eureka ，大家工作中，完全有可能遇到这种情况。
* 后期的服务注册/发现组件/技术，都参考了 Eureka 的设计和理念，学习了 Eureka 后，我们上手
Nacos 容易很多，而且理解的更深刻。

> 当前项目架构分析:
> 调用会员中心(consumer) ————————> 会员中心-提供服务(provider)

**当前项目问题分析：**
1. 在企业项目中，服务消费访问请求会存在高并发
2. 如果只有一个会议中心-提供服务，可用性就比较差了
3. 所以，会议中心提供服务往往是一个集群，也就是说会有多个会议中心-提供服务微服务模块
4. 那么这个时候，就存在一个问题就是服务消费方，怎么去发现可以使用的服务(哪些服务又不可以使用)
5. 当服务消费方，发现了可以使用的服务后(可能是多个，又存在一个问题就是到底调用 A服务，
还是调用B服务，这就引出了服务注册和负载均衡的问题)
6. Eureka 就可以解决上述问题



# 引入  Eureka项目架构刨析
1. 会员中心——> 提供服务的，在项目中，会做成**集群** ，提供高可用
2. Eureka Server 有必要的话，也可以做成**集群** 。
3. Eureka 包含两个组件: **Eureka Server** 和 **Eureka Client**
4. Eureka Server 提供注册服务，各个微服务节点童工配置启动后，会在 EurekaServer 中进行注册，
这样Eureka Server 中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观看到。
5. Eureka Client 通过注册中心进行访问，是一个Java客户端，用于简化 Eureka Server 的交互，
客户端同时也具备一个内置的，使用轮询(round-robin) 负载算法的负载均衡器。在应用启动后，
将会向 Eureka Server 发送心跳(默认周期为 30秒)。如果 Eureka Server 在多个心跳周期
内没有接收到某个节点的心跳，Eureka Server 将会从服务注册表中把这个服务节点移除(默认90秒)

* Eureka 采用了CS[client-server-java] 基础我们讲过一个多人聊天项目。的设计架构，
Eureka Server 作为服务注册的服务器，它是服务注册中心
* 系统中的其它微服务，使用Eureka 的客户端连接到 Eureka Server 并维持心跳连接，
通过 Eureka Server 来监控系统中各个微服务是否正常运行
* 在服务注册与发现中，有一个注册中心，当服务启动的时候，会把当前自己服务器的信息，比如
服务地址通讯地址等以别名方式注册到注册中心上。
* 服务消费者或者服务提供者，以服务别名的方式去注册中心上获取到实际的服务提供
者通讯地址，然后，通过 RPC 调用服务


# Eureka 的自我保护模式
## 自我保护机制/模式说明

* 默认情况下EurekaClient 定时向 EurekaServer 端发送 **心跳包** 
* 如果 Eureka 在 server 端一定时间内(默认90秒) 没有收到 EurekaClient 发送心跳包，便会
直接从服务注册表中剔除该服务
* 如果Eureka 开启了**“自我保护机制/模式”** ,那么在短时间(90秒中)内丢失了大量的服务实例心跳。
这时候Eureka Server 会开启自我保护机制，不会剔除该服务(该现象可能出现在如果网络不通或者
阻塞)因为客户端还能正常发送心跳，只是网络延迟问题，而保护机制是为了解决此问题而产生的

2. 自我保护是属于**CAP里面的AP分支** ，保证高可用和分区容错性
3. 自我保护模式是一种应对网络异常的安全保护措施，它的**架构哲学是宁可同时保留所有微服务(健康
的微服务和不健康的微服务都会保留)也不盲目注销任何健康的微服务**。使用自我保护模式，
可以让 Eureka 集群更加的健壮，稳定，参考：
https://blog.csdn.net/wangliangluang/article/details/120626014
> * 一致性: 所有节点在同一时间的数据完全一致
> * 可用性：服务在正常时间内一直可用
> * 分区容错性：系统在遇到节点或者网络分区故障的时候，仍然能够对外满足可用性或一致性的服务

# 禁用自我保护模式(生产环境中，一般不禁用)
# 测试完毕后，别忘了恢复原状，启用自我保护


# 为什么需要集群 Eureka Server
说明:
1. 微服务 RPC 远程服务调用最核心的是实习高可用
2. 如果注册中心只有1个，它出故障，会导致整个服务环境不可用
3. 解决办法: 搭建 Eureka 注册中心集群，实习 **负载均衡 + 故障容错** 


# 注意事项和细节
1. 因为 member-service-provider-10000 和 member-service-provider-10002 作为一个
集群提供服务，因此需要将spring.application.name 进行统一
2. 这样消费方通过统一的别名进行负载均衡调用
**演示：** 
将 10000 和 10002 两个提供方服务的，别名修改为一样的 member-service-provider
```
xml
server:
  port: 10000
spring:
  application:
    name: member-service-provider # 配置应用的名称
```
运行，再从 9001 和 9002  Service 服务中心查看效果
> MEMBER-SERVICE-PROVIDER	n/a (2)	(2)	UP (2) - localhost:member-service-provider:10000 , localhost:member-service-provider:10002

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


# Ribbon 是什么
1.Spring Cloud Ribbon 是基于Netflix Ribbon 实现的一套客户端，负载均衡的工具
2.Ribbon 主要功能是提供客户端负载均衡算法和服务调用
3.Ribbon 客户端组件提供一系列完善的配置项如“连接超时，重试”
4.Ribbon 会基于某种规则（如简单轮询，随机连接等）去连接指定服务
5.程序员很容易是由 Ribbon 的负载均衡算法实现负载均衡
6.一句话: Ribbon: 负载均衡 + RestTemplate 调用
Ribbon 的官网地址:  https://github.com/Netflix/ribbon

## Ribbon 进入维护状态

Ribbon 目前进入维护模式，未来替换方案是 Spring Cloud LoadBalancer

# LB 分类
1. 集中式: LB
* 即在服务的消费方和提供方之间使用独立的LB设施(可以是硬件，如F5，也可以是软件，如 Nginx)
由该设施负责把访问请求通过某种策略转发至服务的提供方
* LB(Load Balance 负载均衡)
2. 进程内LB
* 将 LB逻辑集成到消费方，消费方服务注册中心获知有哪些地址可用，然后再从这些地址中选择
一个合适的服务地址
* Ribbon 就属于进程内LB，它只是一个类库，集成于消费方进程，消费方通过它来获取到服务提供方的地址。

# Ribbon 常见负载算法
> Ribbon 机制
> 1. 先选择 EurekaServer ，它的优先选择在同一个区域内负载较少的 server
> 2. 再根据用户指定的策略，在从server 取到的服务注册列表中选择一个地址
> 3. Ribbon 提供了多种策略，比如：轮询，随机和根据响应时间加权。
* RandomRule 随机选择一个Server，在index 上随机，选择index对应位置的 server
* RoundRobinRule 轮询index，选择index 对应位置的server


# OpenFeign 是什么
1. OpenFeign 是个声明式 WebService 客户端，使用OpenFeign 让编写Web Service客户端更简单
2. 它的使用方法是定义一个服务接口然后在上面添加注解
3. OpenFeign 也支持可插拔式的编码器和解码器
4. Spring Cloud 对OpenFeign 进行了封装使其支持了Spring MVC 标注注解和HttpMessageConverters
5. OpenFeign 可以与 Eureka 和 Ribbon 组合使用以支持负载均衡
官网地址： https://github.com/spring-cloud/spring-cloud-openfeign
**简单的说：就是一个远程服务登录访问的，转发的一个组件，可以实现Server 集群之间的相互注册通信** 


# Feign 和 OpenFeign 区别
Feign 
* Feign 是Spring Cloud 组件中的一个轻量级 RESTful的HTTP服务客户端
* Feign 内置了Ribbon ，用来做客户端负载均衡，去调用服务注册中心的服务
* Feign 的使用方式是: 使用Feign的注解定义接口，调用服务注册中心的服务。
* Feign 支持的注解和用法参考官方文档: https://github.com/OpenFeign/feign
* Feign 本身**不支持Spring MVC的注解** ，它有一套自己的注解
* 引入依赖:
```
xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

```
**OpenFeign** 
* OpenFeign 是 Spring Cloud 在 **Feign的基础上支持了Spring MVC 的注解，如 @RequestMapping等等
* OpenFeign 的 @FeignClient 可以解析Spring MVC 的 @RequestMapping注解下的接口
* OpenFeign 通过动态代理的方式产生实现类，实现类中做负载均衡并调用其他服务。
引入依赖:
```
xml
   <!--  引入 openfeign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

```
**精简一句话: OpenFeign 就是在 Feign基础上做了加强，有些程序员为了方便，说Feign就是OpenFeign

# 补充: spring-boot-starter-actuator  是spring boot 程序的监控系统,可以实现健康检查
```
xml

        <!--1. starter-actuator 是spring boot 程序的监控系统,可以实现健康检查,info 信息等
         2. 访问http://localhost:10000/actuator 可以看到相关链接,还可以做相关配置-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

## Openfeign 的使用的特点：微服务接口+@FeignClient,使用接口进行解耦(简单的说:就是使用接口调用对应 provider 提供服务的集群)

@FeignClient(value = "MEMBER-SERVICE-PROVIDER")  // 这个 value 值就是,对应我们想要访问的 provider(提供服务/服务集群)的 name 名称
所以：注意不要将提供注册的名称，写错了
* 接口方法上的: value 是不能乱写的，远程调用的url为 : 对应你想要调用哪个 provider 名称的别名/名称 http://MEMBER-SERVICE-PROVIDER/member/get/{id}
* 接口上的方法，也必须与调用的 provider 当中的方法保持一致: 
```
java

@GetMapping(value = "/member/get/{id}")
public Result<Member> getMembertById(@PathVariable("id") Long id);

```

## 日志配置
1. 说明 Feign 提供了日志打印功能，可以通过配置来调整日志级别，从面对 Feign 接口的调用情况进行监控和输出
1. 日志级别:
> 1. NONE: 默认的，不显示任何日志
> 2. BASIC: 仅记录请求方式，URL,响应状态码及执行时间
> 3. HEADERS:除了 BASIC 中定义的信息之外，还有请求和响应的头信息
> 4. FULL: 除了HEADERS 中定义的信息之外，还有请求和响应的正文及元数据

> 常见的日志级别有 5 种，分别是 error、warn、info、debug、trace
  error：错误日志，指比较严重的错误，对正常业务有影响，需要运维配置监控的；
  warn：警告日志，一般的错误，对业务影响不大，但是需要开发关注；
  info：信息日志，记录排查问题的关键信息，如调用时间、出参入参等等；
  debug：用于开发 DEBUG 的，关键逻辑里面的运行时数据；
  trace：最详细的信息，一般这些信息只记录到日志文件中。

**测试：最后：别忘了，撤销测试日志配置**

## OpenFeign 超时
 在两个 10000/10002 添加上模拟超时，
```
java
/模拟超时，这里暂停 5 秒
try {
TimeUnit.SECONDS.sleep(5);
 } catch (Exception e) {
e.printStackTrace();
}

```

**测试效果:**
> 浏览器显示: Read timed out executing GET http://MEMBER-SERVICE-PROVIDER/member/get/1
> IDEA后端显示: java.net.SocketTimeoutException: Read timed out
>Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is feign.RetryableException: Read timed out executing GET http://MEMBER-SERVICE-PROVIDER/member/get/1] with root cause
>

配置超时时间: 在 application.yaml 当中配置
```
yaml


```



# Gateway 是什么
1. Gateway 是Spring 生态系统之上构建的 API 网关服务，基于 Spring ,Spring Boot 和 Project Reactor 等技术。
2. GateWay 旨在提供一种简单而有效的方式来对 API 进行路由，以及提供一些强大的过滤器功能，例如：熔断，限流，重试等
官方地址: https://spring.io/projects/spring-cloud-gateway
https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/how-it-works.html

## Gateway 核心功能:
> 1. 鉴权，2.流量控制，3.熔断, 4.日志监控, 5. 反向代理

## Gateway 和 Zuul 区别:
1.Spring Cloud Gateway 作为 Spring Cloud 生态系统中的网关，目标是替代Zuul
2.SpringCloud  Gateway 是基于 Spring WebFlux 框架实现的
3.Spring WebFlux 框架底层则使用了高性能的 Reactor模式通信框架Netty，提升了网关性能


## GateWay 特性
Spring Cloud Gate 基于 Spring FrameWork(支持Spring WebFlux),Project Reactor 和 Spring Boot 进行构建，具有如下特性:
> * 动态路由
> * 可以对路由指定 Predicate(断言)和Filter(过滤器)
> * 集成 Hystric 的断路器功能
> * 集成 Spring Cloud 服务发现功能
> * 请求限流功能
> * 支持路径重写

## Route(路由)
1. 一句话: **路由是构建网关的基本模块，它由 ID,目标 URI,一系列的断言和过滤器组成，如果断言为 true 则匹配该路由**
2. 简单举例: 比如配置路径，-Path=/member/get/** #断言，路径匹配的进行路由转发，如果Http的请求路径不匹配，则
不进行路由转发。

## Filter (过滤)
1. 一句话: 使用过滤器，可以在**请求被路由前或者之后** 对请求进行处理。
2. 你可以理解成,在对Http请求断言匹配成功后，可以通过网关的过滤机制，对 Http请求处理
3.简单举例:
```
yaml
filters:
 -AddRequestParamter=color,blue # 过滤器在匹配的请求头加上一对请求头，名称为color 值为blue
,比如原来的http请求是:http://localhost:10000/member/get/1 == 过滤器处理
=>http://localhost:10000/member/get/1?color=blue
```

## 梳理流程(how to work)
1. 客户端向 Spring Cloud Gateway 发出请求，然后在 Gateway Handler Mapping 中找到与请求相匹配
的路由，将其发送到 GateWay Web Handler
2. Handler 再通过指定的过滤器链来将请求发送到我们实际的服务执行业务逻辑，然后返回。
3. 过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前("pre") 或之后("post") 执行业务
4. Filter 在“pre” 类型的过滤器可以做参数校验，权限校验，流量监控，日志输出，协议转换等
5. 在 "post" 类型的过滤器中可以做响应内容，响应头的修改，日志的输出,流量监控等有着非常非常重要的作用.
一句话说: 路由转发+执行过滤器链.
1.  通过网关暴露的接口，实现调用真正的服务 
2.  网关本身也是一个微服务模块

> 特别提醒:
>    1. 特别提示: 不要引入Spring-boot-starter-web 和 spring-boot-starter-actuator
>    2. 因为 gateway 是一个服务网关，不需要 web... 


## 路由配置
两种方式：
1.第一种方式: 在 application.yaml当中配置
2.第二种方式: 编写配置类，进行一个类注入的方式进行配置(了解)


## 实现功能: 
> 当客户端通过网关服务调用接口，路由能够动态的切换到不同的服务接口，service provider 10000/2
> 负载均衡(轮询)
**在 application.yaml 当值配置动态的切换不同的服务接口,这时候需要将配置类方式的类-注释掉防止冲突**
```
yaml
# 1. lb: 协议名, member-service-provider 注册到eureka server服务名(小写)
          # 2. 默认情况下，负载均衡算法是轮询
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name

```
同样可以自定义配置上，负载均衡算法（通过配置类的方式配置）
```
java
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


```

## Predicate/断言
> 一句话: Predicate 就是一组匹配规则，当请求匹配成功,就执行对应的Route,匹配失败,放弃处理/转发
> Route Predicate Factories
https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/request-predicates-factories.html
官网解读:
1. Spring Cloud Gateway 包括许多内置 Route Predicate 工厂,所有这些Predicate都与Http请求的不同
属性匹配,可以组合使用
2. Spring Cloud Gateway 创建 Route 对象时,使用 RoutePredicateFactory 创建 Predicate 对象,
Predicate 对象可以赋值给Route
3. 所有这些谓词 **都匹配HTTP请求的不同属性** 多种谓词工厂可以组合。

### 演示:
只有: 2022-11-18 12:35:50之后的请求才进行匹配/转发，不满足条件的,不处理
如下Java程序，获取到对应 时间格式的要求
```
java
package com.rianbowsea.springcloud.test;

import java.time.ZonedDateTime;

public class T2 {
    public static void main(String[] args) {
        ZonedDateTime now = ZonedDateTime.now();
        // 2024-11-17T23:48:50.519+08:00[Asia/Shanghai]
        System.out.println(now);
    }
}


```

```
yaml
spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**
            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间
        
```

**只有: 2022-11-18 12:35:50之前的请求才进行匹配/转发，不满足条件的,不处理**
```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: before_route
        uri: https://example.org
        predicates:
        - Before=2017-01-20T17:42:47.789-07:00[America/Denver]
```
```
yaml
spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
          # gateway 最终访问的url是 url=url+Path
          # 匹配后提供服务的路由地址:也可以是外网 http://www.baidu.com
          # 这里我们匹配为一个 localhost:10000 服务即可
          # 比如: 客户端/浏览器请求: url http://localhost:20000/member/get/1
          # 如果根据Path匹配成功，最终访问的url/转发url就是 url=http://localhost:10000/member/get/1
          # 如果匹配失败，则有 gateway 返回 404信息
          # 疑问: 这里老师配置的 url 是固定的，在当前这种情况其实可以不是有 Eureka Server,
          # 后面老师会使用灵活配置的方式，配置，就会使用 Eureka Server
          # 注意：当这里我们仅仅配置了一个url 的时候，就只会将信息转发到这个10000服务端，为我们处理业务，
          # 一个服务就没有(轮询)的负载均衡了
          #          uri: http://localhost:10000
          # 将查询服务，配置为动态的服务(轮询)负载均衡
          # 1. lb: 协议名, member-service-provider 注册到eureka server服务名(小写)
          # 2. 默认情况下，负载均衡算法是轮询
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**
#            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
        
```


**只有: 只有 2020-11-18 12:35:50  和  2022-11-18 12:35:50  之间的请求才进行匹配/转 发,  不满足该条件的，不处理**
```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: between_route
        uri: https://example.org
        predicates:
        - Between=2017-01-20T17:42:47.789-07:00[America/Denver], 2017-01-21T17:42:47.789-07:00[America/Denver]
```
```
yaml

spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
          # gateway 最终访问的url是 url=url+Path
          # 匹配后提供服务的路由地址:也可以是外网 http://www.baidu.com
          # 这里我们匹配为一个 localhost:10000 服务即可
          # 比如: 客户端/浏览器请求: url http://localhost:20000/member/get/1
          # 如果根据Path匹配成功，最终访问的url/转发url就是 url=http://localhost:10000/member/get/1
          # 如果匹配失败，则有 gateway 返回 404信息
          # 疑问: 这里老师配置的 url 是固定的，在当前这种情况其实可以不是有 Eureka Server,
          # 后面老师会使用灵活配置的方式，配置，就会使用 Eureka Server
          # 注意：当这里我们仅仅配置了一个url 的时候，就只会将信息转发到这个10000服务端，为我们处理业务，
          # 一个服务就没有(轮询)的负载均衡了
          #          uri: http://localhost:10000
          # 将查询服务，配置为动态的服务(轮询)负载均衡
          # 1. lb: 协议名, member-service-provider 注册到eureka server服务名(小写)
          # 2. 默认情况下，负载均衡算法是轮询
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**
            #            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            #            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
            - Between=2024-11-17T23:48:50.519+08:00[Asia/Shanghai], 2024-11-18T23:48:50.519+08:00[Asia/Shanghai]
        

```

**需求: 请求带有cookie键:user值:hsp才匹配/断言成功**

```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: cookie_route
        uri: https://example.org
        predicates:
        - Cookie=chocolate, ch.p
```

```
yaml

spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**
            #            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            #            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
            #            - Between=2024-11-17T23:48:50.519+08:00[Asia/Shanghai], 2024-11-18T23:48:50.519+08:00[Asia/Shanghai]
            - Cookie=user,hsp # 配置 Cookie 断言
```


**需求: 请求头 Header  有  X-Request-Id，  并且值 hello  才匹配/断言成功**

```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: header_route
        uri: https://example.org
        predicates:
        - Header=X-Request-Id, \d+
X-Request-Id  是 header 的名称, \d+  是一个正则表达式
```

```
yaml

spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**
            #            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            #            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
            #            - Between=2024-11-17T23:48:50.519+08:00[Asia/Shanghai], 2024-11-18T23:48:50.519+08:00[Asia/Shanghai]
            #            - Cookie=user,hsp # 配置 Cookie 断言
            - Header=X-Request-Id, hello
```


**需求: 请求 Host 是**.hspedu.**  才匹配/断言成功     比如  Host www.rianbowsea.com**
**Host 可以有多个,  使用逗号间隔**
```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: host_route
        uri: https://example.org
        predicates:
        - Host=**.somehost.org,**.anotherhost.org
```

```
yaml
spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**
            #            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            #            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
            #            - Between=2024-11-17T23:48:50.519+08:00[Asia/Shanghai], 2024-11-18T23:48:50.519+08:00[Asia/Shanghai]
            #            - Cookie=user,hsp # 配置 Cookie 断言
#            - Header=X-Request-Id, hello
            - Host=**.rainbowsea.com,**.anotherhost.org

```

**Method Route Predicate** 
**需求: 请求是 Get 方式才匹配/断言成功** 
请求方式可以有多个   使用逗号间隔
```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: method_route
        uri: https://example.org
        predicates:
        - Method=GET,POST
```
```
yaml

spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
          # gateway 最终访问的url是 url=url+Path
          # 匹配后提供服务的路由地址:也可以是外网 http://www.baidu.com
          # 这里我们匹配为一个 localhost:10000 服务即可
          # 比如: 客户端/浏览器请求: url http://localhost:20000/member/get/1
          # 如果根据Path匹配成功，最终访问的url/转发url就是 url=http://localhost:10000/member/get/1
          # 如果匹配失败，则有 gateway 返回 404信息
          # 疑问: 这里老师配置的 url 是固定的，在当前这种情况其实可以不是有 Eureka Server,
          # 后面老师会使用灵活配置的方式，配置，就会使用 Eureka Server
          # 注意：当这里我们仅仅配置了一个url 的时候，就只会将信息转发到这个10000服务端，为我们处理业务，
          # 一个服务就没有(轮询)的负载均衡了
          #          uri: http://localhost:10000
          # 将查询服务，配置为动态的服务(轮询)负载均衡
          # 1. lb: 协议名, member-service-provider 注册到eureka server服务名(小写)
          # 2. 默认情况下，负载均衡算法是轮询
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**,/member/save
            #            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            #            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
            #            - Between=2024-11-17T23:48:50.519+08:00[Asia/Shanghai], 2024-11-18T23:48:50.519+08:00[Asia/Shanghai]
            #            - Cookie=user,hsp # 配置 Cookie 断言
            #            - Header=X-Request-Id, hello
            #            - Host=**.rainbowsea.com,**.anotherhost.org
            #            -
            - Method=POST,GET

```


**Path Route Predicate: Path 可以有多个,  使用逗号间隔**

**Query Route Predicate 需求:  请求有参数 email ,并且满足电子邮件的基本格式,  才能匹配/断言成功** 

red 是参数名 gree. 是值 支持正则表达式.
```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: query_route
        uri: https://example.org
        predicates:
        - Query=red, gree.
```


```
yaml
spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
            - Path=/member/get/**
            #            - Path=/member/get/**,/member/save
            #            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            #            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
            #            - Between=2024-11-17T23:48:50.519+08:00[Asia/Shanghai], 2024-11-18T23:48:50.519+08:00[Asia/Shanghai]
            #            - Cookie=user,hsp # 配置 Cookie 断言
            #            - Header=X-Request-Id, hello
            #            - Host=**.rainbowsea.com,**.anotherhost.org
            #            -
            #            - Method=POST,GET
            - Query=email, [\w-]+@([a-zA-Z]+\.)+[a-zA-Z]+ # red是参数名;gree是值(支持正则表达式)
```
http://localhost:20000/member/get/1?email=hsp@sohu.com
http://localhost:20000/member/get/1?email=hspsohu.com

**需求分析/图解**
需求: 请求转发的IP是127.0.0.1，才能匹配/断言成功
The RemoteAddr Route Predicate Factory 
```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: remoteaddr_route
        uri: https://example.org
        predicates:
        - RemoteAddr=192.168.1.1/24
```


```
yaml

spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**
            #            - Path=/member/get/**,/member/save
            #            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            #            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
            #            - Between=2024-11-17T23:48:50.519+08:00[Asia/Shanghai], 2024-11-18T23:48:50.519+08:00[Asia/Shanghai]
            #            - Cookie=user,hsp # 配置 Cookie 断言
            #            - Header=X-Request-Id, hello
            #            - Host=**.rainbowsea.com,**.anotherhost.org
            #            -
            #            - Method=POST,GET
            #            - Query=email, [\w-]+@([a-zA-Z]+\.)+[a-zA-Z]+ # red是参数名;gree是值(支持正则表达式)
            - RemoteAddr=127.0.0.1
```

http://127.0.0.1:20000/member/get/1  测试

**The Weight Route Predicate Factory  **
```
yaml
spring:
  cloud:
    gateway:
      routes:
      - id: weight_high
        uri: https://weighthigh.org
        predicates:
        - Weight=group1, 8
      - id: weight_low
        uri: https://weightlow.org
        predicates:
        - Weight=group1, 2
```


## Filter(过滤)
1. 一句话: 使用过滤器,可以在**请求被路由前或者之后**对请求进行处理
2. 你可以理解成，在对Http请求断言匹配成功后，可以通过网关的过滤机制，对Http请求处理
3. 简单举例:
```
java

filters:
 -AddRequestParameter=color,blue # 过滤器在匹配的请求头加上一对请求头，名称为color值为blue
,比如原来的http请求是:http://localhost:10000/member/get/1 ===过滤器处理
http://localhost:10000/member/get/1?color=blue
```

https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/global-filters.html
路由过滤器可用于修改进入的HTTP请求和返回的HTTP响应
Spring Cloud Gateway 内置了多种路由过滤器，他们都由GatewayFilter的工厂类来产生


```
yaml
spring:
  application:
    name: e-commerce-gateway
  cloud:
    gateway:
      routes: # 配置路由，可以配置多个路由 List<RouteDefinition> routes
        - id: member_route01 # 路由的id,程序员自己配置，要求唯一
          uri: lb://MEMBER-SERVICE-PROVIDER # 注意是大写发服务器名name（大小写都行）
          predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
            - Path=/member/get/**
            #            - Path=/member/get/**,/member/save
            #            - After=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间，之后
            #            - Before=2024-11-17T23:48:50.519+08:00[Asia/Shanghai] # 配置断言限制访问时间,之前
            #            - Between=2024-11-17T23:48:50.519+08:00[Asia/Shanghai], 2024-11-18T23:48:50.519+08:00[Asia/Shanghai]
            #            - Cookie=user,hsp # 配置 Cookie 断言
            #            - Header=X-Request-Id, hello
            #            - Host=**.rainbowsea.com,**.anotherhost.org
            #            -
            #            - Method=POST,GET
            #            - Query=email, [\w-]+@([a-zA-Z]+\.)+[a-zA-Z]+ # red是参数名;gree是值(支持正则表达式)
          #            - RemoteAddr=127.0.0.1
          filters:
            - AddRequestParameter=color, blue #过滤器工厂会在匹配的请求头加上一对请求头， 名称为 color 值为 blue
            - AddRequestParameter=age, 18 # 过滤器工厂会在匹配的请求头加上一对请求头，名称为 age 值为 18
        
```

1.  自定义全局 GlobalFilter 过滤器
2.  如果请求参数  user=hspedu , pwd=123456  则放行,  否则不能通过验证


# SpringCloud Sleuth+Zipkin
1. 在微服务框架中，一个由客户端发起的请求在后端系统中会经过多个不同的服务节点调用，来协同
产生最后的请求结果，每一个请求都会形成一条复杂的分布式服务调用的**链路** 。

2. 链路中的任何一环出现高延时或错误会引起整个请求最后的失败，因此对**整个服务的调用进行链路追踪和
分析** 就非常的重要。
3. Sleuth 和 Zipkin 的简单关系图：
**一句话：Sleuth 提供了一套完整的服务跟踪的解决方案，并兼容 Zipkin，** 
**Sleuth 做链路追踪，Zipkin 做数据搜集/存储/可视化** 

## Sleuth 工作原理
1. Span 和 Trace 在一个系统中使用 Zipkin 的过程-图形化**

> * Span: 基本工作单元，**表示调用链路来源，通俗的理解span就是一次请求信息** 。
> * spans 的 parent/child 关系图形化** 
> 1. 表示一请求链路，一条链路通过 Trace Id 唯一标识，Span 标识发起的请求信息，各span通过 parent id 关联起来
> 2. Trace: 类似于树结构的 Span 集合，表示一条调用链路，存在唯一标识
> 3. Span: 基本工作单元，表示调用链路来源，通俗的理解 span 就是一次请求信息。
>  ```
>  text
>   1.Span 标识发起的请求信息
>   2.各span 通过 parent id 关联起来
> ```

## Sleuth 和 Zipkin 可以对服务调用链路进行监控，并在 Zipkin 进行显示(r如图:)

```
xml
        <!--包含了 sleuth+zipkin-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>

```
查看一次调用链路的深度，以及该链路包含请求，各个请求耗时，找到请求瓶颈，为优化提供依据(重要)
浏览器访问:http://localhost:9411/zipkin/


# Spring Cloud Alibaba Nacos
官网地址: https://github.com/alibaba/Nacos
Nacos 是什么: 一句话: Nacos 就是注册中心[替代 Eureka ] + 配置中心[替代 Config]
Nacos : Dynamic Naming and Configuration Service
Nacos: 架构理论基础: CAP理论(支持AP和CP，可以切换)
浏览器访问：http://localhost:8848/nacos
用户名和密码都是: nacos
简单的说：Nacos 和 Eureka 是一样的

## 各种注册中心对比

## 注意使用 Nacos 要让 Nacos 一直在运行当中，不可以停掉，不然就无法获取到了
 Nacos 本身自己也是一个微服务

### Nacos 中 AP 和 CP 模式如何切换
> 这个不能随便切，建议保持默认的 AP 即可
> 集群环境下所有的服务都要切换
> 可以使用 postman 模拟，必须使用 put 请求，用 get 和 post 均无效

## Nacos Server 配置中心
1. 进入到 Nacos Server
2. 加入配置, **`特别提醒: 文件后缀.yaml `** 

```
xml
        <!--nacos-config-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
        <!--引入 alibaba-nacos-discovery  即场景启动器，使用版本仲裁 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```


```
yaml
server:
  port: 5000

spring:
  application:
    name: e-commerce-nacos-config-client
  # 配置 nacos
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # 配置服务注册中心地址
      config:
        server-addr: localhost:8848 # 配置中心地址
```
> 特别说明: 这里因为只有一个主机，所以我们这里的 Nacos 是既是 服务注册中心，也是配置中心，
> 所以这里的“注册中心的地址” 和 “配置中心的地址” 都是一样的。

### 注意事项和细节
1. @Value("${config.name}") 是在: org.springframework.beans.factory.annotation.Value 包下
2. 配置文件 application.yaml 和 bootstrap.yaml 结合会得到配置文件/资源地址
3. 参考文档: https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html
4. 注意在: Nacos Server 的配置文件的后缀是 .yaml ，而不是 .yml 不然，会报错，无法编译通过
5. 在项目初始化时,要保证先从配置中心进行拉取,拉取配置之后,才能保证项目的正常启动,也就是说如果
项目不能正确的获取到 Nacos Server 的配置数据,项目是启动不了的.(演示)，同理前后都是一样的，如果编写错误的话
6. springboot 中配置文件的加载是存在优先级顺序的，bootstrap.yml 优先级高于 application.yaml
7. **@RefreshScope 是springcloud 原生注解,实现配置信息自动刷新,如果在Nacos Server 修改了
配置数据,Client 端就会得到最新配置(演示)**


## Nacos 分类配置 (实现配置隔离)
**注意: nacos 要是正在运行的状态，同时不要忘记了 yaml后缀** 
### Data Id 方案
```
yaml
spring:
  profiles:
    active: test # 指定环境，常见的环境有 dev(开发)/test(测试)/prod(生产)


```

```
yaml
server:
  port: 5000

spring:
  application:
#    name: e-commerce-nacos-config
    name: e-commerce-nacos-config-client
  # 配置 nacos
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # 配置服务注册中心地址
      config:
        server-addr: localhost:8848 # 配置中心地址
        file-extension: yaml # 指定yaml格式的配置，配置中心放的配置是 yaml格式的配置


```

### Group 方案:
```
yaml

spring:
  profiles:
    active: dev # 指定环境，常见的环境有 dev(开发)/test(测试)/prod(生产)

```

```
yaml

server:
  port: 5000

spring:
  application:
    #    name: e-commerce-nacos-config
    name: e-commerce-nacos-config-client
  # 配置 nacos
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # 配置服务注册中心地址
      config:
        server-addr: localhost:8848 # 配置中心地址
        file-extension: yaml # 指定yaml格式的配置，配置中心放的配置是 yaml格式的配置
#        group: order # 指定 order组,默认DEFAULT_GROUP
        group: seckill # 指定 seckill组,默认DEFAULT_GROUP


```

### namespace 命名空间 + group 方案
```
yaml

spring:
  profiles:
    active: dev # 指定环境，常见的环境有 dev(开发)/test(测试)/prod(生产)
```

```
yaml
server:
  port: 5000

spring:
  application:
    #    name: e-commerce-nacos-config
    name: e-commerce-nacos-config-client
  # 配置 nacos
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # 配置服务注册中心地址
      config:
        server-addr: localhost:8848 # 配置中心地址
        file-extension: yaml # 指定yaml格式的配置，配置中心放的配置是 yaml格式的配置
        #        group: order # 指定 order组,默认DEFAULT_GROUP
        group: seckill # 指定 seckill组,默认DEFAULT_GROUP
        namespace: cd4b7aa7-ae61-4d5d-9eb5-efb29a07f702 # 指定对应 namespace id 阿里的


```
**注意: namespace:不要复制过多了，不要把命名空间的名称也给复制上了。** 


## Namespace/Group/Data ID 关系:
1. Nacos 默认的命名空间是: public，Namespace 主要用来实现配置隔离，隔离范围大
2. Group 默认是 DEFAULT GROUP, GROUP 可以把不同的微服务划分到同一个分组里面去
3. Service 就是微服务,相同的 Service 可以是一个 Cluster(簇/集群),Instance 就是微服务的实例


# SpringCloud Alibaba Sentinel

## Sentinel 基础
官网地址: https://github.com/alibaba/Sentinel
快速开始: https://sentinelguard.io/zh-cn/docs/quick-start.html
中文文档: https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D

## Sentinel 概述
Sentinel 是什么
> 随着微服务的流行，服务和服务之间的**稳定性** 变得越来越重要。Sentinel 以流量为切入点，
>从流量控制，熔断降级，系统负载保护等多个维度保护服务的稳定性。

Sentinel 的主要特征:
> 实时监控,机器发现，规则配置，流量控制，线程数隔离，慢调用降级，调用链路
> 异常熔断，系统自适用保护。

**一句话: Sentinel 分布式系统的流量防卫兵，保护你的微服务** 

**Sentinel 核心功能** 
流量控制,熔断降级，系统负载保护，消息削峰填谷
流量控制:
> 拿旅游景点举个例子，每个旅游景点通常都会有最大的接待量，不可能无限制的放游客进入，
> 比如长城每天只卖八万张票，超过八万的游客，无法购买票进入，因为如果超过八万人，
> 景点的工作人员可能就忙不过来，过于拥挤的景点也会影响游客的体验和心情，并且还会有安全隐患，
> 只卖 N张票，这就是一种限流的手段。

**熔断降级** 
在调用系统的时候，如果调用的时候链路当中的某个资源出现了不稳定的，最终会导致
请求发生堆积，如下图所示：


> 熔断降级可以解决这个问题，所谓的熔断降级就是当检测到调用链路中某个资源出现不稳定 表现，
> 例如: 请求响应时间长或异常比例升高的时候，则对这个资源的调用进行限制，让请求快速失败，
> 避免影响到其它的资源而导致级联故障

**系统负载保护** 
> 根据系统能够处理的请求，和允许进来的请求，来做平衡，追求的目标是在系统不被拖垮的情况下，
> 提高系统的吞吐率。

**消息削峰填谷**
> 某瞬时来了大流量的请求，而如果此时要处理所有的请求，很可能会导致系统负载过高，
> 影响稳定性。但其实可能后面几秒之内都没有消息投递，若直接把多余的消息丢掉则设有
> 充分利用系统处理消息的能力。
 - Sentinel 两个组成部分
 核心库: (Java客户端) 不依赖任何框架/库，能够运行在所有Java运行时，环境，对 Spring Cloud有较好的支持。
 控制台: (Dashboard) 基于Spring Boot 开发，打包后可以直接运行，不需要额外的 Tomcat 等应用容器。
 

Sentinel 下载的官方地址: https://github.com/alibaba/Sentinel/releases/tag/v1.8.0
运行: 指令: java-jar sentinel-dashboard-1.8.0.jar  注意；要在对应的路径下运行才行。
注意: Sentinel 控制台 默认端口是 8080 
指定: Sentinel 的服务器的端口: java -jar sentinel-dashboard-1.8.0.jar --server.port=9999 (更改Sentinel 的控制台的端口)


##  Sentinel 监控微服务(进行实时监控)

QPS:  Querles Per Second(每秒查询率) ,是服务器每秒响应的查询次数 
Sentinel 采用的是懒加载，只有调用了某个接口/服务，才能看到监控数据
Sentinel 流量控制
规则:
资源名:唯一名称，默认请求路径。
针对来源: Sentinel 可以针对调用者进行限流，填写微服务名，默认 default(不区分来源)
阈值类型/单击阈值:
 QPS(每秒钟的请求数量)：当调用该 api 的 QPS 达到阈值的时候，进行限流
 线程数: 当调用该 api 的线程达到阈值的时候，进行限流

**解读QPS和线程数的区别,注意听: 比如 QPS 和  线程我们都设置阈值为 1 ** 
> 1. 对 QPS 而言，如果 1秒内，客户端发出了2次请求，就到达阈值，从而限流。 QPS 表示: 每秒钟的请求数量
> 2. 对线程而言，如果在 1秒内，客户端降发出了 2 次请求，不一定达到线程限制的阈值，为什么呢
> 假设我们 1次请求后台会创建一个线程，但是这个请求完成时间是 0.1秒(可以视为该请求对应的线程存活 0.1 秒)，
> 所以当客户端第2次请求时，(比如客户端是在 0.3 秒发出的)，这时第1个请求的线程就是已经结束了，因此就没有达到线程的阈值，也不会
> 限流。
> 3. 可以这样理解，如果 1 个请求对应的线程平均执行时间为 0.1 那么，就相当于 QPS 为 10
> 是否集群：不需要集群。
> 流控模式: 
>   * 直接: api 达到限流条件时，直接限流
>   * 关联: 当关联的资源达到阈值时，就限流自己
>   * 链路:  当从某个接口过来的资源达到限流条件，开启限流。
流控效果:
>   * 快速失败: 直接失败，抛出异常
>   * Warm Up: 根据 codeFactor(冷加载因子，默认 3) 的值，从阈值/codeFactor,经过预热时长，才达到设置的 QPS 阈值
>   * 排队等待: 匀速排队，让请求以匀速的速度通过，阀值类型必须设置为 QPS(每秒访问请求的次数)，否则无效。


 ** demo01** 
 1. 浏览器输入: http://localhost:10004/member/get/1 , 1 秒钟内访问次数不超过 1 次,  页 面显示正常
 2. 浏览器输入: http://localhost:10004/member/get/1 , 1 秒钟内访问次数超过 1 次,  页面 出现错误提示

> 注意事项和细节
> 1. 流量规则改动，实时生效，不需重启微服务，Sentine 控制台
> 2. 在 sentinel 配置流量规则时，如何配置通配符问题，比如: /member/get/1/member/get/2 统一
>  使用一个规则
```
java

方案1:  在 sentinel 中/member/get?id=1 和 /member/get?id=2 被统一认为是 /member/get 所以
只要对 /member/get 限流就OK了。

/**
     * 这里我们使用 url占位符 + @PathVariable
     *
     * @param id
     * @return
     */
    //@GetMapping("/member/get/{id}")
    // 在sentinel中  /member/get?id=1  和  /member/get?id=2  被统一认为是 /member/get  所以只要对/member/get  限流就OK了. 进行统一的限流
    @RequestMapping(value = "/member/get/", params = "id", method = RequestMethod.GET)
    //public Result getMemberById(@PathVariable("id") Long id, HttpServletRequest request) {
    public Result getParameter(Long id) {
        Member member = memberService.queryMemberById(id);

        //String color = request.getParameter("color");
        //String age = request.getParameter("age");


        // 模拟超时 ,这里暂停 5秒
       /* try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // 使用 Result 把查询到的结果返回
        if (member != null) {
            //return Result.success("查询会员成功 member-service-nacos-provider-10004  color" + color + "age" + age, member);
            return Result.success("查询会员成功 member-service-nacos-provider-10004  color",member);
        } else {
            return Result.error("402", "ID" + id + "不存在 member-service-nacos-provider-10004 ");
        }
    }
```


```
java
方案2： URL 资源清洗
 可以通过 UrlCleaner 接口来实现资源清洗，也就是对于 /member/get/{id} 这个 URL,
我们可以统一归集到 /member/get/* 资源下，具体的代码实现如下: 需要实现 UrlCleaner接口
并重写其中的 clean 方法即可

```
**注意：如果 sentinel 流控规则没有持久化，当我们重启调用 API 所在微服务模块后，规则会
丢失，需要重新加入** 
```
java
package com.rainbowsea.springcloud.controller;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


/**
 * 方案2： URL 资源清洗
 * 可以通过 UrlCleaner 接口来实现资源清洗，也就是对于 /member/get/{id} 这个 URL,
 * 我们可以统一归集到 /member/get/* 资源下，具体的代码实现如下: 需要实现 UrlCleaner接口
 * 并重写其中的 clean 方法即可
 */

@Component  // 注意：同样要被 Spring IOC 容器管理起来
public class CustomUrlCleaner implements UrlCleaner {
    @Override
    public String clean(String originUrl) {

        // 判断字符串是否为空 Null
        // 特别注意: StringUtils.isBlank 是在:org.apache.commons.lang3.StringUtils 包下的
        if (StringUtils.isBlank(originUrl)) {
            return originUrl;
        }

        if (originUrl.startsWith("/member/get")) {

            // 1.如果请求的是接口 /member/get 开头的，比如: /member/get/1
            // 2.给sentinel 的返回的资源名就是 /member/get/*
            // 3. 在 sentinel 对 /member/get/* 添加流控规则即可
            return "/member/get/*";
        }
        return originUrl;
    }
}


```


**流量控制实例-线程数**
当调用  member-service-nacos-provider-10004  的  /member/get/*  接口/API 时，限制 只有一个工作线程，否则直接失败，抛异常.
> 浏览器输入: http://localhost:10004/member/get/1 ,  快速刷新,  页面显示正常(原因 是服务执行时间很短，刷新下一次的时候，启动的工作线程，已经完成)
为了看到效果，我们修改下 com/hspedu/springcloud/controller/MemberController.java
```
java


        // 让线程休眠1s，模拟执行时间
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
```


**注意事项和细节：** 
> 当我们请求一次微服务的 API 接口时，后台会启动一个线程.

**阈值类型 QPS 和 线程数的区别讨论** 
> 如果一个线程**平均执行时间** 为 0.05 秒，就说明在 1秒钟，可以执行 20次(相当于 QPS 为 20)
> 如果一个线程**平均执行时间** 为 1秒，说明 1 秒钟，可以执行 1次数(相当于 QPS 为1)
> 如果一个线程**平均执行时间** 为 2 秒，说明2秒钟内，才能执行1次请求。


## 流量控制实例-关联
当关联的资源达到阈值时，就限流自己

当调用 member-service-nacos-provider-10004 的 /t2 API 接口时，如果 QPS 超过 1，这时调用 /t1 API 接口 直接接失败，抛异常.
老师梳理 /t2 是关联的资源 , 限流的资源是 /t1 
> 
> 这里使用 postman 模拟高并发访问/t2 ；然后在 postman 执行高并发访问/t2没有结束时，去访问 /t1 才能
> 看到流控异常出现。t1被流控了。

## Warm up(冷启动，预热) 介绍
1. 概述
 当流量突然增大的时候，我们常常会希望系统从空闲状态到繁忙状态的切换时间长一些。即如果系统在此
 之前长期处于空闲状态，我们希望处理请求的数量是缓步的增多，经过预期的时间以后，到达系统处理请求个数
 的最大值，Warm up (冷启动，预热)模式就是为了实现这个目的的。
**这个场景主要用于额外开销的场景，例如：建立数据库连接等。** 
官方介绍 Warm Up 地址链接: https://github.com/alibaba/Sentinel/wiki/%E9%99%90%E6%B5%81---%E5%86%B7%E5%90%AF%E5%8A%A8
> 默认 coldFactor 为 3 ,即请求 QPS 从 threshold / 3 开始，经预热时长逐渐升至设定的QPS阈值。
> Warm up(冷启动/预热) 
> 应用场景：秒杀在开启瞬间，大流量很容易造成冲垮系统，Warm up 可慢慢的把流量放入，最终将阈值增长
> 到设置阈值
>
**需求分析/图解：** 
1. 需求: 通过 Sentinel 实现流量控制，演示 Warm up
2. 调用: member-service-nacos-provider-10004 的 /t2 API接口，将QPS设置为 9 ,设置 Warm up 值为 3 .
3. 含义为请求 /t2 的QPS 从threshold/ 3(9/3 = 3) 开始，有预热时长(3秒) 逐渐升至设定的 QPS阈值为(9)
4. 为什么是  9 /3 ，这个 3 就是默认冷启动启动加载因子 coldFactor = 3
5. 测试预期效果：在前3秒，如果访问 /t2 的 QPS超过 3,会直接报错，在3秒后，访问 /t2 的QPS 超过 3,小于等于9 ,是正常访问。
> 浏览器访问:http://localhost:10004/t2 快速刷新页面，在前3秒，会出现流控异常，后3秒就正常了(
>如果你刷新非常快 QPS > 9 ，仍然会出现流控异常)

**注意事项和细节：** 
> 测试 Warm up 效果不是很好测，如果出不来可以尝试，调整流控规则: 比如 QPS 为 11，Warm up 预热时间为 6秒
>  流控规则: 比如 QPS 为 11 ，Warm up 预热时间为 6 秒。
> 如果请求停止(即: 一段时间没有达到阈值 Warm up 过程将重复，大家可以理解是一个弹性过程)
 
## 排队介绍
1. 排队方式: 这种方式严格控制了请求通过的间隔时间，也即是让请求以均匀的速度通过，对应的是“漏桶算法”
2. 一张图: 阈值为QPS=2时，每隔 500ms 才允许通过下一次请求
3. 这种方式主要用于处理间隔突发的流量，例如：消息队列。比如这样的场景，在某一秒有大量的请求到来。
而接下来的几秒则处于空闲的状态，我们希望系统能够在接下来的空闲期间逐渐处理这些请求，而不是在第一秒直接
拒绝多余的请求。
4. 均速排队，阈值必须设置为QPS

**需求分析:/图解:** 
1. 需求：通过 Sentinel 实现流量控制-排队
2. 调用 member-service-nacos-provider-1004 的 t2 API接口，将QPS设置为 1
3 当调用 /t2 的QPS 超过 1时，不拒绝请求，而是排队等待，依次执行
4 当等待时间超过 10 ，则为等待超时
修改：代码: 模拟延时 10s
```
java

    @GetMapping("/t2")
    public Result t2() {

        // 让线程休眠 1s，模拟执行时间为1s=>当多少个请求就会造成超时
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 输出线程信息
        log.info("执行t2()，线程id={}",Thread.currentThread().getId());
        return Result.success("t2执行成功");
    }

```
> 演示: 浏览器访问  http://localhost:10004/t2  快速刷新页面 9 次，观察前台/后台输出的情
      况
> 2025-01-12 10:28:00.735  INFO 26800 --- [io-10004-exec-6] c.r.s.controller.MemberController        : 执行t2()，线程id=67
  2025-01-12 10:28:01.739  INFO 26800 --- [io-10004-exec-8] c.r.s.controller.MemberController        : 执行t2()，线程id=69
  2025-01-12 10:28:02.749  INFO 26800 --- [io-10004-exec-7] c.r.s.controller.MemberController        : 执行t2()，线程id=68
  2025-01-12 10:28:03.737  INFO 26800 --- [io-10004-exec-2] c.r.s.controller.MemberController        : 执行t2()，线程id=63
> 输出结果分析： 没有报错误，后台请求排队执行，每隔1s匀速执行
> 浏览器访问  http://localhost:10004/t2  快速刷新页面 20 次，当请求等待时间超过 10S, 仍然出现流控异常

## Sentinel 熔断降级
**线程堆积引出熔断降级** 
文档地址:https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
> 线程堆积引出熔断降级
> 1. 一个服务常常会调用别的模块，可能是另外的一个远程服务，数据库，或者第三方API等【架构图】
> 2. 例如: 支付的时候，可能需要远程调用银联提供的API：查询某个商品的价格，可能需要进行数据库查询。
>然而，这个被依赖的服务的稳定性是不能保证的。如果依赖的服务出现了不稳定的请求，请求的响应时间变长，那么调用
>服务的方法的响应时间也会变长，线程会产生堆积，最终可能耗尽业务自身的线程池，服务本身也变得不可用。
>这时，我们对**不稳定的服务进行熔断降级** ，让其快速返回结果，不要造成线程堆积
>
>
>
图解说明：
* 现代微服务架构都是分布式的，由非常多个的服务组成，不同服务之间相互调用，组成复杂的调用链路。
* 链路调用中会产生放大的效果，复杂链路的某一环不稳定，就可能会层层级联，最终导致整个链路都不可用。
* 因此需要对不稳定的弱依赖服务调用进行熔断降级，暂时切断不稳定调用，避免局部不稳定因素导致整体的雪崩。

**熔断，降级，限流 三者的关系** 
* 熔断强调的是服务之间的调用的能实现自我恢复的状态*
* 限流是从系统的流量入口考虑，从进入的流量上进行限制，达到保护系统的作用。
* 降级，是从系统业务的维度考虑，流量大了或者频繁异常，可以牺牲一些非核心业务，保护核心流程正常使用。
**梳理：** 
> 熔断是降级方式的一种
> 降级又是限流的一种
> 三者都是为了通过一定的方式在流量过大或者出现异常时，保护系统的手段。

慢调用比例：
1. 慢调用比例:(SLOW_REQUEST_RATIO) :选择以慢调用比例作为阈值，需要设置允许的慢调用RT（即
最大的响应时间），请求的响应时间大于该值则统计为慢调用。
2. 当单位统计时长(statIntervalMs) 内请求数目大于设置的最小请求数目，并且慢调用的比例大于阈值，则
接下来的熔断时长内请求会自动被熔断。
3. 熔断时长后，熔断器会进入探测恢复状态(HALF-OPEN状态)，若接下来的一个请求响应时间小于设置的
慢调用RT则结束熔断，若大于设置的慢调用RU则会再次熔断
**异常比例：** 
1. 异常比例(ERROR_RATIO) ： 当单位统计时长(sataIntervalMs)内请求数目大于设置的最小请求
数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。
2. 经过熔断时长后熔断器会进入探测恢复状态(HALF-OPEN状态)
3. 若接下来的一个请求成功完成(没有错误)则结束熔断，否则会再次被熔断
4. 异常比率的阈值范围是[0.0,1.0],代表0%~100%

**异常数** 
1. 异常数(ERROR_COUNT) ；当单位统计时长内的异常数目超过阈值之后会自动进行熔断
2. 经过熔断时长后熔断器会进入探测恢复状态(HALF-OPFE状态)
3. 若接下来的一个请求成功完成(没有错误)则会结束熔断，否则会再次被熔断

**演示慢调用比例：** 
```
java
    @GetMapping("/t3")
    public Result t3() {
        try {
            // 让线程休眠300毫秒，模拟执行时间
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success("t3()执行成功");
    }

```


**演示 通过 Sentinel  实现  熔断降级控制**
2. 当调用 member-service-nacos-provider-10004 的 /t4 API 接口时，当资源的每秒请求 量>=5，
并且每秒异常总数占通过量的比值超过 20%(即异常比例到 20%), 断路器打开(即: 进入降级状态), 
让 /t4 API 接口 微服务不可用
3.  当对/t4 API 接口  访问降到  1S 内 1 个请求，降低访问量了，断路器关闭，5 秒后微服 务恢复

**熔断降级实例-异常数**
10.5.7.1 需求分析/图解
1.  需求: 通过Sentinel实现熔断降级控制
2. 当调用 member-service-nacos-provider-10004 的/t5API 接口时，当资源的每分钟请求量>=5，
并且每分钟异常总数>=5 , 断路器打开(即: 进入降级状态), 让 /t5 API 接口 微服务不可用
3.  当熔断时间(比如 20S)结束后，断路器关闭,  微服务恢复

**说明:** 
> 资源在1分钟的异常数目超过阈值之后会进行熔断降级
> 当异常数统计是分钟级别的，若设置的时间窗口小于 60 s,则结束熔断状态仍可能再进入
>熔断状态，测试时，最后将时间窗口设置超过60s
>


## **sentinel 热点规则：** 
一个问题引出热点key限流
1.热点: 热点即经常访问的数据。很多时候我们希望统计热点数据中，访问频次最高的 Top K 数据，并对其访问进行限制。
2. 比如：某条新闻上**热搜** ，在某段时间内高频访问，为了防止系统雪崩，可以对该条新闻进行热点限流
热点规则官方地址: https://github.com/alibaba/Sentinel/wiki/%E7%83%AD%E7%82%B9%E5%8F%82%E6%95%B0%E9%99%90%E6%B5%81
```
text
何为热点？热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的 Top K 数据，并对其访问进行限制。比如：

商品 ID 为参数，统计一段时间内最常购买的商品 ID 并进行限制
用户 ID 为参数，针对一段时间内频繁访问的用户 ID 进行限制
热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。

Sentinel Parameter Flow Control

Sentinel 利用 LRU 策略统计最近最常访问的热点参数，结合令牌桶算法来进行参数级别的流控。热点参数限流支持集群模式。

```
* 热点参数限流会统计传入参数的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源进行限流
* 热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。
* Sentinel 利用LRU策略统计最近最常访问的热点参数，结合**令牌桶算法** 来进行参数级别的流控:https://blog.csdn.net/qq_34416331/article/details/106668747
* 热点参数限流支持集群模式。

**演示：热点规则：** 
需求:  通过 Sentinel  实现  热点 Key 限流
2.  对  member-service-nacos-provider-10004  的  /news?id=x&type=x API 接口进行热点限 流
3.  假定 id=10  这一条新闻是当前的热点新闻， 当查询新闻时，对通常的 id(非热点新闻) 请求  QPS 限定为 2,  如果 id=10 QPS 限定为 100
4.  如果访问超出了规定的 QPS,  触发热点限流机制,    调用自定义的方法，给出提示信息.
5.  当对  /news?id=x&type=x API 接口  降低访问量，QPS 达到规定范围,  服务恢复
独立设置热点 id=10 的 QPS 阈值(即添加例外)

**注意事项和细节:** 
* 热点参数类型是(byte/int/long/float/double/char/String)
* 热点参数值，可以配置多个
* 热点规则只对指定的参数生效(比如本实例对id生效，对type不生效)

## 系统规则:
如我们系统最大性能能抗  100QPS,  如何分配  /t1 /t2  的 QPS?
2.  方案 1:    /t1  分配  QPS=50 /t2  分配  QPS=50 ,  问题,  如果/t1  当前 QPS 达到  50 ,  而 /t2 的  QPS 才 10,  会造成没有充分利用服务器性能.
3.  方案 2:    /t1  分配  QPS=100 /t2  分配  QPS=100 ,  问题,  容易造成  系统没有流量保护， 造成请求线程堆积，形成雪崩.
4.  有没有对各个  资源请求的  QPS 弹性设置,  只要总数不超过系统最大 QPS 的流量保护规 则? ==>  系统规则
系统处理请求的过程想象为一个水管，到来的请求是往这个水管灌水，当系统处理顺 畅的时候，请求不需要排队，直接从水管中穿过，这个请求的RT是最短的；
,反之，当请求堆积的时候，那么处理请求的时间则会变为：排队时间  +  最短处理时间

**演示:系统规则:** 
需求:  通过 Sentinel  实现  系统规则-入口 QPS
2.  对  member-service-nacos-provider-10004  的  所有  API 接口进行流量保护，不管访问 哪个  API 接口,  系统入口总的 QPS    不能大于 2,  大于 2，就进行限流控制
3.    提示: 上面的 QPS 是老师为了方便看效果,  设置的很小
说明: 当配置的资源名 news  触发限流机制时，会调用  newsBlockHandler      方法 2.    上面的处理方案存在一些问题
     每个@SentinelResource 对应一个异常处理方法，会造成方法很多
     异常处理方法和资源请求方法在一起，不利于业务逻辑的分离
     解决方案->  自定义全局限流处理类.


## @SentinelResource  自定义全局限流处理类
> 注意：blockHandler 只负责sentine控制台配置违规的异常
> 而 fallback 负责 java异常/业务异常
                                                                                                                                                                                                                
如果希望忽略某个异常，可以使用 exceptionsToIgnore
如果希望忽略某个异常(支持数组)，可以使用 exceptionsToIgnore
```
java
// 这里我们使用全局限流处理类，显示限流信息

    /**
     * value="t6" 表示 sentinel 限流资源的名字
     * blockHandlerClass = CustomGlobalBlockHandler.class:全局限流处理类
     * blockHandler = "handlerMethod1" 指定使用全局限流处理类哪个方法，来处理限流信息
     * fallbackClass = CustomGlobalFallbackHandler.class 全局 fallback处理类
     * fallback = "fallbackHandlerMethod1" 指定使用全局fallback处理类哪个方法来处理java异常/业务异常
     * exceptionsToIgnore = {NullPointerException.class}
     * @return
     */
    @GetMapping("/t6")
    @SentinelResource(value = "t6",
            //   //设置处理sentinel 控制台违规后的异常 blockHand
            blockHandlerClass = CustomGlobalBlockHandler.class,
            blockHandler = "handlerMethod1",
    //设置处理Java异常的 fallback
            fallbackClass = CustomGlobalFallbackHandler.class,
            fallback = "fallbackHandlerMethod1",

            // 如果希望忽略某个异常，可以使用 exceptionsToIgnore，这里忽略NullPointerException异常
            exceptionsToIgnore = {NullPointerException.class}
    )
    public Result t6() {
        log.info("执行t6() 线程id={}", Thread.currentThread().getId());


        // 假定；当访问t6资源次数是5倍数时，就出现Java异常
        if (++num % 5 == 0) {
            throw new NullPointerException("null 指针异常 num=" + num);
        }

        if (++num % 6 == 0) {
            throw new RuntimeException("RuntimeException  num=" + num);
        }
        return Result.success("200", "t6()执行OK~~~");
    }

```

https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81
https://github.com/alibaba/Sentinel/wiki/注解支持
@SentinelResource 注解
注意：注解方式埋点不支持 private 方法。

@SentinelResource 用于定义资源，并提供可选的异常处理和 fallback 配置项。 @SentinelResource 注解包含以下属性：

value：资源名称，必需项（不能为空）
entryType：entry 类型，可选项（默认为 EntryType.OUT）
blockHandler / blockHandlerClass: blockHandler 对应处理 BlockException 的函数名称，
可选项。blockHandler 函数访问范围需要是 public，返回类型需要与原方法相匹配，参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为 BlockException。blockHandler 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 blockHandlerClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。
fallback / fallbackClass：fallback 函数名称，可选项，用于在抛出异常的时候提供 fallback 处理逻辑。fallback 函数可以针对所有类型的异常（除了 exceptionsToIgnore 里面排除掉的异常类型）进行处理。fallback 函数签名和位置要求：
返回值类型必须与原函数返回值类型一致；
方法参数列表需要和原函数一致，或者可以额外多一个 Throwable 类型的参数用于接收对应的异常。
fallback 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 fallbackClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。
defaultFallback（since 1.6.0）：默认的 fallback 函数名称，可选项，通常用于通用的 fallback 逻辑（即可以用于很多服务或方法）。默认 fallback 函数可以针对所有类型的异常（除了 exceptionsToIgnore 里面排除掉的异常类型）进行处理。若同时配置了 fallback 和 defaultFallback，则只有 fallback 会生效。defaultFallback 函数签名要求：
返回值类型必须与原函数返回值类型一致；
方法参数列表需要为空，或者可以额外多一个 Throwable 类型的参数用于接收对应的异常。
defaultFallback 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 fallbackClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。
exceptionsToIgnore（since 1.6.0）：用于指定哪些异常被排除掉，不会计入异常统计中，也不会进入 fallback 逻辑中，而是会原样抛出。
1.8.0 版本开始，defaultFallback 支持在类级别进行配置。

注：1.6.0 之前的版本 fallback 函数只针对降级异常（DegradeException）进行处理，不能针对业务异常进行处理。

特别地，若 blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 BlockException 时只会进入 blockHandler 处理逻辑。若未配置 blockHandler、fallback 和 defaultFallback，则被限流降级时会将 BlockException 直接抛出（若方法本身未定义 throws BlockException 则会被 JVM 包装一层 UndeclaredThrowableException）。


整合 Openfeign 和 Sentinel
在 member-service-nacos-consumer-80 调用某个无效服务时,启动Sentinel的熔断降级机制,
能够快速返回响应,而不是使用默认的超时机制(因为超时机制容易线程堆积,从而导致雪崩) 
> OpenFeign 默认超时时间 1秒钟,即等待返回结果 1秒
> 测试: 让 10004 服务对应的API执行时间很长(比如休眠2秒),这时openfeign 去调用会怎么样
> 如果 10004 对应的API响应时间比 10006 明显长,这时候,你会发现 openfeign+sentinel整合后
> 会自动响应时间上更短的哪个服务,这时总是调用 10006的API(响应时间更短)
```
xml
# openfeign 和 sentinel 整合,必须配置
feign:
  sentinel:
    enabled: true

```