package com.rainbowsea.springcloud.service;

import com.rainbowsea.springcloud.entity.Result;
import org.springframework.stereotype.Component;


@Component  // 被 ioc 容器管理
public class MemberFeignFallbackService implements MemberOpenFeignService {
    @Override
    public Result getMemberById(Long id) {
        return Result.error("500", "被调用服务异常,熔断降级,快速返回结果,防止线程堆积");
    }
}
