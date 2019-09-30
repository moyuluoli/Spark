package com.cw.c01;

import com.cw.c01.handler.SpiderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class App {
    @Autowired
    private SpiderHandler spiderHandler;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

    @PostConstruct
    public void task() {
        spiderHandler.spiderData();
    }
}