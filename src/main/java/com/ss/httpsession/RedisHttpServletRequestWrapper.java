package com.ss.httpsession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RedisHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private HttpServletResponse response;
	private RedisHttpSession httpSession;
	private RedisSessionManager sessionManager;
	private RequestEventSubject requestEventSubject;

	public RequestEventSubject getRequestEventSubject() {
		return requestEventSubject;
	}

	public RedisHttpServletRequestWrapper(HttpServletRequest request, HttpServletResponse response,
			RedisSessionManager sessionManager, RequestEventSubject requestEventSubject) {
		super(request);
		this.response = response;
		this.sessionManager = sessionManager;
		this.requestEventSubject = requestEventSubject;
	}

	public HttpSession getSession(boolean create) {
		if ((httpSession != null) && (!(httpSession.expired)))
			return httpSession;

		String sessionId = getRequestedSessionId();
		if (sessionId != null) {
			httpSession = sessionManager.loadSession(sessionId);
			attachSessionEvent();
			if (httpSession == null) {
				sessionManager.deleteSessionIdInCookie(this, response);
				return null;
			}
			return httpSession;
		}

		if (!create)
			return null;

		httpSession = sessionManager.createSession(this, this.response);
		attachSessionEvent();
		return this.httpSession;
	}

	private void attachSessionEvent() {
		if (httpSession != null) {
			this.sessionManager.attachEvent(httpSession, this, response, requestEventSubject);
		}
	}

	public String getRequestedSessionId() {
		Cookie[] cookies = this.getCookies();
		if (cookies == null || cookies.length == 0)
			return null;
		for (Cookie cookie : cookies) {
			if (RedisSessionManager.SESSION_ID_COOKIE.equals(cookie.getName()))
				return cookie.getValue();
		}
		return null;
	}

	public HttpSession getSession() {
		return getSession(true);
	}
}
