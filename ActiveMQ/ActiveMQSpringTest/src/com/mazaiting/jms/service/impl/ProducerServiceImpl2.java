package com.mazaiting.jms.service.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.mazaiting.jms.service.ProducerService;

@Service
public class ProducerServiceImpl2 implements ProducerService{
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("responseQueue")
	private Destination responseDestination;
	
	/**
	 * 发送消息
	 * @param destination 目的地
	 * @param message 消息
	 * @throws JmsException 
	 */
	@Override
	public void sendMessage(Destination destination, final String message) {
		System.out.println("-------------生产者发送消息---------------");
		System.out.println("-------------生产者发了一个消息：" + message);
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(message);
				textMessage.setJMSReplyTo(responseDestination);
				return textMessage;
			}
		});
	}
}
