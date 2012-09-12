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

package pl.cyfronet.coin.impl.air.client;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AirTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "air-test.xml", });
		// // of course, an ApplicationContext is just a BeanFactory
		BeanFactory factory = (BeanFactory) appContext;

		AirClient air = factory.getBean("air-client", AirClient.class);

//		air.uploadSecurityPolicy("test1", "roles=role_a", false);
//		
//		System.out.println("!!!!!!!!!!!!!!!!! lets try again");
//		
//		air.uploadSecurityPolicy("test1", "roles=role_a", false);
		
		air.getSecurityPolicy("nonexisting");
	}
}
