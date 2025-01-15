package com.rainbowsea.springcloud.dao;

import com.rainbowsea.springcloud.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 */
@Mapper
public interface OrderDao {

    /**
     * 新建订单
     *
     * @param order
     */
    void save(Orders order);

    /**
     * 修改订单状态
     */
    void update(@Param("userId") Long userId, @Param("status") Integer status);
}
