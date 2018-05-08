package com.mazaiting;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 获取子节点
 * @author mazaiting
 */
public class ZKGetChildren {

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
		// 路径
		String path = "/MyFirst";
		try {
			// 创建连接对象
			conn = new ZooKeeperConnection();
			// 连接，并获取ZooKeeper对象
			zooKeeper = conn.connect("127.0.0.1");
			// 判断路径是否存在
			Stat stat = exists(path);
			// 判断是否为空
			if (null != stat) {
				// 获取孩子列表
				List<String> children = zooKeeper.getChildren(path, false);
				// 遍历孩子列表
				for(int i = 0; i < children.size(); i++) {
					// 打印数据
					System.out.println(children.get(i));
				}
			} else {
				// 路径不存在
				System.out.println("Node does not exists");
			}
		} catch (Exception e) {
			// 打印错误信息
			System.out.println(e.getMessage());
		}
	}
}
