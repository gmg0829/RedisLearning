package com.gmg.demo.redisson;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gmg
 * @title: Standalone
 * @projectName RedisLearning
 * @description: TODO
 * @date 2019/9/17 19:03
 */
public class Standalone {
    public static void main(String[] args) {
        // 构造redisson实现分布式锁必要的Config
        Config config = new Config();
        config.useSingleServer().setAddress("redis://172.29.1.180:5379").setPassword("a123456").setDatabase(0);
        // 构造RedissonClient
        RedissonClient redissonClient = Redisson.create(config);
        // atomicLong
        RAtomicLong atomicLong = redissonClient.getAtomicLong("myAtomicLong");
        atomicLong.set(3);
        atomicLong.incrementAndGet();
        atomicLong.get();
        //topic
        RTopic topic = redissonClient.getTopic("anyTopic");
        topic.addListener(Object.class, new MessageListener<Object>() {
            @Override
            public void onMessage(CharSequence charSequence, Object o) {

            }
        });

        // in other thread or JVM
        RTopic topics = redissonClient.getTopic("anyTopic");
        long clientsReceivedMessage = topics.publish(new Object());

        //RBloomFilter
        RBloomFilter<Map<String,String>> bloomFilter = redissonClient.getBloomFilter("sample");
        // initialize bloom filter with
        // expectedInsertions = 55000000
        // falseProbability = 0.03
        bloomFilter.tryInit(55000000L, 0.03);
        bloomFilter.add(null);
        bloomFilter.add(null);
        bloomFilter.contains(null);

        // RRateLimiter
        RRateLimiter limiter = redissonClient.getRateLimiter("myLimiter");
        // Initialization required only once.
        // 5 permits per 2 seconds
        limiter.trySetRate(RateType.OVERALL, 5, 2, RateIntervalUnit.SECONDS);
        limiter.tryAcquire();



        // 设置锁定资源名称
        RLock disLock = redissonClient.getLock("DISLOCK");
        boolean isLock;
        try {
            /**
             * hash结构，key就是资源名称，field就是UUID+threadId，value就是重入值，在分布式锁时，
             * 这个值为1（Redisson还可以实现重入锁，那么这个值就取决于重入次数了）
             */
            /**
             *  $ hgetall DISLOCK
             * 1) "01a6d806-d282-4715-9bec-f51b9aa98110:1"
             * 2) "1"
             */
            //尝试获取分布式锁
            isLock = disLock.tryLock(500, 15000, TimeUnit.MILLISECONDS);
            if (isLock) {
                //TODO if get lock success, do something;
                Thread.sleep(15000);
            }
        } catch (Exception e) {
        } finally {
            // 无论如何, 最后都要解锁
            disLock.unlock();
        }
    }
}
