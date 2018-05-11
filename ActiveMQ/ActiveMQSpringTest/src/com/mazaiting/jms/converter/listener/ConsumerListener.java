package com.mazaiting.jms.converter.listener;

import com.mazaiting.jms.converter.Email;

public class ConsumerListener {
	public void receiveMessage(String message) {
		System.out.println("ConsumerListener通过receiveMessage接收到一个纯文本消息，消息内容是：" + message);
	}
	
	public void receiveMessage(Email email) {
        System.out.println("接收到一个包含Email的ObjectMessage。");  
        System.out.println(email); 
	}
}
