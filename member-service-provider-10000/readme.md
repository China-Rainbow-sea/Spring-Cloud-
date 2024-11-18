# 创建数据库的脚本SQL语句
```
sql

CREATE DATABASE e_commerce_center_db;
USE e_commerce_center_db;
CREATE TABLE member
(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    `NAME` VARCHAR(64) COMMENT '用户名',
    pwd CHAR(32) COMMENT '密码',
    mobile VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(64) COMMENT '邮箱',
    gender TINYINT COMMENT '性别',
    PRIMARY KEY (id)
);

INSERT INTO member VALUES
(NULL, 'smith', MD5('123'), '123456789000', 'smith@sohu.com', 1);
SELECT * FROM member

```
# 后续的添加的用户的密码都是：123

# 注意事项和细节
1. 如果前端是以表单形式提交了/是以 parameters，则不需要使用@RequestBody，才会进行对象 bean参数的封装，
同时保证 http的请求头的 content-type 是对应
2. 在进行 SpringBoot 应用程序测试时，引入的 JUnit 是 org.junit.jupiter.api.Test
3.在运行程序时，一定要确保你的 XxxMapper.mxl文件被自动放到 target 目录的 classes 指定的目录当中

```
java
package com.rainbowsea.springcloud.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomGateWayFilter implements GlobalFilter, Ordered {


    // filter 是核心的方法，将我们的过滤的业务，写在该方法中
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 先获取到对应的参数值
        // 比如:http://localhost:20000/member/get?user=rainbowsea&pwd123456
        // 第一种方式获得:get("user")返回的是List集合，需要再get(0) 表明序列返回的是字符串
        //String regStr = exchange.getRequest().getQueryParams().get("user").get(0);
        String user = exchange.getRequest().getQueryParams().getFirst("user");
        String pwd = exchange.getRequest().getQueryParams().getFirst("pwd");
        if (!("rainbowsea".equals(user) && "123456".equals(pwd))) { // 如果不满足条件
            System.out.println("非法用户");
            // NOT_ACCEPTABLE 是 406 错误
            // NOT_ACCEPTABLE(406, "Not Acceptable"),
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);// 回应
            return exchange.getResponse().setComplete();
        }

        // 验证通过，放行 // 注意需要将前面我们再 application.yaml 当中配置的 filters 过滤器的前缀给去了，进行测试比较明显
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

```
浏览器运行测试: http://localhost:20000/member/get/1?user=rainbowsea&pwd=123456
http://localhost:20000/member/get/1?user=rainbowsea&pwd=12356