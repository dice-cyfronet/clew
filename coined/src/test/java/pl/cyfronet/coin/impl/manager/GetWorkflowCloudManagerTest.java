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

package pl.cyfronet.coin.impl.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Properties;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Credential;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetWorkflowCloudManagerTest extends AbstractCloudManagerTest {

	@Test
	public void shouldGetNonExistingWorkflow() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		String contextId = "nonExisting";
		String username = "username";

		// when
		mockGetNonExistingWorkflow(air, contextId);

		try {
			manager.getWorkflow(contextId, username);
			fail();
		} catch (WorkflowNotFoundException e) {
			// then this exception should be thrown - workflow does not exist.
		}

		// then
		verify(air, times(1)).getWorkflow(contextId);
	}

	@Test
	public void shouldGetWorkflowNotBelongingToUser() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		String contextId = "existing";
		String username = "username";

		WorkflowDetail w = new WorkflowDetail();
		w.setVph_username("otherUser");

		// when
		when(air.getWorkflow(contextId)).thenReturn(w);

		try {
			manager.getWorkflow(contextId, username);
			fail();
		} catch (WorkflowNotFoundException e) {
			// then this exception should be thrown - workflow does not belongs
			// to the user.
		}

		// then
		verify(air, times(1)).getWorkflow(contextId);
	}

	@Test
	public void shouldGetUserWorkflow() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		String contextId = "id";
		String username = "user";

		String workflowName = "wName";

		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setName(workflowName);
		wd.setWorkflow_type(WorkflowType.workflow);

		Vms vm1 = new Vms();
		vm1.setAppliance_type("type1");
		vm1.setName("vm1");
		vm1.setState(Status.booting);
		vm1.setVms_id("id1");

		Vms vm2 = new Vms();
		vm2.setAppliance_type("type2");
		vm2.setName("vm2");
		vm2.setState(Status.running);
		vm2.setVms_id("id2");

		wd.setVms(Arrays.asList(vm1, vm2));

		// when
		when(air.getWorkflow(contextId)).thenReturn(wd);

		Workflow workflow = manager.getWorkflow(contextId, username);

		// then
		assertNotNull(workflow);
		assertEquals(workflow.getName(), workflowName);
		assertEquals(workflow.getType(), WorkflowType.workflow);
		assertNotNull(workflow.getAtomicServiceInstances());
		assertEquals(workflow.getAtomicServiceInstances().size(), 2);
		equals(vm1, workflow.getAtomicServiceInstances().get(0));
		equals(vm2, workflow.getAtomicServiceInstances().get(1));
		assertNull(workflow.getAtomicServiceInstances().get(0).getCredential());
		assertNull(workflow.getAtomicServiceInstances().get(1).getCredential());
	}

	@Test
	public void shouldGetUserDevelopmentWorkflow() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		String contextId = "id";
		String username = "user";

		String workflowName = "wName";

		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setName(workflowName);
		wd.setWorkflow_type(WorkflowType.workflow);

		Vms vm1 = new Vms();
		vm1.setAppliance_type("type1");
		vm1.setName("vm1");
		vm1.setState(Status.booting);
		vm1.setVms_id("id1");

		Vms vm2 = new Vms();
		vm2.setAppliance_type("type2");
		vm2.setName("vm2");
		vm2.setState(Status.running);
		vm2.setVms_id("id2");

		wd.setVms(Arrays.asList(vm1, vm2));

		// when
		when(air.getWorkflow(contextId)).thenReturn(wd);

		Workflow workflow = manager.getWorkflow(contextId, username);

		// then
		assertNotNull(workflow);
		assertEquals(workflow.getName(), workflowName);
		assertEquals(workflow.getType(), WorkflowType.workflow);
		assertNotNull(workflow.getAtomicServiceInstances());
		assertEquals(workflow.getAtomicServiceInstances().size(), 2);
		equals(vm1, workflow.getAtomicServiceInstances().get(0));
		equals(vm2, workflow.getAtomicServiceInstances().get(1));
		assertNull(workflow.getAtomicServiceInstances().get(0).getCredential());
		assertNull(workflow.getAtomicServiceInstances().get(1).getCredential());
	}

	@Test
	public void shouldGetWorkflowInDevelopmentMode() throws Exception {
		// given

		Properties credentialProp = new Properties();
		credentialProp.put("type1.username", "vm1Username");
		credentialProp.put("type1.password", "vm1Password");
		credentialProp.put("type2.username", "vm2Username");
		credentialProp.put("type2.password", "vm2Password");

		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);
		manager.setCredentialProperties(credentialProp);

		String contextId = "id";
		String username = "user";

		String workflowName = "wName";

		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setName(workflowName);
		wd.setWorkflow_type(WorkflowType.development);

		Vms vm1 = new Vms();
		vm1.setAppliance_type("type1");
		vm1.setName("vm1");
		vm1.setState(Status.booting);
		vm1.setVms_id("id1");

		Vms vm2 = new Vms();
		vm2.setAppliance_type("type2");
		vm2.setName("vm2");
		vm2.setState(Status.running);
		vm2.setVms_id("id2");

		wd.setVms(Arrays.asList(vm1, vm2));

		// when
		when(air.getWorkflow(contextId)).thenReturn(wd);

		Workflow workflow = manager.getWorkflow(contextId, username);

		// then
		assertNotNull(workflow);
		assertNotNull(workflow.getAtomicServiceInstances());
		assertEquals(workflow.getAtomicServiceInstances().size(), 2);
		equals(workflow.getAtomicServiceInstances().get(0).getCredential(),
				"vm1Username", "vm1Password");
		equals(workflow.getAtomicServiceInstances().get(1).getCredential(),
				"vm2Username", "vm2Password");
	}

	/**
	 * @param cred1
	 * @param string
	 * @param string2
	 */
	private void equals(Credential credential, String username, String password) {
		assertNotNull(credential);
		assertEquals(username, credential.getUsername());
		assertEquals(password, credential.getPassword());
	}

	private void equals(Vms vm, AtomicServiceInstance asi) {
		assertEquals(vm.getAppliance_type(), asi.getAtomicServiceId());
		assertEquals(vm.getName(), asi.getName());
		assertEquals(vm.getState(), asi.getStatus());
		assertEquals(vm.getVms_id(), asi.getId());
	}
}