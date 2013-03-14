package pl.cyfronet.coin.impl.air.client;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.cyfronet.coin.api.OwnedPayloadService;

public class SecurityPolicyClient {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "securitypolicy-client.xml", });
		BeanFactory factory = (BeanFactory) appContext;
		
		OwnedPayloadService client = factory.getBean("client-local", OwnedPayloadService.class);
		
//		client.create("mk", "my content", true);
	}
}
