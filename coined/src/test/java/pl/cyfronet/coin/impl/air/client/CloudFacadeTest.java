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
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudFacadeTest {

	private static CloudFacade cf;

	public static void main(String[] args) throws Exception {
		initCloudFacde();

		printAtomicServices();

	}

	static void printAtomicServices() {
		List<AtomicService> atomicServices = cf.getAtomicServices();

		for (AtomicService atomicService : atomicServices) {
			System.out.println(atomicService);
		}
	}

	static void addInitialConfiguration(String atomicServiceId, String name)
			throws AtomicServiceNotFoundException,
			AtomicServiceInstanceNotFoundException, CloudFacadeException,
			InitialConfigurationAlreadyExistException {
		InitialConfiguration initConf = new InitialConfiguration();
		initConf.setName(name);
		initConf.setPayload("<init/>");

		System.out.println(cf
				.addInitialConfiguration(atomicServiceId, initConf));
	}

	static void addAtomicService(String atomicServiceInstanceId, String asName) {
		AtomicService atomicService = new AtomicService();
		atomicService.setName("mkAs");
		atomicService.setDescription("as description");
		atomicService.setHttp(true);
		atomicService.setPublished(true);
		atomicService.setInProxy(true);

		Endpoint e = new Endpoint();
		e.setDescription("e1 test");
		e.setDescriptor("<wsdl/>");
		e.setInvocationPath("/service/path");
		e.setPort(9090);
		e.setServiceName("gimias");

		Endpoint e2 = new Endpoint();
		e2.setDescription("e1 test");
		e2.setDescriptor("<wsdl/>");
		e2.setInvocationPath("/service/path");
		e2.setPort(9090);
		e2.setServiceName("gimias");

		atomicService.setEndpoints(Arrays.asList(e));

		System.out
				.println("atomic service id: "
						+ cf.createAtomicService(atomicServiceInstanceId,
								atomicService));
	}

	private static void initCloudFacde() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "cf-test.xml", });
		BeanFactory factory = (BeanFactory) appContext;

		cf = factory.getBean("cf-client", CloudFacade.class);
	}
}
