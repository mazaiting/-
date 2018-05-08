package com.mazaiting;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 创建节点
 * @author mazaiting
 */
public class ZKCreate {
	/**
	 * 创建静态的ZooKeeper实例
	 */
	private static ZooKeeper zooKeeper;
	/**
	 * 创建静态的连接实例
	 */
	private static ZooKeeperConnection conn;
	
	/**
	 * 创建节点
	 * @param path Znode路径
	 * @param data 存储在Znode路径中的数据
	 * @throws KeeperException ZooKeeper异常
	 * @throws InterruptedException 中断异常
	 */
	public static void create(String path, byte[] data) throws KeeperException, InterruptedException {
		zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	public static void main(String[] args) {
		// znode路径
		String path = "/MyFirst";
		// 二进制数据
		byte[] data = "My First ZooKeeper app".getBytes();
		
		try {
			// 创建连接对象
			conn = new ZooKeeperConnection();
			// 连接， 并返回ZooKeeper
			zooKeeper = conn.connect("127.0.0.1");
			// 创建Znode
			create(path, data);
			// 关闭
			conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
