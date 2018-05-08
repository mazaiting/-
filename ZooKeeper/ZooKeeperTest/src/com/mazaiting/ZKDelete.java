package com.mazaiting;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * 删除路径
 * @author mazaiting
 */
public class ZKDelete {

	/**
	 * 定义静态的ZooKeeper对象
	 */
	private static ZooKeeper zooKeeper;
	/**
	 * 定义静态的ZooKeeper连接对象
	 */
	private static ZooKeeperConnection conn;
	
	/**
	 * 删除节点
	 * @param path 路径
	 * @throws InterruptedException 中断异常
	 * @throws KeeperException ZooKeeper异常
	 */
	public static void delete(String path) throws InterruptedException, KeeperException {
		zooKeeper.delete(path, zooKeeper.exists(path, true).getVersion());
	}
	
	public static void main(String[] args) {
		// 路径
		String path = "/MyFirst/first";
		try {
			// 创建ZooKeeper连接
			conn = new ZooKeeperConnection();
			// 连接
			zooKeeper = conn.connect("127.0.0.1");
			// 删除
			delete(path);
		} catch (Exception e) {
			// 打印错误信息
			System.out.println(e.getMessage());
		}
	}
}
