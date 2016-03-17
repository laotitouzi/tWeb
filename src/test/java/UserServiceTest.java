import java.util.Date;

import javax.jms.Destination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ss.dao.UserMapper;
import com.ss.entity.User;
import com.ss.mq.ProductService;
import com.ss.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml") 

public class UserServiceTest {
	@Autowired 
	private UserService userService;
	@Autowired
	private ProductService productService;
	@Autowired
	UserMapper userMapper;

	@Autowired
	@Qualifier("queueDestination")
	private Destination destination;

	@Test
	public void insert() {
		User u = new User();
		u.setName("nala");
		u.setCreateDate(new Date());
		userService.addUser(u);
		
		u.setName(u.getName()+"_ma");
	userMapper.insertUser(u);

		//productService.sendObject(destination, u);
	}
}
