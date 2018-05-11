package com.mazaiting.jms.listener;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;

public class ConsumerSessionAwareMessageListener implements SessionAwareMessageListener<TextMessage>{
	private Destination destination;
	@Override
	public void onMessage(TextMessage message, Session session) throws JMSException {
		System.out.println("收到一条消息");
		System.out.println("消息的内容是： " + message.getText());
		// 创建消息生产者
		MessageProducer producer = session.createProducer(destination);
		// 创建文本消息
		Message textMessage = session.createTextMessage("ConsumerSessionAwareMessageListener...");
		// 发送消息
		producer.send(textMessage);
	}
	public Destination getDestination() {
		return destination;
	}
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}
