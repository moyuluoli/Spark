package scala

import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by Henry on 2019/9/11
  */
case class MovieRating1(val uid: Int, val mid: Int, val ratingasd: Double)
//推荐
case class Recommendation(rid:Int, r:Double)
// 用户的推荐
case class UserRecs(uid:Int, recs:Seq[Recommendation])
//电影的相似度
case class MovieRecs(mid:Int, recs:Seq[Recommendation])

object DemoCalc2 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("JdbcDataSource")
      .master("local[*]").getOrCreate()

    import spark.implicits._
    //load这个方法会读取真正mysql的数据吗？
    val logs = spark.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://127.0.0.1:3306/ipproxy",
        "driver" -> "com.mysql.jdbc.Driver",
        "dbtable" -> "azhong",
        "user" -> "root",
        "password" -> "root")
    ).load().as[MovieRating1].rdd.map(r=> (r.uid, r.mid, r.ratingasd))

    val logss = spark.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://127.0.0.1:3306/ipproxy",
        "driver" -> "com.mysql.jdbc.Driver",
        "dbtable" -> "a",
        "user" -> "root",
        "password" -> "root")
    ).load().as[MovieRating1].rdd.map(r=> (r.uid, r.mid, r.ratingasd))

    println("=================================")

    val userRDD = logs.map(_._1).distinct().cache()
    val movieRDD=logs.map(_._2).distinct().cache()
    val trainData=logs.map(x=>Rating(x._1,x._2,x._3))
    val (rank,iterations,lambda) = (50, 5, 0.01)
    val model = ALS.train(trainData,rank,iterations,lambda)
    val userMovies = userRDD.cartesian(movieRDD)
    val preRatings = model.predict(userMovies)
/*    val userRecs = preRatings
      .filter(_.rating > 0)
      .map(rating => (rating.user,(rating.product, rating.rating)))
      .groupByKey()
      .map{
        case (uid,recs) => UserRecs(uid,recs.toList.sortWith(_._2 > _._2).map(x => Recommendation(x._1,x._2)))
      }.toDF()*/

    val twotable = logss.map({ r =>
      val uid = r._1
      val mid = r._2
      (uid.toInt, mid.toInt)
    }).groupByKey().map {
      case (uid, mid) => (uid, mid.toList)
    }

    val userRecs = preRatings
      .filter(_.rating > 0)
      .map(rating => (rating.user,(rating.product, rating.rating)))
      .groupByKey()
      .map{r=>
        val uid=r._1
        val mids=r._2.toList.sortWith(_._2>_._2).map(_._1)
        var value: RDD[(Int, List[Int])] = twotable.filter(x => {
          !mids.contains(x._2)
        })
        (uid,value)
      }
    userRecs.foreach(println)
    println("===============")



    twotable.foreach(println)
    println("===============")


//    userRecs.createTempView("aa")
//    val aa = spark.sql("select * from aa ").show()


/*    val twotable: RDD[(Int, RDD[Int])] = logss.map({ r =>
      val uid = r._1
      val mid = r._2
      (uid.toInt, mid.toInt)
    }).groupByKey().map {
      case (uid, mid) => (uid, spark.sparkContext.parallelize(mid.toList))
    }*/







//    logss.createTempView("bb")
//    val bb= spark.sql("select * from bb").show()
//    val arr= logss.map(x=>(x.get(0),x.get(1))).collect().toMap.toArray
//    logss.map(x =>(x._1,x._2)).groupByKey()












    spark.stop()
  }

}
