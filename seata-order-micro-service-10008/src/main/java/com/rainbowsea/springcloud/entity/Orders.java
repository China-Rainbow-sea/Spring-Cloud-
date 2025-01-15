package com.rainbowsea.springcloud.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer nums;
    private Integer money;
    private Integer status;
}
