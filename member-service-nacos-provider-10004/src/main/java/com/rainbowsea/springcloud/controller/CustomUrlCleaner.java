//package com.rainbowsea.springcloud.controller;
//
//import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//
//
///**
// * 方案2： URL 资源清洗
// * 可以通过 UrlCleaner 接口来实现资源清洗，也就是对于 /member/get/{id} 这个 URL,
// * 我们可以统一归集到 /member/get/* 资源下，具体的代码实现如下: 需要实现 UrlCleaner接口
// * 并重写其中的 clean 方法即可
// */
//
//@Component  // 注意：同样要被 Spring IOC 容器管理起来
//public class CustomUrlCleaner implements UrlCleaner {
//    @Override
//    public String clean(String originUrl) {
//
//        // 判断字符串是否为空 Null
//        // 特别注意: StringUtils.isBlank 是在:org.apache.commons.lang3.StringUtils 包下的
//        if (StringUtils.isBlank(originUrl)) {
//            return originUrl;
//        }
//
//        if (originUrl.startsWith("/member/get")) {
//
//            // 1.如果请求的是接口 /member/get 开头的，比如: /member/get/1
//            // 2.给sentinel 的返回的资源名就是 /member/get/*
//            // 3. 在 sentinel 对 /member/get/* 添加流控规则即可
//            return "/member/get/*";
//        }
//        return originUrl;
//    }
//}
