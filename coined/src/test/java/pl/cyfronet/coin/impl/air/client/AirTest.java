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

		ATEndpoint asE1 = new ATEndpoint();
		asE1.setDescription("endpoint description");
		asE1.setDescriptor("<wsdl/>");
		asE1.setInvocation_path("/gimias");
		asE1.setPort(9000);
		asE1.setService_name("gimias");
		asE1.setEndpoint_type("ws");
		//asE1.setEndpoint_type("REST");
		
		ATEndpoint asE2 = new ATEndpoint();
		asE2.setDescription("endpoint description 3");
		asE2.setDescriptor("<wsdl 2/>");
		asE2.setInvocation_path("/gimias2");
		asE2.setPort(9001);
		asE2.setService_name("gimias2");

		AddAtomicServiceRequest request = new AddAtomicServiceRequest();
		
		request.setClient("rest");
		request.setDescription("description");
		request.setHttp(true);
		request.setIn_proxy(true);
		request.setName("yyyy");
		request.setPublished(true);
		request.setScalable(true);
		request.setShared(true);
		request.setVnc(true);
		request.setEndpoints(Arrays.asList(asE1));

		// System.out.println(air.getUserWorkflows("marek"));

//		System.out.println(air.addAtomicService(request));

		// System.out.println(air.getWorkflow("4f8c38d68664883031005412"));

//		try {
//			System.out.println(air.addInitialConfiguration("ArchLinuxTestConf",
//					"euHeart Services", "<mk><init/></mk>"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(e.getClass());
//		}

		//System.out.println(air.getApplianceTypes());
		
		// try {
		// List<ApplianceType> types = air.getApplianceTypes();
		// for (ApplianceType applianceType : types) {
		// System.out.println(applianceType.getName() + " " +
		// applianceType.isHttp());
		// }
		// } catch (Exception e) {
		// System.out.println(e.getClass());
		// }
		
		//System.out.println(air.getWorkflow("4f8eafde86648824da00b15b"));
//		List<ApplianceType> types = air.getApplianceTypes();
//		
//		for (ApplianceType applianceType : types) {
//			System.out.println(applianceType);
//		}
		
		System.out.println(air.getWorkflow("4f9d0b538664884a64000635"));
	}
}
