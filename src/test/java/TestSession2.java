import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ss.httpsession.RedisHttpSession;
import com.ss.httpsession.RedisSessionManager;
import com.ss.httpsession.RequestEventSubject;
import com.ss.httpsession.RedisHttpServletRequestWrapper;
import com.ss.redis.RedisClientTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
public class TestSession2 {
	@Autowired
	private RedisSessionManager sessionManager;
	@Autowired
	public RedisClientTemplate redisClientTemplate;

	@Test
	public void test() {
		HttpServletRequest request1 = new MockHttpServletRequest("/", "");
		HttpServletResponse response = new MockHttpServletResponse();
		RequestEventSubject eventSubject = new RequestEventSubject();
		RedisHttpServletRequestWrapper request = new RedisHttpServletRequestWrapper(request1, response,
				this.sessionManager, eventSubject);

		RedisHttpSession session = (RedisHttpSession) request.getSession();
		String value = UUID.randomUUID().toString().replaceAll("-", "");
		session.setAttribute("hello", value);

		System.out.println("getFromSession:" + session.getAttribute("hello"));
		System.out.println("getFromRedis1:" + redisClientTemplate.get("hello"));
		session.removeAttribute("hello");
		System.out.println("getFromRedis2:" + redisClientTemplate.get("hello"));
		System.out.println("getFromSession2:" + session.getAttribute("hello"));

		session.setAttribute("hello", value + "_2");

		System.out.println("getFromSession3:" + session.getAttribute("hello"));
	}
}
