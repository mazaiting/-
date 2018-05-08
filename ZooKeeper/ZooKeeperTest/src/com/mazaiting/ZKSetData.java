package com.mazaiting;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * 设置数据
 * @author mazaiting
 */
public class ZKSetData {

	/**
	 * 定义静态的ZooKeeper对象
	 */
	private static ZooKeeper zooKeeper;
	/**
	 * 定义静态的ZooKeeper连接对象
	 */
	private static ZooKeeperConnection conn;
	
	/**
	 * 更新数据
	 * @param path 路径
	 * @param data 二进制数据
	 * @throws KeeperException ZooKeeper异常
	 * @throws InterruptedException 中断异常
	 */
	public static void update(String path, byte[] data) throws KeeperException, InterruptedException {
		zooKeeper.setData(path, data, zooKeeper.exists(path, true).getVersion());
	}
	
	public static void main(String[] args) {
		// 路径
		String path = "/MyFirst";
		// 二进制数据
		byte[] data = "Success".getBytes();
		try {
			// 创建连接对象
			conn = new ZooKeeperConnection();
			// 连接并获取ZooKeeper
			zooKeeper = conn.connect("127.0.0.1");
			// 更新数据
			update(path, data);
		} catch (Exception e) {
			// 打印错误信息
			System.out.println(e.getMessage());
		}
	}
}
