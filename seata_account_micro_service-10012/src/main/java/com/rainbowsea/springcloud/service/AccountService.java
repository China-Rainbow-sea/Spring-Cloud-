package com.rainbowsea.springcloud.service;


public interface AccountService {
    //扣减用户的money
    void reduce(Long userId, Integer money);
}
