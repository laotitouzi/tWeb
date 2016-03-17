package com.ss.httpsession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.ss.redis.RedisClientTemplate;

public class RedisSessionManager {
	public static final String SESSION_ID_COOKIE = "RSESSIONID";

	@Autowired
	private RedisClientTemplate redisClientTemplate;

	private int expirationUpdateInterval;
	private int sessionTimeOut;

	public RedisHttpSession createSession(RedisHttpServletRequestWrapper request, HttpServletResponse response) {
		RedisHttpSession session = new RedisHttpSession();
		session.creationTime = System.currentTimeMillis();
		session.isNew = true;
		saveCookie(session, request, response);
		return session;
	}

	public void saveSession(RedisHttpSession session) {
		String sessionKey = null;
		try {
			sessionKey = session.getId();

			if (session.expired) {
				redisClientTemplate.deleteSession(sessionKey);
			} else {
				redisClientTemplate.putSession(sessionKey, session, this.sessionTimeOut);

			}
		} catch (Exception e) {
			throw new SessionException(e);
		}
	}

	public RedisHttpSession loadSession(String sessionId) {
		RedisHttpSession session = null;
		try {
			session = redisClientTemplate.getSession(sessionId);
			if (session != null) {
				session.isNew = false;
				session.isDirty = false;
			}
			return session;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void attachEvent(final RedisHttpSession session, final HttpServletRequestWrapper request,
			final HttpServletResponse response, RequestEventSubject requestEventSubject) {
		session.setListener(new SessionListener() {
			public void onInvalidated(RedisHttpSession session) {
				saveCookie(session, request, response);
				saveSession(session);
			}
		});
		requestEventSubject.attach(new RequestEventObserver() {
			public void completed(HttpServletRequest request, HttpServletResponse response) {
				RedisHttpServletRequestWrapper req = (RedisHttpServletRequestWrapper) request;
				RedisHttpSession session = (RedisHttpSession) req.getSession();
				int updateInterval = (int) ((System.currentTimeMillis() - session.lastAccessedTime) / 1000);
				if (session.isNew == false && session.isDirty == false && updateInterval < expirationUpdateInterval)
					return;
				if (session.expired)
					return;
				session.lastAccessedTime = System.currentTimeMillis();
				saveSession(session);
			}
		});
	}

	private void saveCookie(RedisHttpSession session, HttpServletRequestWrapper request, HttpServletResponse response) {
		if (session.isNew == false && session.expired == false)
			return;

		Cookie cookie = new Cookie(SESSION_ID_COOKIE, null);
		cookie.setPath(request.getContextPath());
		if (session.expired) {
			cookie.setMaxAge(0);
		} else if (session.isNew) {
			cookie.setValue(session.getId());
		}
		response.addCookie(cookie);
	}

	public void deleteSessionIdInCookie(HttpServletRequestWrapper request, HttpServletResponse response) {
		Cookie cookie = new Cookie(SESSION_ID_COOKIE, null);
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
	}

	public void setExpirationUpdateInterval(int expirationUpdateInterval) {
		this.expirationUpdateInterval = expirationUpdateInterval;
	}

	public void setMaxInactiveInterval(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

	public void setRedisClientTemplate(RedisClientTemplate redisClientTemplate) {
		this.redisClientTemplate = redisClientTemplate;
	}

	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

}