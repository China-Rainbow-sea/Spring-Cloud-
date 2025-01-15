package com.rainbowsea.springcloud.controller;


import com.rainbowsea.springcloud.entity.Result;
import com.rainbowsea.springcloud.service.StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StorageController {

    @Resource
    private StorageService storageService;


    @PostMapping("/storage/reduce")
    public Result reduce(@RequestParam("productId") Long productId, @RequestParam("nums") Integer nums) {
        storageService.reduce(productId, nums);
        return Result.success("扣减库存成功", null);
    }
}
