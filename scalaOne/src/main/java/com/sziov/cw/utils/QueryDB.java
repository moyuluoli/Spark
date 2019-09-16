package com.sziov.cw.utils;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @描述:
 * @公司:
 * @作者: 王聪
 * @版本: 1.0.0
 * @日期: 2019/9/12 16:54
 */
public class QueryDB {

    static {
        System.setProperty("hadoop.home.dir", "D:\\softA\\winutils");
    }

    //首先新建一个sparkconf定义参数
    static SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("table show");

    static JavaSparkContext sc = new JavaSparkContext(sparkConf);

    public static Dataset<Row> query(String sql){
        SQLContext sqlContext = new SQLContext(sc);
        Map<String,String> options = new HashMap<String,String>();
        options.put("url","jdbc:mysql://localhost:3306/ipproxy");
        options.put("driver","com.mysql.jdbc.Driver");
        options.put("user","root");
        options.put("password","root");
        options.put("dbtable","a");
        Dataset<Row> table_a = sqlContext.read().format("jdbc").options(options).load();

        table_a.registerTempTable("t1");

        Dataset<Row> resultDS = sqlContext.sql(sql);

        resultDS.show();
        return resultDS;
    }

}
