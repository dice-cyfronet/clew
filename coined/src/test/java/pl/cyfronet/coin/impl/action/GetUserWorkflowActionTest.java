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
package pl.cyfronet.coin.impl.action;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Credential;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.PortMapping;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetUserWorkflowActionTest extends WorkflowActionTest {

	private Workflow workflow;

	private WorkflowDetail airWorkflow;

	@Test
	public void shouldGetNonExistingWorkflow() throws Exception {
		givenAirContentWithInformationAboutNonExistingWorkflow();

		try {
			whenGetWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// then this exception should be thrown - workflow does not exist.
		}

		thenAirAskedAboutWorkflow();
	}

	private void givenAirContentWithInformationAboutNonExistingWorkflow() {
		mockGetNonExistingWorkflow(air, contextId);
	}

	private void thenAirAskedAboutWorkflow() {
		verify(air, times(1)).getWorkflow(contextId);
	}

	private void whenGetWorkflow() {
		GetUserWorkflowAction action = actionFactory
				.createGetUserWorkflowAction(contextId, username);
		workflow = action.execute();

	}

	@Test
	public void shouldGetWorkflowNotBelongingToUser() throws Exception {
		givenAirWorkflowBelondingToOtherUser();
		try {
			whenGetWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// then this exception should be thrown - workflow does not belongs
			// to the user.
		}

		thenAirAskedAboutWorkflow();
	}

	private void givenAirWorkflowBelondingToOtherUser() {
		airWorkflow = new WorkflowDetail();
		airWorkflow.setVph_username("otherUser");

		when(air.getWorkflow(contextId)).thenReturn(airWorkflow);
	}

	@Test
	public void shouldGetUserWorkflow() throws Exception {
		givenWorkflowWith2Vms();
		whenGetWorkflow();
		thenCheckInformationAboutWorkflowWith2Vms();
	}

	private void thenCheckInformationAboutWorkflowWith2Vms() {
		assertNotNull(workflow);
		assertEquals(workflow.getName(), airWorkflow.getName());
		assertEquals(workflow.getType(), WorkflowType.workflow);
		assertNotNull(workflow.getAtomicServiceInstances());
		assertEquals(workflow.getAtomicServiceInstances().size(), 2);
		equals(airWorkflow.getVms().get(0), workflow
				.getAtomicServiceInstances().get(0));
		equals(airWorkflow.getVms().get(1), workflow
				.getAtomicServiceInstances().get(1));		
		
		assertNull(workflow.getAtomicServiceInstances().get(0).getCredential());
		assertNull(workflow.getAtomicServiceInstances().get(1).getCredential());
	}

	private void givenWorkflowWith2Vms() {
		airWorkflow = new WorkflowDetail();
		airWorkflow.setVph_username(username);
		airWorkflow.setName("wName");
		airWorkflow.setWorkflow_type(WorkflowType.workflow);

		Vms vm1 = new Vms();
		vm1.setAppliance_type("type1");
		vm1.setName("vm1");
		vm1.setState(Status.booting);
		vm1.setVms_id("id1");
		vm1.setConf_id("initConf1");

		Vms vm2 = new Vms();
		vm2.setAppliance_type("type2");
		vm2.setName("vm2");
		vm2.setState(Status.running);
		vm2.setVms_id("id2");
		vm2.setConf_id("initConf2");

		airWorkflow.setVms(Arrays.asList(vm1, vm2));

		when(air.getWorkflow(contextId)).thenReturn(airWorkflow);
	}

	@Test
	public void shouldGetUserWorkflowInWorkflowMode() throws Exception {
		givenWorkflowInWorkflowMode();
		whenGetWorkflow();
		thenCheckIfWorkflowDoesNotContainsVmsCredentials();
	}

	private void thenCheckIfWorkflowDoesNotContainsVmsCredentials() {
		assertNotNull(workflow);
		assertEquals(workflow.getName(), airWorkflow.getName());
		assertEquals(workflow.getType(), WorkflowType.workflow);
		assertNotNull(workflow.getAtomicServiceInstances());
		assertEquals(workflow.getAtomicServiceInstances().size(), 2);
		equals(airWorkflow.getVms().get(0), workflow
				.getAtomicServiceInstances().get(0));
		equals(airWorkflow.getVms().get(1), workflow
				.getAtomicServiceInstances().get(1));
		assertNull(workflow.getAtomicServiceInstances().get(0).getCredential());
		assertNull(workflow.getAtomicServiceInstances().get(1).getCredential());
	}

	private void givenWorkflowInWorkflowMode() {
		givenWorkflowWith2Vms();
		airWorkflow.setWorkflow_type(WorkflowType.workflow);
	}

	@Test
	public void shouldGetWorkflowInDevelopmentMode() throws Exception {
		givenWorkflowWith2VmsAnd2Redirections();
		whenGetWorkflow();
		thenCheckWorkflowCredentialsAndRedirections();		
	}

	private void givenWorkflowWith2VmsAnd2Redirections() {
		String workflowName = "wName";

		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setName(workflowName);
		wd.setWorkflow_type(WorkflowType.development);

		PortMapping sshMapping = new PortMapping();
		sshMapping.setVm_port(22);
		sshMapping.setHeadnode_port(222);
		sshMapping.setHeadnode_ip("headnodeIp");
		sshMapping.setService_name("ssh");
		sshMapping.setHttp(false);
		
		PortMapping vncMapping = new PortMapping();
		vncMapping.setVm_port(5900);
		vncMapping.setHeadnode_port(55900);
		vncMapping.setHeadnode_ip("headnodeIp");
		vncMapping.setService_name("vnc");
		vncMapping.setHttp(false);

		Vms vm1 = new Vms();
		vm1.setAppliance_type("type1");
		vm1.setName("vm1");
		vm1.setState(Status.booting);
		vm1.setVms_id("id1");
		vm1.setInternal_port_mappings(Arrays.asList(sshMapping, vncMapping));

		Vms vm2 = new Vms();
		vm2.setAppliance_type("type2");
		vm2.setName("vm2");
		vm2.setState(Status.running);
		vm2.setVms_id("id2");

		wd.setVms(Arrays.asList(vm1, vm2));
		
		when(air.getWorkflow(contextId)).thenReturn(wd);
	}
	
	private void thenCheckWorkflowCredentialsAndRedirections() {
		assertNotNull(workflow);
		assertNotNull(workflow.getAtomicServiceInstances());
		assertEquals(workflow.getAtomicServiceInstances().size(), 2);
		equals(workflow.getAtomicServiceInstances().get(0).getCredential(),
				"vm1Username", "vm1Password");
		equals(workflow.getAtomicServiceInstances().get(1).getCredential(),
				"vm2Username", "vm2Password");

		List<Redirection> vm1PortMapping = workflow.getAtomicServiceInstances()
				.get(0).getRedirections();
		List<Redirection> vm2PortMapping = workflow.getAtomicServiceInstances()
				.get(1).getRedirections();

		assertNotNull(vm1PortMapping);
		assertEquals(vm1PortMapping.get(0).getToPort().intValue(), 22);
		assertEquals(vm1PortMapping.get(0).getFromPort().intValue(), 222);
		assertEquals(vm1PortMapping.get(0).getHost(), "headnodeIp");
		assertEquals(vm1PortMapping.get(0).getName(), "ssh");

		assertEquals(vm1PortMapping.get(1).getToPort().intValue(), 5900);
		assertEquals(vm1PortMapping.get(1).getFromPort().intValue(), 55900);
		assertEquals(vm1PortMapping.get(1).getHost(), "headnodeIp");
		assertEquals(vm1PortMapping.get(1).getName(), "vnc");

		assertEquals(vm2PortMapping.size(), 0);		
	}

	private void equals(Credential credential, String username, String password) {
		assertNotNull(credential);
		assertEquals(credential.getUsername(), username);
		assertEquals(credential.getPassword(), password);
	}

	private void equals(Vms vm, AtomicServiceInstance asi) {
		assertEquals(asi.getAtomicServiceId(), vm.getAppliance_type());
		assertEquals(asi.getName(), vm.getName());
		assertEquals(asi.getStatus(), vm.getState());
		assertEquals(asi.getId(), vm.getVms_id());
		assertEquals(asi.getConfigurationId(), vm.getConf_id());		
	}
}
