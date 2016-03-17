package com.ss.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ss.dao.UserMapper;
import com.ss.entity.User;

public class ConsumerMessageListener implements MessageListener {
	Log log = LogFactory.getLog(this.getClass());
	@Autowired
	UserMapper userMapper;

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public void onMessage(Message message) {
		User user = null;
		Object objMsg = null;

		if (message instanceof ObjectMessage) {

			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				objMsg = objectMessage.getObject();
			} catch (JMSException e) {
				e.printStackTrace();
			}
			if (objMsg instanceof User)
				user = (User) objMsg;

			if (user != null) {
				user.setName(user.getName() + "_queue");
				log.info("ConsumerMessageListener方法");
				//userMapper.insertUser(user);
			}
		} else if (message instanceof TextMessage) {
			TextMessage textMsg = (TextMessage) message;
			try {
				System.out.println("get " + textMsg.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

	}
}
