package com.sziov.cw.test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @描述:
 * @公司:
 * @作者: 王聪
 * @版本: 1.0.0
 * @日期: 2019/9/29 08:56
 */
public class SparkDemo01 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();

        conf.setMaster("local").setAppName("CacheTest");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaRDD<String> lines = jsc.textFile("D:\\BigData\\scalaDemo\\scalaOne\\src\\main\\resources\\t.sql");


        lines = lines.cache();

        long startTime = System.currentTimeMillis();

        long count = lines.count();//count是action算子，到这里才能触发cache执行，所以这一次count加载是从磁盘读数据，然后拉回到driver端。

        long endTime = System.currentTimeMillis();

        System.out.println("共"+count+ "条数据，"+"初始化时间+cache时间+计算时间="+

                (endTime-startTime));



        long countStartTime = System.currentTimeMillis();

        long countrResult = lines.count();//这一次是从内存种中读数据

        long countEndTime = System.currentTimeMillis();

        System.out.println("共"+countrResult+ "条数据，"+"计算时间="+ (countEndTime-

                countStartTime));



        jsc.stop();
    }
}
