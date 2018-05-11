package com.mazaiting.jms.converter.service.impl;

import java.io.Serializable;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.mazaiting.jms.converter.service.ProducerService;

@Service
public class ProducerServiceImpl implements ProducerService{

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Override
	public void sendMessage(Destination destination, Serializable obj) {
		// 未使用MessageConverter
		/*jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(obj);
			}
		});*/
		// 使用MessageConverter, JmsTemplate就会在其内部调用预定的MessageConverter对我们的消息对象进行转换，然后再进行发送。
		jmsTemplate.convertAndSend(destination, obj);
	}

}
