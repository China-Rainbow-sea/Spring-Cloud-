package com.rainbowsea.springcloud.service;


import com.rainbowsea.springcloud.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-account-micro-service") // 表示使用opFeign与分布式多个微服务通信
// value 是配置在 nacos 当中显示的 服务名(也是配置在 对应微服务applicaton 当中的 name 服务名)，不可随便写
public interface AccountService {


    /**
     * 1.远程调用方式是 post
     * 2. 远程调用url为: http://seata_account_micro_service/account/reduce
     * 3. seata_account_micro_service 是 nacos 注册中心服务名
     * 4. openfeign 是通过接口方式调用服务
     *
     * @param userId
     * @param money
     * @return
     */
    // 扣减账户余额
    @PostMapping("/account/reduce")
    Result reduce(@RequestParam("userId") Long userId, @RequestParam("money") Integer money);
}


