//import org.apache.ivy.plugins.trigger.Trigger
//import org.apache.spark.sql.SparkSession
//
///**
// * @描述:
// * @公司:
// * @作者: 王聪
// * @版本: 1.0.0
// * @日期: 2019/9/16 09:33
// */
//object testSSApp extends App {
//
//  val spark: SparkSession = SparkSession.builder().appName("baidu").master("local[2]").getOrCreate()
//
//  //  结构化流
//  private val read = spark.readStream.format("kafka")
//    .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
//    .option("subscribe", "topic1")
//    .option("maxOffsetperTrigger", "1000000")
//    .option("kafkaConsumer.pollTimeoutMs", "1000")
//    .load()
//
//  //读取的kafak 数据为json格式
//
//  val result = read.selectExpr("CAST(value AS STRING)")
//    .select(
//      get_json_object(col("value"), path = "$.uri").alias("uri"),
//      get_json_object(col("value"), path = "$.market").alias("market")
//    ).groupBy(window(col("timestamp"), "5min", "1min"),
//    col("shop"))
//    .agg(count("market").alias("uv"),
//      approx_count_distinct("uri").alias("pv")).select("*")
//
//  val query = result.writeStream.trigger(Trigger.ProcessingTime(10000)).outputMode("Update")
//    .format("console").start()
//
//  //这里展示以console输出，实际中是回写到kafak或者外部存储。
//
//  query.awaitTermination()
//
//}
