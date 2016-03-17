package com.ss.httpsession;

public interface SessionListener {
	/**
	 * Session失效时监听
	 * 
	 * @param session
	 */
	public void onInvalidated(RedisHttpSession session);

}
