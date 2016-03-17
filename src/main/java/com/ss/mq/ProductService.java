package com.ss.mq;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Repository;

import com.ss.entity.User;

@Repository
public class ProductService {
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("queueDestination")
	private Destination destination;

	public void send(Destination destination, final String message) {

		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	};
	
	public void sendObject(Destination destination, final Serializable object) {

		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(object);
			}
		});
	}

	public void send(Destination destination, final User u) {
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(u);
			}
		});
	};
}