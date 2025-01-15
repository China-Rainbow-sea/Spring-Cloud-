package com.rainbowsea.springcloud.service.impl;


import com.rainbowsea.springcloud.dao.AccountDao;
import com.rainbowsea.springcloud.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    public void reduce(Long userId, Integer money) {
        LOGGER.info("===================seata_account_micro_service-10012 扣减账户余额 start=========");
        accountDao.reduce(userId, money);
        LOGGER.info("===================seata_account_micro_service-10012 扣减账户余额 end =========");
    }
}
