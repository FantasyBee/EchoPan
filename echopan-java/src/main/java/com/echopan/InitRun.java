package com.echopan;

import com.echopan.component.RedisComponent;
import com.echopan.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Component("initRun")
public class InitRun implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisComponent redisComponent;

    @Override
    public void run(ApplicationArguments args) {
        try {
            logger.info("测试数据库连接...");
            dataSource.getConnection();
            logger.info("数据库连接成功");

            logger.info("测试 Redis 连接...");
            redisComponent.getSysSettingsDto();
            logger.info("Redis 连接成功");

            logger.info("服务启动成功，可以开始愉快的开发了");
        } catch (Exception e) {
            logger.error("数据库或者 Redis 设置失败，请检查配置", e);
            throw new BusinessException("服务启动失败: " + e.getMessage());
        }
    }

}
