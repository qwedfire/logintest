package org.example.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class JedisDemo1 {
    public static void main(String[] args) {
        //創建Jedis對象
        Jedis jedis = new Jedis("192.168.1.110", 6379);
        //測試
        String value = jedis.ping();
        System.out.println(value);
    }
    //操作key
    @Test
    public  void demo1(){
        Jedis jedis = new Jedis("192.168.1.110", 6379);
        //添加
        jedis.set("name","lucy");
        //獲取
        String name=jedis.get("name");
        System.out.println(name);

        //設置多個key-value
        jedis.mset("k1","v2","k2","v2");
        List<String> mget=jedis.mget("k1","k2");
        for (String s : mget) {
            System.out.println(s);
        }

        Set<String> keys=jedis.keys(("*"));
        for (String key : keys) {
            System.out.println(key);
        }
    }
    @Test
    public  void demo2(){
        Jedis jedis = new Jedis("192.168.1.110", 6379);
        jedis.lpush("key1","lucy","mary","jack");
        List<String>values=jedis.lrange("key1",0,-1);
        System.out.println(values);  //[jack, mary, lucy]

    }
    @Test
    public  void demo3(){
        //創建Jedis對象
        Jedis jedis = new Jedis("192.168.1.110", 6379);
        jedis.sadd("names","lucy","jack");
        jedis.sadd("names","ppap");
        Set<String>names=jedis.smembers("names");
        System.out.println(names);  //[ppap, jack, lucy]
    }
    @Test
    public  void demo4(){
        //創建Jedis對象
        Jedis jedis = new Jedis("192.168.1.110", 6379);
        jedis.hset("users","age","20");
        String hget=jedis.hget("users","age");
        System.out.println(hget);  //20
    }
    @Test
    public void demo5(){
        Jedis jedis = new Jedis("192.168.1.110", 6379);
        jedis.zadd("taiwan",100d,"gary");
        Set<String>taiwan=jedis.zrange("taiwan",0,-1);
        System.out.println(taiwan);  //gary
    }
}
