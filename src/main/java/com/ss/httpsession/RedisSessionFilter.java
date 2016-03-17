package com.ss.httpsession;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RedisSessionFilter implements Filter {
	Log log = LogFactory.getLog(this.getClass());
	public static final String[] IGNORE_SUFFIX = { ".png", ".jpg", ".jpeg", ".gif", ".css", ".js", ".html", ".htm",
			"swf" };
	private RedisSessionManager sessionManager;

	public static final String SESSION_MANAGER = "sessionManager";

	public void init(FilterConfig filterConfig) throws ServletException {
		String sessionManagerBeanName = filterConfig.getInitParameter("sessionManager");

		if (StringUtils.isBlank(sessionManagerBeanName)) {
			sessionManagerBeanName = "sessionManager";
		}
		ServletContext servletContext = null;
		servletContext = filterConfig.getServletContext();
		WebApplicationContext wac = null;
		wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		sessionManager = (RedisSessionManager) wac.getBean(sessionManagerBeanName);
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		log.info("doFilter start");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		RequestEventSubject eventSubject = new RequestEventSubject();
		RedisHttpServletRequestWrapper requestWrapper = new RedisHttpServletRequestWrapper(request, response,
				this.sessionManager, eventSubject);
		if (!ifFilter(request)) {
			filterChain.doFilter(requestWrapper, response);
			return;
		}
		try {
			filterChain.doFilter(requestWrapper, response);
		} finally {
			log.info("finally");
			eventSubject.completed(requestWrapper, response);
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	private boolean ifFilter(HttpServletRequest request) {
		String uri = request.getRequestURI().toLowerCase();
		for (String suffix : IGNORE_SUFFIX) {
			if (uri.endsWith(suffix))
				return false;
		}
		return true;
	}

	public void destroy() {
	}
}