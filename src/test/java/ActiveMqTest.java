
import javax.jms.Destination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ss.entity.User;
import com.ss.mq.ProductService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/application_activemq.xml")
public class ActiveMqTest {

	@Autowired
	private ProductService productService;
	@Autowired
	@Qualifier("queueDestination")
	private Destination destination;

	@Test
	public void testSend() {
		for (int i = 0; i < 1; i++) {
			User u = new User();
			u.setName("Syrngg");
			productService.send(destination, ")hello,world" + i);
			productService.send(destination, u);
		}
	}

}