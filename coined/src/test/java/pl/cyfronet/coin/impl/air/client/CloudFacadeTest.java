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

import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
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
		//addAtomicService("cyfronet-nova-vm-208", "SecHelloWorld");
		//addInitialConfiguration("SecHelloWorld", "secHelloWorldInitConf");
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

	private static void initCloudFacde() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "cf-test.xml", });
		BeanFactory factory = (BeanFactory) appContext;

		cf = factory.getBean("cf-client-local", CloudFacade.class);
	}
}
