package com.mazaiting;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 获取数据
 * @author mazaiting
 */
public class ZKGetData {
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
		// 用户停止(等待)主线程，直到客户端知道获取到数据
		final CountDownLatch connectedSignal = new CountDownLatch(1);
		try {
			// 创建连接对象
			conn = new ZooKeeperConnection();
			// 连接
			zooKeeper = conn.connect("127.0.0.1");
			// 检测是否存此路径
			Stat stat = exists(path);
			// 判断是否存在
			if (null != stat) {
				// 获取字节数组
				byte[] bytes = zooKeeper.getData(path, new Watcher() {
					
					@Override
					public void process(WatchedEvent event) {
						// 检测状态
						if (Event.EventType.None == event.getType()) {
							// 判断状态
							switch (event.getState()) {
							case Expired:
								// 关闭
								connectedSignal.countDown();
								break;

							default:
								break;
							}
						} else {
							try {
								byte[] bn = zooKeeper.getData(path, false, null);
								// 将字节数组转换为字符创
								String data = new String(bn, "UTF-8");
								// 打印字符串
								System.out.println(data);
								// 关闭
								connectedSignal.countDown();
							} catch (Exception e) {
								// 打印错误日志
								System.out.println(e.getMessage());
							}
						}
					}
				}, null);
				// 将字节数组转换为字符创
				String data = new String(bytes, "UTF-8");
				// 打印字符串
				System.out.println(data);
				// 等待
				connectedSignal.await();
			} else {
				// 节点不存在
				System.out.println("Node does not exists");
			}
		} catch (Exception e) {
			// 打印错误日志
			System.out.println(e.getMessage());
		}
	}
}
