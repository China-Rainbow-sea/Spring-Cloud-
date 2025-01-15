package com.rainbowsea.springcloud.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体类对应 storage 表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor  // 使用 lombok 插件进行自动生成
public class Storage {
    private Long id;
    private Long productId;
    private Integer amount;
}
