package com.rainbowsea.springcloud.controller;


import com.rainbowsea.springcloud.entity.Result;
import com.rainbowsea.springcloud.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AccountController {

    @Resource
    AccountService accountService;

    @PostMapping("/account/reduce")
    public Result reduce(@RequestParam("userId") Long userId, @RequestParam("money") Integer money) {
        //模拟异常,超时
        //openfeign 接口调用默认超时时间为1s
     /*   try {
            TimeUnit.SECONDS.sleep(12);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        accountService.reduce(userId, money);
        return Result.success("200", "扣减账户余额OK");
    }
}
