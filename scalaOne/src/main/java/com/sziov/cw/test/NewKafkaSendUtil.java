package com.sziov.cw.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by cw
 */
public class NewKafkaSendUtil implements Serializable {

    public void init() {
        InputStream path = this.getClass().getResourceAsStream("/kafka.properties");
        BufferedReader reader = new BufferedReader(new InputStreamReader(path));
    }

    public void getKey() {
        ResourceBundle bundle = ResourceBundle.getBundle("kafka.properties");
        String driver = bundle.getString("driver");
        String url = bundle.getString("url");
        String username = bundle.getString("username");
        String password = bundle.getString("password");

    }

    public static void main(String[] args) {
        String brokerList = "d3.cdh.com:9092,d4.cdh.com:9092,d5.cdh.com:9092";
        String topic = "test01";
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            datas.add(i+"");
        }
        System.out.println("开始发送消息-----------------------");
        sendMsg(brokerList,topic,datas);
        System.out.println("发送消息成功！");
    }

    public static void sendMsg(String brokerList, String topic, List<String> datas) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerList);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = null;
        try {
            producer = new KafkaProducer(properties);
            for (int i = 0; i < datas.size(); i++) {
                ProducerRecord<String, String> message = new ProducerRecord<String, String>(topic, datas.get(i));
                producer.send(message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
}