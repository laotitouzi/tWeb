
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ss.redis.RedisClientTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
public class RedisTest {
	@Autowired
	private RedisClientTemplate redisClientTemplate;

	@Test
	public void testSayHello() {
//
//		String key = "hello";
//		String value = "value ";
//		String return1 = redisClientTemplate.setex(key, value);
		
	String return2 = redisClientTemplate.get("hello");
	System.out.println(return2);
	}
}