package com.mazaiting;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 判断某个路径是否存在
 * @author mazaiting
 */
public class ZKExists {
	/**
	 * 定义静态的ZooKeeper对象
	 */
	private static ZooKeeper zooKeeper;
	/**
	 * 定义静态的ZooKeeper连接对象
	 */
	private static ZooKeeperConnection conn;
	
	/**
	 * 判断某个路径是否存在
	 * @param path 路径
	 * @return 返回状态
	 * @throws KeeperException ZooKeeper异常
	 * @throws InterruptedException 中断异常
	 */
	public static Stat exists(String path) throws KeeperException, InterruptedException {
		return zooKeeper.exists(path, true);
	}
	
	public static void main(String[] args) {
		// znode路径
		String path = "/MyFirst";
		
		try {
			// 创建连接对象
			conn = new ZooKeeperConnection();
			// 连接， 并返回ZooKeeper
			zooKeeper = conn.connect("127.0.0.1");
			// 检测路径是否存在
			Stat stat = exists(path);
			// 判断是否为空
			if (null != stat) {
				// 存在打印其版本信息
				System.out.println("Node exists and the node version is " + stat.getVersion());
			} else {
				System.out.println("Node does not exists");
			}
			// 关闭
			conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
