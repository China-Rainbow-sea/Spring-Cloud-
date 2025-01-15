package com.rainbowsea.springcloud.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StorageDao {
    // 扣减库存信息
    void reduce(@Param("productId") Long productId, @Param("nums") Integer nums);
}
