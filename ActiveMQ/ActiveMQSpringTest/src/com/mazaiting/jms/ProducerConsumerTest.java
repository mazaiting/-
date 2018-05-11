package com.mazaiting.jms;

import javax.jms.Destination;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mazaiting.jms.service.ProducerService;

public class ProducerConsumerTest {
	private ApplicationContext context;
	private ProducerService producerService;
	private Destination destination;
	@Before
	public void onInit() {
		context = new ClassPathXmlApplicationContext("classpath:bean.xml");
		producerService = (ProducerService) context.getBean("producerServiceImpl");
//		destination = (Destination) context.getBean("queueDestination");
//		destination = (Destination) context.getBean("sessionAwareQueue");
		destination = (Destination) context.getBean("adapterQueue");
	}
	
	@Test
	public void sendTest() {
		for (int i = 0; i < 2; i++) {
			producerService.sendMessage(destination, "你好，生产者！ 这是消息" + (i+1));
		}
	}
	
	@Test
	public void sessionAwareMessageListenerTest() {
		producerService.sendMessage(destination, "测试SessionAwareMessageListener");
	}
	
	@Test
	public void messageListenerAdapterTest() {
		producerService.sendMessage(destination, "测试MessageListenerAdapter");
	}
}
