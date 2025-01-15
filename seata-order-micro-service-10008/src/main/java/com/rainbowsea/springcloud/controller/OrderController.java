package com.rainbowsea.springcloud.controller;


import com.rainbowsea.springcloud.entity.Orders;
import com.rainbowsea.springcloud.entity.Result;
import com.rainbowsea.springcloud.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderController {

    @Resource
    private OrderService orderService;


    /**
     * 老师提醒 - 简单回顾一把
     * 1. 我们的前端如果是以json格式来发送添加信息Order， 那么我们需要使用@RequestBody
     * 才能将数据封装到对应的bean, 同时保证http的请求头的 content-type是对应
     * 2. 如果前端是以表单形式提交了，则不需要使用@RequestBody, 才会进行对象参数封装, 同时保证
     * http的请求头的 content-type是对应
     */
    @GetMapping("/order/save")


    public Result save(Orders order) {
        orderService.save(order);
        return Result.success("订单创建成功", null);
    }

}
