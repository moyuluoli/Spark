// partitionsPerTopic表示topic和分区关系，key是topic，value是分区数量
// subscriptions表示订阅关系，key是消费者，value是订阅的topic
@Override
public Map<String, List<TopicPartition>> assign(Map<String, Integer> partitionsPerTopic,
                                                Map<String, Subscription> subscriptions) {
    // 得到topic和订阅的消费者集合信息，例如{t0:[c0, c1], t1:[C0, C1]}
    Map<String, List<String>> consumersPerTopic = consumersPerTopic(subscriptions);
    // 保存topic分区和订阅该topic的消费者关系结果map
    Map<String, List<TopicPartition>> assignment = new HashMap<>();
    for (String memberId : subscriptions.keySet())
        // memberId就是消费者client.id+uuid(kafka在client.id上追加的)
        assignment.put(memberId, new ArrayList<TopicPartition>());

    // 遍历每个topic和消费者集合信息组成的map（由这个遍历可知，range策略分配结果在各个topic之间互不影响）
    for (Map.Entry<String, List<String>> topicEntry : consumersPerTopic.entrySet()) {
        // topic名称
        String topic = topicEntry.getKey();
        // topic的消费者集合信息
        List<String> consumersForTopic = topicEntry.getValue();

        // 当前topic的分区数量
        Integer numPartitionsForTopic = partitionsPerTopic.get(topic);
        // 如果当天topic没有分区，那么继续遍历下一个topic
        if (numPartitionsForTopic == null)
            continue;

        // 消费者集合根据字典排序
        Collections.sort(consumersForTopic);
        // 每个topic分区数量除以消费者数量，得出每个消费者分配到的分区数量
        int numPartitionsPerConsumer = numPartitionsForTopic / consumersForTopic.size();
        // 无法整除的剩余分区数量
        int consumersWithExtraPartition = numPartitionsForTopic % consumersForTopic.size();
        // 根据topic名称和分区数量，得到分区集合信息
        List<TopicPartition> partitions = AbstractPartitionAssignor.partitions(topic, numPartitionsForTopic);
        // 遍历订阅当前topic的消费者集合
        for (int i = 0, n = consumersForTopic.size(); i < n; i++) {
            // 分配到的分区的开始位置
            int start = numPartitionsPerConsumer * i + Math.min(i, consumersWithExtraPartition);
            // 分配到的分区数量（整除分配到的分区数量，加上1个无法整除分配到的分区--如果有资格分配到这个分区的话。判断是否有资格分配到这个分区：如果整除后余数为m，那么排序后的消费者集合中前m个消费者都能分配到一个额外的分区）
            int length = numPartitionsPerConsumer + (i + 1 > consumersWithExtraPartition ? 0 : 1);
            // 给消费者分配分区
            assignment.get(consumersForTopic.get(i)).addAll(partitions.subList(start, start + length));
        }
    }
    return assignment;
}
### 总结
由上面的分析可知，range策略会把无法整除的剩余分区，分配给前面几个消费者，而且每个topic都会如此。这样的话，topic越多，前面几个消费者可能承受的压力就越大。range的弊端还是非常明显的。


// partitionsPerTopic表示topic和分区关系，key是topic，value是分区数量
// subscriptions表示订阅关系，key是消费者，value是订阅的topic信息
@Override
public Map<String, List<TopicPartition>> assign(Map<String, Integer> partitionsPerTopic,
                                                Map<String, Subscription> subscriptions) {
    Map<String, List<TopicPartition>> assignment = new HashMap<>();
    for (String memberId : subscriptions.keySet())
        assignment.put(memberId, new ArrayList<TopicPartition>());

    // 将消费者集合先按照字典排序，再构造成一个环形迭代器
    CircularIterator<String> assigner = new CircularIterator<>(Utils.sorted(subscriptions.keySet()));
    // 以topic名称排序（SortedSet<String> topics = new TreeSet<>();TreeSet保存topic名称从而实现排序），遍历topic下的分区，得到全部分区（分区主要信息包括topic名称和分区编号）
    for (TopicPartition partition : allPartitionsSorted(partitionsPerTopic, subscriptions)) {
        final String topic = partition.topic();
        // assigner.peek()得到最后一次遍历的消费者。如果遍历的当前分区所属topic不在最后一次遍历的消费者订阅的topic范围内，那么从环形迭代器中轮询选择下一个消费者，直到选择的消费者订阅的topic集合包含当前topic。
        while (!subscriptions.get(assigner.peek()).topics().contains(topic))
            assigner.next();
        // 给消费者分配分区，并轮询到下一个消费者
        assignment.get(assigner.next()).add(partition);
    }
    return assignment;
}












