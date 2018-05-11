package com.mazaiting.jms.converter;

import java.io.Serializable;

/**
 * 邮件 Bean
 * @author mazaiting
 */
public class Email implements Serializable{
	private static final long serialVersionUID = 2346710794078682832L;
	private String receiver;
	private String title;
	private String content;
	
	public Email(String receiver, String title, String content) {
		this.receiver = receiver;
		this.title = title;
		this.content = content;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Email [receiver=");
		builder.append(receiver);
		builder.append(", title=");
		builder.append(title);
		builder.append(", content=");
		builder.append(content);
		builder.append("]");
		return builder.toString();
	}	
}
