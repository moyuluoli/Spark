package com.sziov.cw.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @描述:
 * @公司:
 * @作者: 王聪
 * @版本: 1.0.0
 * @日期: 2019/9/20 15:36
 */
public class ConsumerTwo {
    public static Properties props;
    static {
        props = new Properties();
        props.put("zookeeper.connect", "node1:2181/kafka");
        props.put("serializer.class", StringEncoder.class.getName());
        props.put("metadata.broker.list", "node1:9092");
        props.put("group.id", "group"); // group组的名字 （做group组区分）
        props.put("group.name", "2"); // 当前group组中的名字
        // （在相同的group组中做consumer的区分）
    }

    public static void main(String[] args) throws InterruptedException {
        String topic = "zhu1";
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1); // 取哪一个topic 取几条数据
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer
                .createMessageStreams(topicCountMap);
        final KafkaStream<byte[], byte[]> kafkaStream = messageStreams.get(
                topic).get(0);
        ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
        while (iterator.hasNext()) {
            String item = new String(iterator.next().message());
            // String msg;
            // try {
            // msg = new String(item.getBytes("gbk"),"utf-8");
            System.out.println("收到消息：" + item);
            Thread.sleep(2000);
            // } catch (UnsupportedEncodingException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
        }
    }
}
