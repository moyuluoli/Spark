package cw.flink;

import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String args[]){
        Jedis jedis=new Jedis("127.0.0.1",7001);
        jedis.auth("123456");
        jedis.select(2);
        System.out.println("Server is running: " + jedis.ping());
        System.out.println("result:"+jedis.hgetAll("flink"));
    }
}