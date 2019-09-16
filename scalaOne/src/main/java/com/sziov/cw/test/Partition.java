package com.sziov.cw.test;
 
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

public class Partition implements Partitioner{
	private final AtomicInteger atomicInteger = new AtomicInteger(0);
	@Override
	public void configure(Map<String, ?> arg0) {
		// TODO Auto-generated method stub
		
	}
 
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
 
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();
		if (null == keyBytes || keyBytes.length<1) {
			return atomicInteger.getAndIncrement() % numPartitions;
		}
		//借用String的hashCode的计算方式
		int hash = 0;
		for (byte b : keyBytes) {
			hash = 31 * hash + b;
		}
		return hash % numPartitions;
	}
 
}