package cw.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class FilnkCostKafka {
    public static void main(String[] args) throws Exception {
        //1.创建一个Flink上下文
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(1000);

        //2.准备kafka参数
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "d3.cdh.com:9092,d4.cdh.com:9092,d5.cdh.com:9092");
        properties.setProperty("zookeeper.connect", "d3.cdh.com:2181");
        properties.setProperty("group.id", "qb_test_cw_01");

        FlinkKafkaConsumer09<String> myConsumer = new FlinkKafkaConsumer09<String>("tbox_period_30s", new SimpleStringSchema(), properties);

        DataStream<String> stream = env.addSource(myConsumer);
        DataStream<Tuple2<String, Integer>> counts = stream.flatMap(new LineSplitter()).keyBy(0).sum(1);

        //实例化Flink和Redis关联类FlinkJedisPoolConfig，设置Redis端口
        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").setPort(7001).setPassword("123456").setDatabase(2).build();
        //实例化RedisSink，并通过flink的addSink的方式将flink计算的结果插入到redis
        counts.addSink(new RedisSink<Tuple2<String, Integer>>(conf,new RedisExampleMapper()));
        env.execute("WordCount from Kafka data");
    }

    public static final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        private static final long serialVersionUID = 1L;

        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            String[] tokens = value.toLowerCase().split("\\W+");
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<String, Integer>(token, 1));
                }
            }
        }
    }

    //指定Redis key并将flink数据类型映射到Redis数据类型
    public static final class RedisExampleMapper implements RedisMapper<Tuple2<String, Integer>> {
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET, "flink");
        }

        public String getKeyFromData(Tuple2<String, Integer> data) {
            return data.f0;
        }

        public String getValueFromData(Tuple2<String, Integer> data) {
            return data.f1.toString();
        }
    }
}