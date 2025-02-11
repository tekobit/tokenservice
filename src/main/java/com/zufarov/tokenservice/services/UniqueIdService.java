package com.zufarov.tokenservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;


import java.util.concurrent.Executor;

@Service
public class UniqueIdService {
    @Value("${jedis.set.name}")
    private String redisSetName;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${jedis.set.minimum-number-of-elements}")
    private int minimumNumberOfIdsInCache;

    private final JedisService jedisService;

    private final ThreadPoolTaskExecutor asyncTaskExecutor ;

    @Autowired
    public UniqueIdService( JedisService jedisService, Executor asyncTaskExecutor) {
        this.jedisService = jedisService;
        this.asyncTaskExecutor = (ThreadPoolTaskExecutor) asyncTaskExecutor;
    }

    public String getNextToken() {  

// some shitty code here to get rid of situation when there is 0 id
// I guess this situation won't ever happen(
        try (JedisPool jedisPool = new JedisPool(host, port)){
            Jedis jedis =jedisPool.getResource();

            long numberOfIds = jedis.scard(redisSetName);

            if (numberOfIds==minimumNumberOfIdsInCache) {
                jedisService.loadIdsInRedis(jedis);
            }
            else if (asyncTaskExecutor.getActiveCount()!=0 && numberOfIds==0) {
                waitUntilIdsInCache();
            } else if (numberOfIds==0){
                jedisService.loadIdsInRedis(jedis);
                waitUntilIdsInCache();
            }
            return jedis.spop(redisSetName);

        }

    }

    private void waitUntilIdsInCache() {
        try (JedisPool jedisPool = new JedisPool(host, port)) {
            Jedis jedis = jedisPool.getResource();
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    unsubscribe();
                }
            }, "channel1");
        }
    }

}

