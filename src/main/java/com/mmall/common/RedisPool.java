package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool pool; //jedis连接池
    private static Integer maxTotal= Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));  //最大链接数
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));        //最大的idle状态的jedis实例的个数
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2")); //最小的idle状态的jedis实例的个数  idle：空闲状态
    private static Boolean testOnBorrow= Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true")); //在borrow一个jedis实例的时候，是否要进行验证操作，true则得到的jedis实例肯定可用
    private static Boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//在交还一个jedis实例的时候，是否要进行验证操作，true则放回jedispool的jedis实例肯定可用
    private static String redisIp=PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort=Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    private static void initPool(){
        JedisPoolConfig config=new JedisPoolConfig();

        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);//连接耗尽时候是否阻塞，false会抛出异常，true阻塞直到超时，默认true
        pool=new JedisPool(config,redisIp,redisPort,1000*2);
    }
    static {
        initPool();
    }
    public static Jedis getJedis(){
        return pool.getResource();
    }
    public static void  returnJedisResource(Jedis jedis){

            pool.returnBrokenResource(jedis);

    }
    public static  void returnBrokenResource(Jedis jedis){

            pool.returnResource(jedis);

    }

    public static void main(String[] args) {
        Jedis jedis =pool.getResource();
        jedis.set("lqhkey","keyvalue");
        returnJedisResource(jedis);
        pool.destroy();//临时调用 销毁连接池仲的所有连接
        System.out.println("progarm is end");
    }
}
