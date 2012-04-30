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
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetUserWorkflowsTest extends AbstractCloudManagerTest {

	private List<WorkflowBaseInfo> infos;

	@Test
	public void shouldGetRunningUserWorkflows() throws Exception {
		given2WorkingAnd1StoppedUserWorkflows();
		whenGetUserWorkflows();
		thanGetOnlyRunningWorkflows();
	}

	private void given2WorkingAnd1StoppedUserWorkflows() {
		WorkflowDetail w1 = new WorkflowDetail();
		w1.setName("w1");
		w1.setId("id1");
		w1.setState(Status.running);
		w1.setWorkflow_type(WorkflowType.development);

		WorkflowDetail w2 = new WorkflowDetail();
		w2.setName("w2");
		w2.setId("id2");
		w2.setState(Status.running);
		w2.setWorkflow_type(WorkflowType.workflow);

		WorkflowDetail w3 = new WorkflowDetail();
		w3.setState(Status.stopped);

		when(air.getUserWorkflows(username)).thenReturn(Arrays.asList(w1, w2));
	}

	private void whenGetUserWorkflows() {
		infos = manager.getWorkflows(username);
	}

	private void thanGetOnlyRunningWorkflows() {
		assertEquals(2, infos.size());
		assertEquals("id1", infos.get(0).getId());
		assertEquals("w1", infos.get(0).getName());
		assertEquals(WorkflowType.development, infos.get(0).getType());
		assertEquals("id2", infos.get(1).getId());
		assertEquals("w2", infos.get(1).getName());
		assertEquals(WorkflowType.workflow, infos.get(1).getType());
	}
}