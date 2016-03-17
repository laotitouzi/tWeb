package com.ss.token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenValidInterceptor extends HandlerInterceptorAdapter {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean flag = isRepeatSubmit(request);
		if (flag) {
			response.sendRedirect(request.getContextPath() + "/repet_submit");
			return false;
		}

		return !flag;

	}

	private boolean isRepeatSubmit(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return true;
		String serverToken = (String) session.getAttribute("token");
		if (serverToken == null) {
			return true;
		}
		String clinetToken = request.getParameter("token");
		if (clinetToken == null) {
			return true;
		}
		if (serverToken.equals(clinetToken)) {
			session.removeAttribute("token");
			return false;
		} else {
			return true;
		}
	}
}
