///*
//package com.rainbowsea.springcloud.config;
//
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//*/
///**
// * 配置类-配置路由
// *//*
//
//@Configuration  // 标注配置类
//public class GateWayRoutesConfig {
//
//
//    */
///**
//     * - id: member_route02 # 路由的id,程序员自己配置，要求"唯一"
//     * uri: http://localhost:10000
//     * predicates: # 断言,可以多种形式 # 断言为通过报 404 找不到异常
//     * # 这时如果客户端/浏览器 访问 gateway 的url http://localhost:20000/member/save
//     * # 匹配Path成功 最终访问的url就是 ； http://localhost:10000/member/save 给对对应配置转发的10000服务处理了
//     * - Path=/member/save
//     *
//     * @param routeLocatorBuilder
//     * @return
//     *//*
//
//    // 注意：编写了配置类的方式配置路由，则需要将 application.yaml 文件当中配置的路由注释掉
//    @Bean  // 注入到 ioc 容器当中管理起来
//    public RouteLocator myRouteLocator04(RouteLocatorBuilder routeLocatorBuilder) {
//        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
//
//        */
///*
//        1. 是一个函数式接口
//        2. 接收的类型是: PredicateSpec, 返回的类型是 Route.AsyncBuilder
//        3.   r -> r.path("/member/get/**")
//                 .uri("http://localhost:10000"))
//
//         *//*
//
//        // 方法写
//        return routes.route("member_route04",
//                r -> r.path("/member/get/**")
//                        .uri("http://localhost:10000"))
//                .build();
//    }
//}
//*/
