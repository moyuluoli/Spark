package scala

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object jdbc {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "D:\\softA\\winutils") //加载hadoop组件
    val conf = new SparkConf().setAppName("mysql").setMaster("spark://192.168.66.66:7077")
      .set("spark.executor.memory", "1g")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      //.setJars(Seq("D:\\workspace\\scala\\out\\scala.jar"))//加载远程spark
      .setJars(Array("hdfs://192.168.66.66:9000/spark-jars/ojdbc14-10.2.0.1.0.jar",
     "hdfs://192.168.66.66:9000/spark-jars/mysql-connector-java-5.1.39.jar"))
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    //操作MySQL
    val mysql = sqlContext.read.format("jdbc").option("url","jdbc:mysql://192.168.66.66:3306/test").
      option("dbtable","student").option("driver","com.mysql.jdbc.Driver").
      option("user","root").option("password","1").load()
    mysql.show()
    val mysql2= sqlContext.read.format("jdbc").options(
      Map(
       "driver" -> "com.mysql.jdbc.Driver",
        "url" -> "jdbc:mysql://192.168.66.66:3306",
        "dbtable" -> "test.student",
       "user" -> "root",
        "password" -> "1",
        "fetchsize" -> "3")).load()
    mysql2.show
    mysql.registerTempTable("student")
    mysql.sqlContext.sql("select * from student").collect().foreach(println)
    //操作ORACLE
    val oracle= sqlContext.read.format("jdbc").options(
      Map(
        "driver" -> "oracle.jdbc.driver.OracleDriver",
        "url" -> "jdbc:oracle:thin:@10.2.1.169:1521:BIT",
        "dbtable" -> "tab_lg",
        "user" -> "lxb",
        "password" -> "lxb123",
        "fetchsize" -> "3")).load()
    //oracle.show
    oracle.registerTempTable("tab_lg")
    oracle.sqlContext.sql("select * from tab_lg limit 10").collect().foreach(println)
  }
}