package pl.cyfronet.coin.auth;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MasterInterfaceAuthClientTest {

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "validatetkt-test.xml", });
		// // of course, an ApplicationContext is just a BeanFactory
		BeanFactory factory = (BeanFactory) appContext;

		MasterInterfaceAuthClient auth = factory.getBean("validatetkt-client",
				MasterInterfaceAuthClient.class);

		AuthService authService = new AuthService();
		authService.setAuthClient(auth);
		
		String ticket = "ZDA3YTZhNzJkOGJhNWI2ZjgxYWRhYTA0Y2IyNWIxODA1MDMzZjBhZm1hcmVrIW1hcmVrLE1hcmVrIEthc3p0ZWxuaWssbS5rYXN6dGVsbmlrQGN5ZnJvbmV0LnBsLCxQT0xBTkQsMzA5NTA=";
		
		//ticket = "wrong" + ticket;
		
		System.out.println(authService.getUserDetails(ticket));
		System.out.println(authService.getUserDetails(ticket));
		
		Thread.sleep(5000);
		
		authService.run();
		System.out.println(authService.getUserDetails(ticket));
	}
}
