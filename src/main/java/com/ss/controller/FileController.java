package com.ss.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.ss.entity.FileEntity;
import com.ss.entity.Order;
import com.ss.service.UserService;
import com.ss.utils.CSVUtils;

@Controller
@RequestMapping("/file")
public class FileController {

	@Autowired
	public UserService userService;

	@RequestMapping("/upload")
	public String upload(FileEntity file) {
		MultipartFile mf = file.getCsvTradeFile();
		if (mf.getSize() == 0) {
			return "/upload/error";
		}
		try {
			List<Order> list = CSVUtils.process(mf.getInputStream());
			userService.addOrders(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(mf.getName());
		return "/upload/success";
	}

	@RequestMapping("/orders")
	public String queryOrders(Model model) {
		List<Order> list = userService.queryOrders();
		model.addAttribute("orders", list);
		return "/order/result";
	}
}
