package com.mazaiting.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 定义消息的生产者
 * @author mazaiting
 */
public class Producer {
	// 用户名
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	// 密码
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	// 链接
	private static final String BROKENURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	
	/**
	 * 定义消息并发送，等待消息的接收者(消费者)消费此消息
	 * @param args
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws JMSException {
		// 消息中间件的链接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				USERNAME, PASSWORD, BROKENURL);
		// 连接
		Connection connection = null;
		// 会话
		Session session = null;
		// 消息的目的地
		Destination destination = null;
		// 消息生产者
		MessageProducer messageProducer = null;
		
		try {
			// 通过连接工厂获取链接
			connection = connectionFactory.createConnection();
			// 创建会话，进行消息的发送
			// 参数一：是否启用事务
			// 参数二：设置自动签收
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// 创建消息队列
			destination = session.createQueue("talkWithMo");
			// 创建一个消息生产者
			messageProducer = session.createProducer(destination);
			// 设置持久化/非持久化， 如果非持久化，MQ重启后可能后导致消息丢失
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 模拟发送消息
			for (int i = 0; i < 5; i++) {
				TextMessage textMessage = session.createTextMessage("给妈妈发送的消息:"+i);
				System.out.println("textMessage: " + textMessage);
				messageProducer.send(textMessage);
			}
			
			// 如果设置了事务，会话就必须提交
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






























