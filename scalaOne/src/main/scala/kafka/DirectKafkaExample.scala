//package kafka
//
//import kafka.common.TopicAndPartition
//import kafka.message.MessageAndMetadata
//import kafka.serializer.StringDecoder
//import kafka.utils.ZkUtils
//import org.I0Itec.zkclient.ZkClient
//import org.apache.spark.SparkConf
//import org.apache.spark.rdd.RDD
//import org.apache.spark.streaming.dstream.InputDStream
//import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
//import org.apache.spark.streaming.kafka010.KafkaUtils
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//
//object DirectKafkaExample {
//
//  def main(args: Array[String]) {
//
//    val ssc =  setupSsc
//    ssc.start()
//    ssc.awaitTermination()
//
//
//  }
//
//
//  def setupSsc(): StreamingContext ={
//
//    val conf = new SparkConf().setAppName("CustomDirectKafkaExample").setMaster("local[3]")
//    val kafkaParams:Map[String,String] = Map("metadata.broker.list" -> "d3.cdh.com:9092")
//    val topicsSet = Set("test01")
//    val ssc = new StreamingContext(conf, Seconds(5))
//
//    val messages = createCustomDirectKafkaStream(ssc,kafkaParams,"d3.cdh.com:2181","/mysefloffset", topicsSet).map(_._2)
//
//    messages.foreachRDD{rdd => {
//      rdd.foreachPartition { partitionOfRecords =>
//        if(partitionOfRecords.isEmpty)
//          {
//            println("此分区数据为空.")
//          }
//          else
//          {
//            partitionOfRecords.foreach(println(_))
//          }
//      }
//
//     }
//    }
//    ssc
//  }
//
//
//  def createCustomDirectKafkaStream(ssc: StreamingContext, kafkaParams: Map[String, String], zkHosts: String
//                                    , zkPath: String, topics: Set[String]): InputDStream[(String, String)] = {
//    val topic = topics.last
//    val zkClient = new ZkClient(zkHosts, 30000, 30000)
//    val storedOffsets = readOffsets(zkClient,zkHosts, zkPath, topic)
//
//    val kafkaStream = storedOffsets match {
//          case None => //最新的offset
//            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
//
//          case Some(fromOffsets) => // offset从上次继续开始
//            val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.key, mmd.message)
//            KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder,(String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
//        }
//
//    // save the offsets
//    kafkaStream.foreachRDD(rdd => saveOffsets(zkClient,zkHosts, zkPath, rdd))
//    kafkaStream
//
//  }
//
//
//
//  private def readOffsets(zkClient: ZkClient,zkHosts:String, zkPath: String, topic: String):Option[Map[TopicAndPartition, Long]] = {
//
//     println("开始读取从zk中读取offset")
//
//    val stopwatch = new Stopwatch()
//
//    val (offsetsRangesStrOpt, _) = ZkUtils.readDataMaybeNull(zkClient, zkPath)
//    offsetsRangesStrOpt match {
//      case Some(offsetsRangesStr) =>
//          println(s"读取到的offset范围: ${offsetsRangesStr}")
//        val offsets = offsetsRangesStr.split(",")
//          .map(s => s.split(":"))
//          .map { case Array(partitionStr, offsetStr) => (TopicAndPartition(topic, partitionStr.toInt) -> offsetStr.toLong) }
//          .toMap
//          println("读取offset结束: " + stopwatch)
//        Some(offsets)
//      case None =>
//          println("读取offset结束: " + stopwatch)
//        None
//    }
//  }
//
//  private def saveOffsets(zkClient: ZkClient,zkHosts:String, zkPath: String, rdd: RDD[_]): Unit = {
//    println("开始保存offset到zk中去")
//
//    val stopwatch = new Stopwatch()
//    val offsetsRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
//
//    //分区,offset
//    offsetsRanges.foreach(offsetRange => println(s"Using ${offsetRange}"))
//
//    val offsetsRangesStr = offsetsRanges.map(offsetRange => s"${offsetRange.partition}:${offsetRange.fromOffset}").mkString(",")
//     println("保存的偏移量范围:"+ offsetsRangesStr)
//    ZkUtils.updatePersistentPath(zkClient, zkPath, offsetsRangesStr)
//    println("保存结束,耗时 ：" + stopwatch)
//  }
//
//  class Stopwatch {
//    private val start = System.currentTimeMillis()
//    override def toString() = (System.currentTimeMillis() - start) + " ms"
//  }
//
//
//}