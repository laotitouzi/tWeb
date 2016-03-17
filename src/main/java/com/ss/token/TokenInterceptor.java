package com.ss.token;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ss.httpsession.RedisSessionManager;

public class TokenInterceptor extends HandlerInterceptorAdapter {


	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Token annotation = method.getAnnotation(Token.class);
		if (annotation != null) {
			boolean needSaveSession = annotation.save();
			if (needSaveSession) {
				HttpSession session = request.getSession();
				String tokenString = UUID.randomUUID().toString();
				session.setAttribute("token", tokenString);
			}
		}

		return true;

	}

}