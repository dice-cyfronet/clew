/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pl.cyfronet.coin.auth;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.cyfronet.coin.auth.mi.MasterInterfaceAuthClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
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
