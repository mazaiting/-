package com.mazaiting.jms.converter.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.support.converter.MessageConverter;

import com.mazaiting.jms.converter.Email;

public class ConsumerMessageListener implements MessageListener{
	/**
	 * 消息转换器
	 */
	@Autowired
	@Qualifier("emailMessageConverter")
	private MessageConverter messageConverter;
	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
//			ObjectMessage objMessage = (ObjectMessage) message;
			try {
//				Object obj = objMessage.getObject();
//				Email email = (Email) obj;
				Email email = (Email) messageConverter.fromMessage(message);
				System.out.println("接收到一个ObjectMessage,包含Email对象");
				System.out.println(email.toString());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
