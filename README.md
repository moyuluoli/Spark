# Spark
### 1.foreachrdd和foreachpartition的区别
DStream是抽象类，它把连续的数据流拆成很多的小RDD数据块， 这叫做“微批次”， spark的流式处理， 都是“微批次处理”。 DStream内部实现上有批次处理时间间隔，滑动窗口等机制来保证每个微批次的时间间隔里， 数据流以RDD的形式发送给spark做进一步处理。因此， 在一个为批次的处理时间间隔里， DStream只产生一个RDD。 

可以利用dstream.foreachRDD把数据发送给外部系统。 但是想要正确地， 有效率的使用它， 必须理解一下背后的机制。通常向外部系统写数据需要一个Connection对象（通过它与外部服务器交互）。程序员可能会想当然地在spark上创建一个connection对象， 然后在spark线程里用这个对象来存RDD。

这个代码会产生执行错误， 因为rdd是分布式存储的，它是一个数据结构，它是一组指向集群数据的指针， rdd.foreach会在集群里的不同机器上创建spark工作线程， 而connection对象则不会在集群里的各个机器之间传递， 所以有些spark工作线程就会产生connection对象没有被初始化的执行错误。 解决的办法可以是在spark worker里为每一个worker创建一个connection对象， 但是如果你这么做， 程序要为每一条record创建一次connection，显然效率和性能都非常差。

另一种改进方法是为每个spark分区创建一个connection对象，同时维护一个全局的静态的连接迟对象， 这样就可以最好的复用connection。 另外需要注意： 虽然有多个connection对象， 但在同一时间只有一个connection.send(record)执行， 因为在同一个时间里， 只有 一个微批次的RDD产生出来。
