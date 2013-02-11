package pl.cyfronet.coin.impl.air.client;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.cyfronet.coin.api.SecurityPolicyService;

public class SecurityPolicyClient {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "securitypolicy-client.xml", });
		BeanFactory factory = (BeanFactory) appContext;
		
		SecurityPolicyService client = factory.getBean("client-local", SecurityPolicyService.class);
		
		client.updateSecurityPolicy("mk", "my content", true);
	}
}
