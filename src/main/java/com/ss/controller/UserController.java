package com.ss.controller;

import java.util.Map;

import javax.jms.Destination;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ss.entity.User;
import com.ss.mq.ProductService;
import com.ss.redis.RedisClientTemplate;
import com.ss.service.UserService;

@Controller
@Scope("prototype")
@RequestMapping("/user")
public class UserController {
	@Autowired
	public RedisClientTemplate redisClientTemplate;
	@Autowired
	public UserService userService;
	@Autowired
	@Qualifier("queueDestination")
	private Destination destination;
	@Autowired
	public ProductService productService;;
	
	@RequestMapping("/save")
	public String save(HttpSession session,String name, String password, Map<String, Object> map) {
		map.put("name", name);
		User user = new User();
		user.setName(name);
		userService.addUser(user);
		redisClientTemplate.set("username", name);
		session.setAttribute("hello", "woshinidie");
		productService.sendObject(destination,user);
		return "/user/success";
	}

	@RequestMapping("/list")
	public String list(Map<String, Object> map) {
		String name = redisClientTemplate.get("username");
		System.out.println(name);
		map.put("name", name);
		return "/user/success";
	}
}
