package com.rainbowsea.springcloud.service.impl;


import com.rainbowsea.springcloud.dao.StorageDao;
import com.rainbowsea.springcloud.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;


@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageDao storageDao;


    @Override
    public void reduce(Long productId, Integer nums) {
        LOGGER.info("====================seata_storage_micro_service-10010 扣减库存 start========");
        storageDao.reduce(productId, nums);
        LOGGER.info("====================seata_storage_micro_service-10010 扣减库存 end========");

    }
}
