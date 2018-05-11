package com.mazaiting.cs;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;


public class Server implements MessageListener {
	// 经纪人链接
	private static final String BROKER_URL = "tcp://localhost:61616";
	// 请求队列
	private static final String REQUEST_QUEUE = "requestQueue";
	// 经纪人服务
	private BrokerService brokerService;
	// 会话
	private Session session;
	// 生产者
	private MessageProducer producer;
	// 消费者
	private MessageConsumer consumer;
	
	private void start() throws Exception {
		createBroker();
		setUpConsumer();
	}

	/**
	 * 创建经纪人
	 * @throws Exception 
	 */
	private void createBroker() throws Exception {
		// 创建经纪人服务
		brokerService = new BrokerService();
		// 设置是否持久化
		brokerService.setPersistent(false);
		// 设置是否使用JMX
		brokerService.setUseJmx(false);
		// 添加链接
		brokerService.addConnector(BROKER_URL);
		// 启动
		brokerService.start();
	}
	
	/**
	 * 设置消费者
	 * @throws JMSException 
	 */
	private void setUpConsumer() throws JMSException {
		// 创建连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
		// 创建连接
		Connection connection = connectionFactory.createConnection();
		// 启动连接
		connection.start();
		// 创建Session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建队列
		Destination adminQueue = session.createQueue(REQUEST_QUEUE);
		// 创建生产者
		producer = session.createProducer(null);
		// 设置持久化模式
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		// 创建消费者
		consumer = session.createConsumer(adminQueue);
		// 消费者设置消息监听
		consumer.setMessageListener(this);
	}

	public void stop() throws Exception {
		producer.close();
		consumer.close();
		session.close();
		brokerService.stop();
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			// 创建新消息
			TextMessage response = this.session.createTextMessage();

			// 判断消息是否是文本消息
			if (message instanceof TextMessage) {
				// 强转为文本消息 
				TextMessage textMessage = (TextMessage) message;
				// 获取消息内容
				String text = textMessage.getText();
				// 设置消息
				response.setText(handleRequest(text));
			}
			response.setJMSCorrelationID(message.getJMSCorrelationID());
			producer.send(message.getJMSReplyTo(), response);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构建消息内容
	 * @param text 文本
	 * @return
	 */
	private String handleRequest(String text) {
		return "Response to '" + text + "'";
	}
	
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		// 启动
		server.start();
		System.out.println();
		System.out.println("Press any key to stop the server");
		System.out.println();
		System.in.read();
		server.stop();
	}
}





























