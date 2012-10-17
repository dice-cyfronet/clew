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

		//air.addKey("mk_key", "marek", "publicKey", "fingerprint");
		
		System.out.println(air.getUserKeys("marek"));
		
		try {
			System.out.println(air.getPublicKey("marek", "507e69e72a95243a9500000a"));
		} catch (Exception e) {
			System.out.println(e.getClass());
			e.printStackTrace();
			
		}
		//System.out.println(air.getPublicKey("wojtek", "507d38502a95243a95000002"));
		//air.deletePublicKey("marek", "507d36da2a9524395000000b");
	}
}
