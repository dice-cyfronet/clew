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

import pl.cyfronet.coin.api.KeyManagement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class KeyManagementTest {
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "key-test.xml", });
		// // of course, an ApplicationContext is just a BeanFactory
		BeanFactory factory = (BeanFactory) appContext;

		KeyManagement keyManagement = factory.getBean(KeyManagement.class);
		
		System.out.println(keyManagement.list());
		
		//keyManagement.delete("507e6a142a95243a9500000c");
		//keyManagement.add("my", "ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAxsjCvugOth17LeJN689uhVZ0kPew5tLDAtQA/PnFB+z8QdpwYTw9iUzxQ1JRua3HfBmDWF43Lwh+U3HK5yRAza2xw+Yh4qeYihQBnx9xXQypTH2I1jbsIBrIkBErAq18E2CM77Ub0SsIDTtUTkWTqYyC75zeXUIIut6Tv1mWiwd85KvmdaEaWPLZNpk0s+BTfGWHd6/0M4M/OCbKpdWvqfpnObDawzuP36a4NQ81riNLG8Nt7sFPqwXWf0xIBuTAZEQAQvICepSYNGJgGllrxY0wB6POc1OZ5JbpeJ3SJ9ol0gBFU/skr/Gicf5FCi6bU1EAr/jKQAi4weJqbRU0/w== marek@leeloo");
		//keyManagement.delete("509955172a95243b420000ce");
	}
}
