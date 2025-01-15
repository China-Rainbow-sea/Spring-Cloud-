package com.rainbowsea.springcloud.service;


/**
 *
 */
public interface StorageService {
    // 扣减库存
    void reduce(Long productId, Integer nums);
}
