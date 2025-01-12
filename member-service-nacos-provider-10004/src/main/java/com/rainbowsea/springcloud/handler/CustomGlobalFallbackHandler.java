package com.rainbowsea.springcloud.handler;


import com.rainbowsea.springcloud.entity.Result;

/**
 * CustomGlobalFallbackHandler ：全局 fallback处理类
 * 在 CustomGlobalFallbackHandler 类中，可以去编写处理Java异常/业务异常方法-static
 */
public class CustomGlobalFallbackHandler {
    public static Result fallbackHandlerMethod1(Throwable throwable) {
        return Result.error("402", "java异常信息 + " + throwable.getMessage());
    }


    public static Result fallbackHandlerMethod2(Throwable throwable) {
        return Result.error("402", "java异常信息 + " + throwable.getMessage());
    }
}

