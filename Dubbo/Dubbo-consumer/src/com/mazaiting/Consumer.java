package com.mazaiting;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mazaiting.service.ItemService;

public class Consumer {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = 
				new ClassPathXmlApplicationContext("consumer.xml");
		context.start();
		System.out.println("consumer start");
		ItemService demoService = context.getBean(ItemService.class);
		System.out.println("consumer");
		List<String> list = demoService.getList(1L);
		for (String string : list) {
			System.out.println(string);	
		}
	}
}
