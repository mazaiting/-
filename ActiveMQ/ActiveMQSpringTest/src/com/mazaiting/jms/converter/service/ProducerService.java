package com.mazaiting.jms.converter.service;

import java.io.Serializable;

import javax.jms.Destination;

public interface ProducerService {
	void sendMessage(Destination destination, Serializable obj);
}
