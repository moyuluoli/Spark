package com.sziov.cw.test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;

public class mysqlToDataFrame {
    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "D:\\softA\\winutils");
        //首先新建一个sparkconf定义参数
        SparkConf conf = new SparkConf().setMaster("local").setAppName("JDBCDataSource");
        //创建sparkContext，是通往spark集群的唯一通道
        JavaSparkContext sc = new JavaSparkContext(conf);
        //新建一个sparksql
        SQLContext sqlContext = new SQLContext(sc);
        //sparksql连接mysql
        /*
        * 方法1：分别将两张表中的数据加载为DataFrame
        * */
        /*Map<String,String> options = new HashMap<String,String>();
        options.put("url","jdbc:mysql://localhost:3306/tset");
        options.put("driver","com.mysql.jdbc.Driver");
        options.put("user","root");
        options.put("password","admin");
        options.put("dbtable","information");
        Dataset myinfromation = sqlContext.read().format("jdbc").options(options).load();
        //如果需要多张表，则需要再put一遍
        options.put("dbtable","score");
        Dataset scores = sqlContext.read().format("jdbc").options(options).load();*/

        //方法2：分别将mysql中两张表的数据加载为DataFrame
        DataFrameReader reader = sqlContext.read().format("jdbc");
        reader.option("url","jdbc:mysql://127.0.0.1:3306/ipproxy?serverTimezone=GMT");
        reader.option("driver","com.mysql.jdbc.Driver");
//        reader.option("driver","com.mysql.cj.jdbc.Driver");
        reader.option("user","root");
        reader.option("password","root");
        reader.option("dbtable","azhong");
        Dataset myinformation = reader.load();
        reader.option("dbtable","a");
        Dataset scores = reader.load();

        //将两个DataFrame转换为javapairrdd，执行join操作
        myinformation.registerTempTable("t1");
        scores.registerTempTable("t2");

        //定义sql语句
        String sql = "select * from t1";

        Dataset sql2 = sqlContext.sql(sql);
        sql2.show();

    }
}