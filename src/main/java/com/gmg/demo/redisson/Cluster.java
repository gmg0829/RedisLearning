package com.gmg.demo.redisson;

import org.redisson.config.Config;

/**
 * @author gmg
 * @title: Cluster
 * @projectName RedisLearning
 * @description: TODO
 * @date 2019/9/17 19:10
 */
public class Cluster {
    public static void main(String[] args) {
        Config config = new Config();
        config.useClusterServers().addNodeAddress(
                "redis://172.29.3.245:6375","redis://172.29.3.245:6376", "redis://172.29.3.245:6377",
                "redis://172.29.3.245:6378","redis://172.29.3.245:6379", "redis://172.29.3.245:6380")
                .setPassword("a123456").setScanInterval(5000);
    }
}
