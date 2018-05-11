package com.mazaiting.jms.converter;

import javax.jms.Destination;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mazaiting.jms.converter.service.ProducerService;

public class ProducerConsumerConverterTest {
	private ApplicationContext context;
	private ProducerService producerService;
	private Destination destination;
	@Before
	public void onInit() {
		context = new ClassPathXmlApplicationContext("classpath:bean-converter.xml");
		producerService = (ProducerService) context.getBean("producerServiceImpl");
//		destination = (Destination) context.getBean("queueDestination");
		destination = (Destination) context.getBean("adapterQueue");
	}
	
	@Test
	public void objMessageTest() {
		Email email = new Email("lisi@xxx.com", "主题", "内容");
		producerService.sendMessage(destination, email);
	}
}
