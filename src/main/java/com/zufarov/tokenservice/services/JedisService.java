package com.zufarov.tokenservice.services;

import com.zufarov.tokenservice.repositories.UniqueTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import com.zufarov.tokenservice.util.Base10ToBase64;


import java.util.List;


@Service
public class JedisService {
    UniqueTokenRepository uniqueTokenRepository;

    @Autowired
    public JedisService(UniqueTokenRepository uniqueTokenRepository) {
        this.uniqueTokenRepository = uniqueTokenRepository;
    }

    @Value("${jedis.set.name}")
    String redisSetName;

    @Value("${jedis.set.amount-of-ids-to-load}")
    int amountOfIdsToLoad;

    @Async("asyncTaskExecutor")
    public void loadIdsInRedis(Jedis jedis) {

        List<Long> tokens = uniqueTokenRepository.getNextSequenceValue(amountOfIdsToLoad);
            for (long i: tokens) {
                jedis.sadd(redisSetName,Base10ToBase64.toBase64(i));
            }

        jedis.publish("channel1", "ids have been successfully loaded");
    }



}
