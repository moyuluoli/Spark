//package com.flink;
//import org.apache.flink.api.scala._
//import org.apache.flink.table.api.scala._
//import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
//import org.apache.flink.table.api.{Table, TableEnvironment}
//import scala.language.postfixOps
///**
//  * Created by  cw on 2018/11/12
//  */
//object sql_test {
//  def main(args: Array[String]): Unit = {
//    //获取执行环境
//    val env = StreamExecutionEnvironment.getExecutionEnvironment
//    //获取table
//      val tableEnv = TableEnvironment.getTableEnvironment(env)
//    //读取数据源
//    val source1 = env.readTextFile("C:/flink_data/person.txt")
//    val source2: DataStream[Person1] = source1.map(x=>{
//      val split = x.split(" ")
//      ( Person1(split(0),split(1)))
//    })
//    //将DataStream转化成Table
//    val table1 = tableEnv.fromDataStream(source2)
//    //注册表，表名为：person
//    tableEnv.registerTable("person",table1)
//    //获取表中所有信息
//    val rs: Table = tableEnv.sqlQuery("select *  from person ")
//    val stream: DataStream[String] = rs
//    //过滤获取name这一列的数据
//      .select("name")
//      //将表转化成DataStream
//      .toAppendStream[String]
//     stream.print()
//    env.execute("flinkSQL")
//  }
//}
//
///**
//  * 定义样例类封装数据
//  */
//case class  Person1(name:String ,score:String)