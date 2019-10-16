package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool; //shared jedis连接池
    private static Integer maxTotal= Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));  //最大链接数
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));        //最大的idle状态的jedis实例的个数
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2")); //最小的idle状态的jedis实例的个数  idle：空闲状态
    private static Boolean testOnBorrow= Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true")); //在borrow一个jedis实例的时候，是否要进行验证操作，true则得到的jedis实例肯定可用
    private static Boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//在交还一个jedis实例的时候，是否要进行验证操作，true则放回jedispool的jedis实例肯定可用
    private static String redis1Ip=PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port=Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String redis2Ip=PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port=Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));
    private static void initPool(){
        JedisPoolConfig config=new JedisPoolConfig();

        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);//连接耗尽时候是否阻塞，false会抛出异常，true阻塞直到超时，默认true
        JedisShardInfo info1=new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        JedisShardInfo info2=new JedisShardInfo(redis2Ip,redis2Port,1000*2);
        List<JedisShardInfo> jedisShardInfoList=new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        pool=new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }
    static {
        initPool();
    }
    public static ShardedJedis getJedis(){
        return pool.getResource();
    }
    public static void  returnJedisResource(ShardedJedis jedis){

        pool.returnBrokenResource(jedis);

    }
    public static  void returnBrokenResource(ShardedJedis jedis){

        pool.returnResource(jedis);

    }

    public static void main(String[] args) {
        ShardedJedis jedis =pool.getResource();
        jedis.set("lqhkey","keyvalue");
        returnJedisResource(jedis);
     //   pool.destroy();//临时调用 销毁连接池仲的所有连接
        System.out.println("progarm is end");
    }
}
