package com.ouni.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class userTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void test(){

    }



}
