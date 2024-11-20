//package com.rainbowsea.springcloud.config;
//
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 配置类-配置路由
// */
//@Configuration  // 标注配置类
//public class GateWayRoutesConfig {
//    // 注意：编写了配置类的方式配置路由，则需要将 application.yaml 文件当中配置的路由注释掉
//    @Bean  // 注入到 ioc 容器当中管理起来
//    public RouteLocator myRouteLocator04(RouteLocatorBuilder routeLocatorBuilder) {
//        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
//        // 方法写
//        return routes.route("member_route04",
//                r -> r.path("/member/get/**")
//                        .uri("http://localhost:10000"))
//                .build();
//    }
//
///*
//        1. 是一个函数式接口
//        2. 接收的类型是: PredicateSpec, 返回的类型是 Route.AsyncBuilder
//        3.   r -> r.path("/member/get/**")
//                 .uri("http://localhost:10000"))
//
//         */
//
//    @Bean
//    public RouteLocator
//    myRouteLocator02(RouteLocatorBuilder
//                             routeLocatorBuilder) {
//        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
//        return routes.route("member_route05", r -> r.path("/member/save")
//                .uri("http://localhost:10000")).build();
//    }
//}
//
