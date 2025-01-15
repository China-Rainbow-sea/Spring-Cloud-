package com.rainbowsea.springcloud.service.impl;


import com.rainbowsea.springcloud.dao.OrderDao;
import com.rainbowsea.springcloud.entity.Orders;
import com.rainbowsea.springcloud.service.AccountService;
import com.rainbowsea.springcloud.service.OrderService;
import com.rainbowsea.springcloud.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;


    @Resource
    private StorageService storageService;


    @Resource
    private AccountService accountService;



    @Override
    /**
     * 老师解读
     * 1. @GlobalTransactional : 分布式全局事务控制  io.seata.spring.annotation包
     * 2. name = "hspedu-save-order" 名称，程序员自己指定,保证唯一即可
     * 3. rollbackFor = Exception.class 指定发送什么异常就回滚, 这里我们指定的是Exception.class
     *    即 只要发生了异常就回滚
     */
    @GlobalTransactional(name = "hspedu-save-order", rollbackFor = Exception.class)
    public void save(Orders order) {

        //后面我们如果需要打印日志
        log.info("====创建订单 start=====");

        log.info("====本地生成订单 start===");
        orderDao.save(order);//调用本地方法生成订单order
        log.info("====本地生成订单 end===");

        log.info("====扣减库存 start===");
        //远程调用storage微服务扣减库存
        storageService.reduce(order.getProductId(), order.getNums());
        log.info("====扣减库存 end===");

        log.info("====扣减用户余额 start===");
        //远程调用account微服务扣减用户money
        accountService.reduce(order.getUserId(), order.getMoney());
        log.info("====扣减用户余额 end===");

        log.info("====本地修改订单状态 start===");
        //调用本地方法修改订单状态0->1
        orderDao.update(order.getUserId(), 0);
        log.info("====本地修改订单状态 end===");

        log.info("====创建订单 end=====");
    }
/*
    @Override
    *//**
     * 解读：
     * 1. @GlobalTransactional : 分布式全局事务控制  io.seata.spring.annotation.GlobalTransactional
     * 2. name = "rainbowsea-save-order" 名称，程序员自己指定，保证唯一即可
     * 3. rollbackFor=Exception.class 指定发生什么异常就回滚，这里我们指定的是，只要发生了异常就回滚
     *//*
    // //下面这句话是做全局事务控制的,  如果没有，则没有分布式全局事务控制
    @GlobalTransactional(name = "hspedu_order_tx_group", rollbackFor = Exception.class)
    public void save(Orders order) {


        //后面我们如果需要打印日志
        log.info("====创建订单 start=====");

        log.info("====本地生成订单 start===");
        orderDao.save(order);//调用本地方法生成订单order
        log.info("====本地生成订单 end===");

        log.info("====扣减库存 start===");
        //远程调用storage微服务扣减库存
        storageService.reduce(order.getProductId(), order.getNums());
        log.info("====扣减库存 end===");

        log.info("====扣减用户余额 start===");
        //远程调用account微服务扣减用户money
        accountService.reduce(order.getUserId(), order.getMoney());
        log.info("====扣减用户余额 end===");

        log.info("====本地修改订单状态 start===");
        //调用本地方法修改订单状态0->1
        orderDao.update(order.getUserId(), 0);
        log.info("====本地修改订单状态 end===");

        log.info("====创建订单 end=====");
    }*/
}
