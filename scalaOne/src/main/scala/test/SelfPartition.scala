package test

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

/**
  *1.自定义分区器
  *2.继承自Partitioner
  *3.subjects是一个字符串数组
  *
   * @param subjects
  */
class SelfPartition (subjects :Array[String]) extends Partitioner{
 //当课程和分区之间没有定义规则时，需要自定义规则
 val rules = new mutable.HashMap[String ,Int]()
 var i = 0
  for (sub <- subjects){
    rules += (sub -> i)  //将rules逐渐添加完
    i+=1
  }

  //直接固定map
  //val rules = Map("bigdata"-> 1,"java"->2,"python"->3)//不用new 直接写Map

  //定义分区数    是个方法，而不是定义变量
  override def numPartitions: Int = {
    subjects.length+   1
  }

  //获取具体分区
  override def getPartition(key: Any): Int ={
    val k = key.toString
    rules.getOrElse(k,0)
  }
}

/**
  * 1.访问记录存储是一个URL，暂时用一个records = Array[String]来存储
  * 2.将records转换成text(一个rdd)
  * 3.对text进行操作，如：mapPartitions，map
  * 4.将操作后的结果收集并写出到控制台
  * 5.让每个学科分到各自的分区
  */


object FavoriteTeacher{
  def main (args:Array[String]): Unit ={
    val conf = new SparkConf().setAppName("FavoriteTeacher").setMaster("local")
    val sc = new SparkContext(conf)

    //存储文本
    val records: Array[String] = Array("http://bigdata.xiaoniu.com/laozhao",
      "http://bigdata.xiaoniu.com/laozhao",
      "http://bigdata.xiaoniu.com/laozhao",
      "http://bigdata.xiaoniu.com/laozhao",
      "http://bigdata.xiaoniu.com/laozhao",
      "http://java.xiaoniu.com/laozhang",
      "http://java.xiaoniu.com/laozhang",
      "http://python.xiaoniu.com/laoqian",
      "http://java.xiaoniu.com/laoli",
      "http://python.xiaoniu.com/laoli",
      "http://python.xiaoniu.com/laoli")
    val text: RDD[String] = sc.parallelize(records)//转换成rdd
    print("First disposition:")
    text.collect().foreach(println)
    //打印结果如下：http://bigdata.xiaoniu.com/laozhao
//    http://bigdata.xiaoniu.com/laozhao
//    http://bigdata.xiaoniu.com/laozhao
//    http://bigdata.xiaoniu.com/laozhao
//    http://bigdata.xiaoniu.com/laozhao
//    http://java.xiaoniu.com/laozhang
//    http://java.xiaoniu.com/laozhang
//    http://python.xiaoniu.com/laoqian
//    http://java.xiaoniu.com/laoli
//    http://python.xiaoniu.com/laoli
//    http://python.xiaoniu.com/laoli

    /*
      1.处理lines,并返回一个(String,String)元组
     */
    def fun1(lines :String ): (String, String) = {
      val url = new URL(lines)//将lines转换成URL
      val hostName = url.getHost//获取host
      val path = url.getPath//获取path
      val courseName = hostName.substring(0,hostName.indexOf("."))//获取课程名
      val teacherName = path.substring(1)//获取教师的姓名
      (courseName,teacherName)
    }
    val res1: RDD[(String, String)] = text.map(fun1)
    print("Second disposition:")
    res1.foreach(print)
    //打印结果如下：(bigdata,laozhao)(bigdata,laozhao)(bigdata,laozhao)
    // (bigdata,laozhao)(bigdata,laozhao)(java,laozhang)(java,laozhang)(python,laoqian)
    // (java,laoli)(python,laoli)(python,laoli)


    val res2: RDD[((String, String), Int)] = res1.map(x => (x,1))//形成一个map 组合
    val subjects: Array[String] = res2.map(_._1._1).distinct().collect()

    print("subjects = "+subjects)


    val res3: RDD[((String, String), Int)] = res2.reduceByKey(_+_)//根据Key将每个map合并
    print("Third disposition:")
    res3.foreach(print)

    val selfPartition = new SelfPartition(subjects)

    //按照自定义的规则分区shuffle
     val res4: RDD[(String, (String, Int))] = res3.map(t => (t._1._1, (t._1._2,t._2))).partitionBy(selfPartition)

    /*
      * 1.分区中本来就是Iterator,所以在toList之后，需要再转换成iterator
      */
    val result: RDD[(String, (String, Int))] = res4.mapPartitions(_.toList.sortBy(_._2._2).reverse.take(2).iterator)
    result.foreach(print)
  }
}