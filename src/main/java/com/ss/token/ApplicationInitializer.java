package com.ss.token;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

public class ApplicationInitializer implements WebApplicationInitializer{

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
	}
}
