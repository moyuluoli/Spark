package com.sziov.cw.test;

import java.util.*;
import java.util.stream.Collectors;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.admin.TopicCommand;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.security.JaasUtils;
import scala.collection.JavaConversions;

public class ProducerPartition {

    public static void main(String args[]) {
        ZkUtils zkUtils = ZkUtils.apply("d3.cdh.com:2181,d4.cdh.com:2181,d5.cdh.com:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
        create(zkUtils);
//        delete(zkUtils);
//        query(zkUtils);
//		List<String> allTopics = getAllTopics(zkUtils);
//		System.out.println(Arrays.asList(allTopics));

//		listAllTopic(zkUtils);


//		listTopicAllConfig(zkUtils);

//		update(zkUtils);
//		produceMsg();
    }


    /**
     * 创建主题（采用TopicCommand的方式）
     *
     * @param config String s = "--zookeeper localhost:2181 --create --topic kafka-action " +
     *               "  --partitions 3 --replication-factor 1" +
     *               "  --if-not-exists --config max.message.bytes=204800 --config flush.messages=2";
     *               <p>
     *               执行：Main.createTopic(s);
     */
    private static void create_2(String config) {
		String s = "--zookeeper localhost:2181 --create --topic kafka-action " +
				"  --partitions 3 --replication-factor 1" +
				"  --if-not-exists --config max.message.bytes=204800 --config flush.messages=2";
        String[] args = config.split(" ");
        System.out.println(Arrays.toString(args));
        TopicCommand.main(args);
    }

    /**
     * 查看所有主题
     * kafka-topics.sh --zookeeper localhost:2181 --list
     *
     * @param zkUtils
     */
    private static void listAllTopic(ZkUtils zkUtils) {
        List<String> topics = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
        topics.forEach(System.out::println);
    }

    /**
     * 得到所有topic的配置信息
     * kafka-configs.sh --zookeeper localhost:2181 --entity-type topics --describe
     *
     * @param zkUtils
     */
    private static void listTopicAllConfig(ZkUtils zkUtils) {
        try {
            Map<String, Properties> configs = JavaConversions.mapAsJavaMap(AdminUtils.fetchAllTopicConfigs(zkUtils));
            for (Map.Entry<String, Properties> entry : configs.entrySet()) {
                System.out.println("key=" + entry.getKey() + " ;value= " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
    }

    private static List<String> getAllTopics(ZkUtils zkUtils) {
        List<String> allTopicList = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
        String prefix = "t";
        String postfix = "1";
        List<String> topicList = allTopicList.stream()
                .filter(topic -> topic.startsWith(prefix) && topic.endsWith(postfix))
                .collect(Collectors.toList());
        return topicList;
    }

    /**
     * 修改主题配置
     * kafka-config --zookeeper localhost:2181 --entity-type topics --entity-name kafka-action
     * --alter --add-config max.message.bytes=202480 --alter --delete-config flush.messages
     *
     * @param zkUtils
     */
    private static void update(ZkUtils zkUtils) {
        //先取得原始的参数，然后添加新的参数同时去除需要去除的参数
        Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test01");
        // 增加topic级别属性
        props.put("min.cleanable.dirty.ratio", "0.3");
        // 删除topic级别属性
        props.remove("max.message.bytes");
        // 修改topic 'test'的属性
        AdminUtils.changeTopicConfig(zkUtils, "test01", props);
        zkUtils.close();
    }

    private static void query(ZkUtils zkUtils) {
        // 获取topic 'test01'的topic属性属性
        Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test01");
        // 查询topic-level属性
        Iterator it = props.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + " = " + value);
        }
        zkUtils.close();
    }

    /**
     * 删除某主题
     * kafka-topics.sh --zookeeper localhost:2181 --topic kafka-action --delete
     *
     * @param zkUtils
     */
    private static void delete(ZkUtils zkUtils) {
        // 删除topic 't1'
        AdminUtils.deleteTopic(zkUtils, "test01");
        zkUtils.close();
    }

	/**
	 * 创建主题
	 *     kafka-topics.sh --zookeeper localhost:2181 --create
	 *     --topic kafka-action --replication-factor 2 --partitions 3
	 * @param zkUtils
	 */
	private static void create(ZkUtils zkUtils) {
		String topic="test01";
        // 创建一个单分区单副本名为test01的topic
		if (!AdminUtils.topicExists(zkUtils,topic)){
//			AdminUtils.createTopic(zkUtils,config.getTopicName(),config.getPartitions(),
//					config.getReplication_factor(),config.getProperties(),
//					AdminUtils.createTopic$default$6());
			AdminUtils.createTopic(zkUtils, "test01", 15, 3, new Properties(), RackAwareMode.Enforced$.MODULE$);
			System.out.println("messages:successful create!");
		}
		else {
			System.out.println(topic+" is exits!");
		}

//		AdminUtils.createTopic(zkUtils, "test01", 15, 3, new Properties(), RackAwareMode.Enforced$.MODULE$);
        System.out.println("创建topic成功~~~~~~~~~~~");

        zkUtils.close();
    }


    private static void produceMsg() {
        //1.配置生产者属性
        Properties props = new Properties();
        // Kafka服务端的主机名和端口号，可以是多个
        props.put("bootstrap.servers", "d3.cdh.com:9092,d4.cdh.com:9092,d5.cdh.com:9092");
        //配置发送的消息是否等待应答
        props.put("acks", "all");
        //配置消息发送失败的重试
        props.put("retries", 0);
        // 批量处理数据的大小：16kb
        props.put("batch.size", 16384);
        // 设置批量处理数据的延迟，单位：ms
        props.put("linger.ms", 1);
        // 设置内存缓冲区的大小
        props.put("buffer.memory", 33554432);
        //数据在发送之前一定要序列化
        // key序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //设置分区
        props.put("partitioner.class", "com.sziov.cw.test.Partition");

        int num = 0;

        //2.实例化KafkaProducer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 10000; i++) {
            //3.调用Producer的send方法，进行消息的发送，每条待发送的消息，都必须封装为一个Record对象，接口回调
            producer.send(new ProducerRecord<String, String>("test01", "flink" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata arg0, Exception arg1) {
                    if (arg0 != null) {
                        System.out.println(arg0.partition() + "--" + arg0.offset());
                    }
                }
            });
        }
        //4.close释放资源
        producer.close();
    }
}