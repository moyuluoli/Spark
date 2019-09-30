//package com.sziov.cw.test;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.IntegerDeserializer;
//import org.apache.kafka.common.serialization.IntegerSerializer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.kafka.core.*;
//import org.springframework.kafka.listener.KafkaMessageListenerContainer;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.kafka.listener.config.ContainerProperties;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class PureJavaDemo {
//
//    /**
//     * 生产者配置
//     */
//    private static Map<String, Object> senderProps() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "d3.cdh.com:9092");
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return props;
//    }
//
//    /**
//     * 消费者配置
//     */
//    private static Map<String, Object> consumerProps() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "d3.cdh.com:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test1");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        return props;
//    }
//
//    /**
//     * 发送模板配置
//     */
//    private static KafkaTemplate<Integer, String> createTemplate() {
//        Map<String, Object> senderProps = senderProps();
//        ProducerFactory<Integer, String> producerFactory = new DefaultKafkaProducerFactory<>(senderProps);
//        KafkaTemplate<Integer, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
//        return kafkaTemplate;
//    }
//
//    /**
//     * 消息监听器容器配置
//     */
//    private static KafkaMessageListenerContainer<Integer, String> createContainer() {
//        Map<String, Object> consumerProps = consumerProps();
//        ConsumerFactory<Integer, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
//        ContainerProperties containerProperties = new ContainerProperties("test");
//        KafkaMessageListenerContainer<Integer, String> container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
//        return container;
//    }
//
//
//    public static void main(String[] args) throws InterruptedException {
//        String topic1 = "test"; //  主题
//
//        KafkaMessageListenerContainer container = createContainer();
//        ContainerProperties containerProperties = container.getContainerProperties();
//        containerProperties.setMessageListener(new MessageListener<Integer, String>() {
//            @Override
//            public void onMessage(ConsumerRecord<Integer, String> record) {
//                System.out.println("Received: " + record);
//            }
//        });
//        container.setBeanName("testAuto");
//
//        container.start();
//
//        KafkaTemplate<Integer, String> kafkaTemplate = createTemplate();
//        kafkaTemplate.setDefaultTopic(topic1);
//
//        kafkaTemplate.sendDefault(0, "foo");
//        kafkaTemplate.sendDefault(2, "bar");
//        kafkaTemplate.sendDefault(0, "baz");
//        kafkaTemplate.sendDefault(2, "qux");
//
//        kafkaTemplate.flush();
//        container.stop();
//
//        System.out.println("结束");
//    }
//
//}