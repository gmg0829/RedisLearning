package com.gmg.demo.redisson;

import org.redisson.config.Config;

/**
 * @author gmg
 * @title: Gurad
 * @projectName RedisLearning
 * @description: TODO
 * @date 2019/9/17 19:09
 */
public class Guard {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress(
                "redis://172.29.3.245:26378","redis://172.29.3.245:26379", "redis://172.29.3.245:26380")
                .setMasterName("mymaster")
                .setPassword("a123456").setDatabase(0);
    }
}
