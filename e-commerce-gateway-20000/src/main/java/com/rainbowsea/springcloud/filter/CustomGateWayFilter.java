//package com.rainbowsea.springcloud.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class CustomGateWayFilter implements GlobalFilter, Ordered {
//
//
//    // filter 是核心的方法，将我们的过滤的业务，写在该方法中
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        // 先获取到对应的参数值
//        // 比如:http://localhost:20000/member/get?user=rainbowsea&pwd123456
//        // 第一种方式获得:get("user")返回的是List集合，需要再get(0) 表明序列返回的是字符串
//        //String regStr = exchange.getRequest().getQueryParams().get("user").get(0);
//        String user = exchange.getRequest().getQueryParams().getFirst("user");
//        String pwd = exchange.getRequest().getQueryParams().getFirst("pwd");
//        if (!("rainbowsea".equals(user) && "123456".equals(pwd))) { // 如果不满足条件
//            System.out.println("非法用户");
//            // NOT_ACCEPTABLE 是 406 错误
//            // NOT_ACCEPTABLE(406, "Not Acceptable"),
//            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);// 回应
//            return exchange.getResponse().setComplete();
//        }
//
//        // 验证通过，放行 // 注意需要将前面我们再 application.yaml 当中配置的 filters 过滤器的前缀给去了，进行测试比较明显
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
