package com.ss.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component 
@Aspect
public class AopLogger {
	Log log = LogFactory.getLog(this.getClass());

	@Before("execution(* com.ss.service.*.*(..))")
	public void preLog() {
		System.out.println("pre check");
		log.info("pre Logger");
	}

	
	@After("execution(* com.ss.service.*.*(..))")
	public void afterLog() {
		System.out.println("after check");

		log.info("after Logger");
	}
}
