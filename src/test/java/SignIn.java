import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qait.happyhours.domain.User;


public class SignIn {

	public static void main(String[] args) throws JAXBException, IOException {
		
		String url = "http://localhost:8081/happyhours/rest/happy-hours-service/sign-in";
		
		User user = new User();
		user.setEmail("amit.cdacool@gmail.com");
		user.setPassword("1234");
		user.setUserName("amitmalik");
		
		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(user);
		System.out.println(content);

		TestUtil.sendRequest(url, content, "POST");
		
	}




}
