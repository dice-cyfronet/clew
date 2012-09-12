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

import pl.cyfronet.coin.api.WorkflowManagement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class WorkflowManagementTest {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
		        new String[] {"wm-test.xml",});
		// of course, an ApplicationContext is just a BeanFactory
		BeanFactory factory = (BeanFactory) appContext;
		
		WorkflowManagement wm = factory.getBean("wm-client", WorkflowManagement.class);
//		JSONProvider provider = factory.getBean("jsonProvider", JSONProvider.class);
		
//		System.out.println(wm.getStatus("4f4b615c86648809b50004e2"));
//		System.out.println(wm.getWorkflows());
		
		
//		WorkflowStartRequest start = new WorkflowStartRequest();
//		start.setDescription("my description");
//		start.setName("workflowName");
//		start.setPriority(55);
//		start.setType(WorkflowType.portal);
		
//		System.out.println(start);		
//		
//		System.out.println(wm.startWorkflow(start));
		
//		wm.stopWorkflow("4f730f87866488401c000191");
		
//		wm.stopWorkflow("4f4dc97a866488548d000253");
		
//		System.out.println(wm.getWorkflows());
//		wm.stopWorkflow("4f4dc75f866488548d00020f");
//		System.out.println(wm.getWorkflows());
		
//		System.out.println("info: " + wm.getStatus("4f4dc984866488548d000268"));
		
//		System.out.println(wm.getInitialConfigurations("@neurist Services"));
		
		//wm.addAtomicServiceToWorkflow("4f4dc984866488548d000268", "4f4dde29866488548d0002fa");
		//System.out.println(wm.getStatus("4f4e0bdb866488548d000f01"));
		
		System.out.println(wm.getWorkflows());
		System.out.println(wm.getWorkflow("4f8eafde86648824da00b15b"));
		
	}
}
