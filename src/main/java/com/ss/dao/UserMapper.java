package com.ss.dao;

import java.util.List;

import com.ss.entity.Order;
import com.ss.entity.User;

public interface UserMapper {
	public void insertUser(User user);

	public User getUserById(String id);
	
	public void insertOrder(Order order);

	public  List<Order> queryOrders();
}
