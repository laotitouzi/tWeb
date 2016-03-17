package com.ss.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.dao.UserMapper;
import com.ss.entity.Order;
import com.ss.entity.User;

@Service
public class UserService {
	Log log = LogFactory.getLog(this.getClass());

	@Autowired
	UserMapper userMapper;

	@Transactional
	public void addUser(User u) {
		log.info("add User:" + u.getName());
		userMapper.insertUser(u);
	}

	@Transactional
	public void addOrders(List<Order> list) {
		for (Order o : list) {
			userMapper.insertOrder(o);
		}
	}
	
	@Transactional
	public List<Order> queryOrders() {
		return	userMapper.queryOrders();
	}
}
