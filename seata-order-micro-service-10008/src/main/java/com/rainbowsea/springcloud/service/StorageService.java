package com.rainbowsea.springcloud.service;


import com.rainbowsea.springcloud.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-storage-micro-service")  // 表示使用opFeign与分布式多个微服务通信
// value 是配置在 nacos 当中显示的 服务名(也是配置在 对应微服务applicaton 当中的 name 服务名)，不可随便写
public interface StorageService {

    /**
     * 1.远程调用方式是post
     * 2. 远程调用的 url 为 : http://@FeignClient(value = "seata_storage_micro_service")/storage/reduce（@PostMapping("/storage/reduce")）
     * 3. seata_storage_micro_service 是 nacos 注册中心服务名
     * 4. openfeign 会根据负载均衡算法来决定调用的是 : 10004/10006,默认是轮询算法
     * 5. openfeign 是通过接口方式调用服务
     * 6. 特别注意: 该方法要和对应微服务模块当中的 controller 的方法一致(参数，post,方法名,返回类型等)
     *
     * @param productId
     * @param nums
     * @return
     */
    // 扣减库存
    @PostMapping("/storage/reduce")
    Result reduce(@RequestParam("productId") Long productId, @RequestParam("nums") Integer nums);
}
