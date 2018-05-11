package com.mazaiting.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 定义消息的消费者
 * @author mazaiting
 */
public class Consumer {
	// 用户名
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	// 密码
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	// 链接
	private static final String BROKENURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	
	/**
	 * 接收消息
	 * @param args
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws JMSException {
		// 消息中间件的链接工厂
		ConnectionFactory connectionFactory = null;
		// 链接
		Connection connection = null;
		// 会话
		Session session = null;
		// 消息的目的地
		Destination destination = null;
		// 消息的消费者
		MessageConsumer messageConsumer = null;
		// 实例化链接工厂，创建一个链接
		connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKENURL);
		
		try {
			// 通过工厂获取链接
			connection = connectionFactory.createConnection();
			// 启动链接
			connection.start();
			// 创建会话，进行消息的接收
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// 创建消息队列
			destination = session.createQueue("talkWithMo");
			// 创建一个消息的消费者
			messageConsumer = session.createConsumer(destination);
			
			// 模拟接收消息
			while (true) {
				TextMessage textMessage = (TextMessage) messageConsumer.receive(10000);
				if (null != textMessage) {
					System.out.println("收到消息： " + textMessage);
				} else {
					break;
				}
			}
			// 提交
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			if (null != connection) {
				connection.close();
			}
		}
	}
}
























