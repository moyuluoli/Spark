//package com.sziov.cw.test;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//
//@Configuration
//public class JavaConfigurationDemo {
//
//    @KafkaListener(topics = "test")
//    public void listen(ConsumerRecord<String, String> record) {
//        System.out.println("收到消息: " + record);
//    }
//
//    @Bean
//    public CommandLineRunner commandLineRunner() {
//        return new MyRunner();
//    }
//
//    class MyRunner implements CommandLineRunner {
//
//        @Autowired
//        private KafkaTemplate<String, String> kafkaTemplate;
//
//        @Override
//        public void run(String... args) throws Exception {
//            kafkaTemplate.send("test", "foo1");
//            kafkaTemplate.send("test", "foo2");
//            kafkaTemplate.send("test", "foo3");
//            kafkaTemplate.send("test", "foo4");
//        }
//    }
//}