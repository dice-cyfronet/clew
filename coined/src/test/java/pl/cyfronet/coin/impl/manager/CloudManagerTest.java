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
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicServiceStatus;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowStatus;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudManagerTest extends AbstractCloudManagerTest {

	@Test
	public void shouldGetWorkflowStatus() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);
		String contextId = "contextId";
		String name = "name";
		String description = "description";
		int priority = 40;
		String username = "user";

		WorkflowDetail workflowDetail = new WorkflowDetail();
		workflowDetail.setName(name);
		workflowDetail.setDescription(description);
		workflowDetail.setPriority(priority);
		workflowDetail.setState(Status.booting);
		workflowDetail.setWorkflow_type(WorkflowType.development);
		workflowDetail.setVph_username(username);

		Vms vm1 = getVms("name1", "as1", "conf1", "sTemplate1", "1",
				Status.running);
		Vms vm2 = getVms("name2", "as1", "conf1", "sTemplate1", "2",
				Status.booting);
		Vms vm3 = getVms("name3", "as2", "conf2", "sTemplate1", "3",
				Status.stopping);

		workflowDetail.setVms(Arrays.asList(vm1, vm2, vm3));

		// when
		when(air.getWorkflow(contextId)).thenReturn(workflowDetail);
		when(air.getTypeFromConfig("conf1"))
				.thenReturn(getApplianceType("as1"));
		when(air.getTypeFromConfig("conf2"))
				.thenReturn(getApplianceType("as2"));

		WorkflowStatus status = manager.getWorkflowStatus(contextId, username);

		// then

		assertEquals(name, status.getName());
		assertEquals(2, status.getAses().size());

		AtomicServiceStatus asi1 = status.getAses().get(1);
		{
			assertEquals("as1", asi1.getId());
			assertNull(asi1.getMessage());
			assertEquals(Status.booting, asi1.getStatus());
			assertEquals(2, asi1.getInstances().size());

			assertEquals("1", asi1.getInstances().get(0).getId());
			assertEquals("name1", asi1.getInstances().get(0).getName());
			assertEquals("", asi1.getInstances().get(0).getMessage());
			assertEquals(Status.running, asi1.getInstances().get(0).getStatus());

			assertEquals("2", asi1.getInstances().get(1).getId());
			assertEquals("name2", asi1.getInstances().get(1).getName());
			assertEquals("", asi1.getInstances().get(1).getMessage());
			assertEquals(Status.booting, asi1.getInstances().get(1).getStatus());
		}

		AtomicServiceStatus asi2 = status.getAses().get(0);
		{
			assertEquals(1, asi2.getInstances().size());
			assertEquals("3", asi2.getInstances().get(0).getId());
			assertEquals("name3", asi2.getInstances().get(0).getName());
			assertEquals("", asi2.getInstances().get(0).getMessage());
			assertEquals(Status.stopping, asi2.getStatus());
			assertEquals(Status.stopping, asi2.getInstances().get(0)
					.getStatus());
		}

		verify(air, times(1)).getWorkflow(contextId);
		verify(air, times(3)).getTypeFromConfig(any(String.class));
	}
}
