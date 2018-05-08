package com.mazaiting;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * ZooKeeper连接管理
 * @author mazaiting
 */
public class ZooKeeperConnection {

	/**
	 * 定义ZooKeeper实例
	 */
	private ZooKeeper zooKeeper;
	/**
	 * 用户停止(等待)主线程，直到客户端与ZooKeeper集合连接
	 */
	private final CountDownLatch connectedSignal = new CountDownLatch(1);
	
	/**
	 * 连接
	 * @param host 主机地址
	 * @return ZooKeeper对象
	 * @throws IOException IO异常
	 * @throws InterruptedException 中断异常
	 */
	public ZooKeeper connect(String host) throws IOException, InterruptedException {
		// 创建ZooKeeper对象
		zooKeeper = new ZooKeeper(host, 5000, new Watcher(){
			/**
			 * 
			 */
			@Override
			public void process(WatchedEvent event) {
				if (event.getState() == KeeperState.SyncConnected) {
					connectedSignal.countDown();
				}
			}
		});
		// 等待
		connectedSignal.await();
		return zooKeeper;
	}
	
	/**
	 * 关机ZooKeeper的连接
	 * @throws InterruptedException 中断异常
	 */
	public void close() throws InterruptedException {
		zooKeeper.close();
	}
}
