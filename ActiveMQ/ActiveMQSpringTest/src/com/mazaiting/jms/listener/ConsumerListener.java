package com.mazaiting.jms.listener;

public class ConsumerListener {
	public void handleMessage(String message) {
		System.out.println("ConsumerListener通过handleMessage发送到一个纯文本消息，消息内容是：" + message);
	}
	
	public String receiveMessage(String message) {
		System.out.println("ConsumerListener通过receiveMessage接收到一个纯文本消息，消息内容是：" + message);
		return "这是ConsumerListener对象的receiveMessage方法的返回值。";
	}
}
