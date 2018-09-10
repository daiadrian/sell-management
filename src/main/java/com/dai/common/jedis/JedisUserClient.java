package com.dai.common.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;


public class JedisUserClient implements JedisClient{

    @Autowired
    private Jedis jedis;

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public String set(String key, String value) {
        jedis.select(2);
        String result = jedis.set(key, value);
        return result;
    }

    @Override
    public String get(String key) {
        jedis.select(2);
        String result = jedis.get(key);
        return result;
    }

    @Override
    public Boolean exists(String key) {
        jedis.select(2);
        Boolean result = jedis.exists(key);
        return result;
    }

    @Override
    public Long expire(String key, int seconds) {
        jedis.select(2);
        Long result = jedis.expire(key, seconds);
        return result;
    }

    @Override
    public Long ttl(String key) {
        jedis.select(2);
        Long result = jedis.ttl(key);
        return result;
    }

    @Override
    public Long incr(String key) {
        jedis.select(2);
        Long result = jedis.incr(key);
        return result;
    }

    @Override
    public Long hset(String key, String field, String value) {
        jedis.select(2);
        Long result = jedis.hset(key, field, value);
        return result;
    }

    @Override
    public String hget(String key, String field) {
        jedis.select(2);
        String result = jedis.hget(key, field);
        return result;
    }

    @Override
    public Long hdel(String key, String... field) {
        jedis.select(2);
        Long result = jedis.hdel(key, field);
        return result;
    }

    @Override
    public Boolean hexists(String key, String field) {
        jedis.select(2);
        Boolean result = jedis.hexists(key, field);
        return result;
    }

    @Override
    public List<String> hvals(String key) {
        jedis.select(2);
        List<String> result = jedis.hvals(key);
        return result;
    }

    @Override
    public Long del(String key) {
        jedis.select(2);
        Long result = jedis.del(key);
        return result;
    }
}
