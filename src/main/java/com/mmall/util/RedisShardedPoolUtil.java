package com.mmall.util;


import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisShardedPoolUtil {
    //exTime单位是秒

    /**
     * 存储值顺便设置时间
     * @param key
     * @param value
     * @param exTime
     * @return
     */
    public static String setEx(String key,String value,int exTime){
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis= RedisShardedPool.getJedis();
            result=jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} exTime{} error",key,value,exTime,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnJedisResource(jedis);
        return result;
    }

    /**
     * 设置key的有效期 单位秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        ShardedJedis jedis=null;
        Long result=null;
        try {
            jedis= RedisShardedPool.getJedis();
            result=jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("set key:{} exTime{} error",key,exTime,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnJedisResource(jedis);
        return result;
    }

    /**
     * 存储key value
     * @param key
     * @param value
     * @return
     */
    public static String set(String key,String value){
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis= RedisShardedPool.getJedis();
            result=jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnJedisResource(jedis);
        return result;
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public static String get(String key){
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis= RedisShardedPool.getJedis();
            result=jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} value:{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnJedisResource(jedis);
        return result;
    }

    /**
     * 删除key value
     * @param key
     * @return
     */
    public static Long del(String key){
        ShardedJedis jedis=null;
        Long result=null;
        try {
            jedis= RedisShardedPool.getJedis();
            result=jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} value:{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnJedisResource(jedis);
        return result;
    }
}
