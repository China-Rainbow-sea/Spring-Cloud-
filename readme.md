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