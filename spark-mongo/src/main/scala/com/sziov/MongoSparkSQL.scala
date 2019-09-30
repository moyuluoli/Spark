package com.sziov

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Created by cw on 2017/10/8.
 * https://docs.mongodb.com/spark-connector/current/scala/datasets-and-sql/
 */
object MongoSparkSQL {

  def main(args: Array[String]): Unit = {

    /**
     * .set("spark.mongodb.input.uri", "mongodb://mongo:123568@192.168.17.10:27017/cw.logs")
     * .set("spark.mongodb.output.uri", "mongodb://mongo:123568@192.168.17.10:27017/cw.result")
     */
    val session = SparkSession.builder()
      .master("local")
      .appName("MongoSparkConnectorIntro")
      .config("spark.mongodb.input.uri", "mongodb://mongo:123568@192.168.17.10:27017/cw.logs")
      .config("spark.mongodb.output.uri", "mongodb://mongo:123568@192.168.17.10:27017/cw.result")
      .getOrCreate()

    val df: DataFrame = MongoSpark.load(session)

    df.createTempView("v_logs")

    //val result:DataFrame = session.sql("SELECT age, name FROM v_student WHERE age >= 30 ORDER BY age DESC")

    //val result = session.sql("SELECT age, name FROM v_student WHERE age is null")

//    val pv = session.sql("select count(*) from v_logs")

    val uv = session.sql("select count(*) pv, count(distinct openid) uv from v_logs")

//    pv.show()
    println("---------")
//    uv.show()

    MongoSpark.save(uv)
    //MongoSpark

    session.stop()

  }
}
