package com.mazaiting.cs;

import java.util.UUID;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Client implements MessageListener {
	// 经纪人链接
	private static final String BROKER_URL = "tcp://localhost:61616";
	// 请求队列
	private static final String REQUEST_QUEUE = "requestQueue";
	// 连接
	private Connection connection;
	// 会话
	private Session session;
	// 生产者
	private MessageProducer producer;
	// 消费者
	private MessageConsumer consumer;
	// 请求队列
	private Queue tempDest;
	
	public void start() throws JMSException {
		// 连接工厂
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
		// 创建连接
		connection = activeMQConnectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建会话
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建队列
		Destination adminQueue = session.createQueue(REQUEST_QUEUE);
		// 创建生产者
		producer = session.createProducer(adminQueue);
		// 设置持久化模式
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		// 创建模板队列
		tempDest = session.createTemporaryQueue();
		// 创建消费者
		consumer = session.createConsumer(tempDest);
		// 设置消息监听
		consumer.setMessageListener(this);		
	}
	
	/**
	 * 停止
	 * @throws JMSException 
	 */
	public void stop() throws JMSException {
		producer.close();
		consumer.close();
		session.close();
	}
	
	/**
	 * 请求
	 * @param request
	 * @throws JMSException 
	 */
	public void request(String request) throws JMSException {
		System.out.println("Request: " + request);
		// 创建文本消息
		TextMessage textMessage = session.createTextMessage();
		// 设置文本内容
		textMessage.setText(request);
		// 设置回复
		textMessage.setJMSReplyTo(tempDest);
		// 获取UUID
		String correlationId = UUID.randomUUID().toString();
		// 设置JMS id
		textMessage.setJMSCorrelationID(correlationId);
		// 发送消息
		this.producer.send(textMessage);
	}

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("Received response for: " + ((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws JMSException, InterruptedException {
		Client client = new Client();
		// 启动
		client.start();
		int i = 0;
		while(i++ < 10) {
			client.request("REQUEST- " + i);
		}
		Thread.sleep(3000);
		client.stop();
	}
}































