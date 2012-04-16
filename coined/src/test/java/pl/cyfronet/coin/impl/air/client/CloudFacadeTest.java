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

import java.util.Arrays;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class CloudFacadeTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
		        new String[] {"cf-test.xml",});
//		// of course, an ApplicationContext is just a BeanFactory
		BeanFactory factory = (BeanFactory) appContext;
		
		CloudFacade cf = factory.getBean("cf-client", CloudFacade.class);
		
		System.out.println(cf.getAtomicServices());
		
//		AtomicService as = new AtomicService();
//		as.setName("asTest123");
//		as.setDescription("as description");
//		as.setHttp(true);
//		
//		Endpoint e = new Endpoint();
//		e.setDescription("e1 test");
//		e.setDescriptor("<wsdl/>");
//		e.setInvocationPath("/service/path");
//		e.setPort(9090);
//		e.setServiceName("gimias");
//		
//		//as.setEndpoints(Arrays.asList(e));
//		
//		cf.createAtomicService("227", as);
		
		System.out.println(cf.getInitialConfigurations("@neurist with VNC"));
		
		
	}
}
