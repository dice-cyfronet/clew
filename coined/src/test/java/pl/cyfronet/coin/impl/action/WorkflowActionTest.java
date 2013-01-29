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

import static org.mockito.Mockito.when;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class WorkflowActionTest extends ActionTest {

	protected final String contextId = "contextId";
	protected final String username = "user";
	
	protected void givenWorkflowStarted() {
		givenWorkflowStarted((WorkflowType)null);
	}

	protected void givenWorkflowStarted(WorkflowType workflowType) {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(workflowType);

		givenWorkflowStarted(wd);
	}

	protected void givenWorkflowStarted(WorkflowDetail wd) {
		when(air.getWorkflow(contextId)).thenReturn(wd);
	}

	protected void mockGetNonExistingWorkflow(AirClient air, String contextId) {
		when(air.getWorkflow(contextId)).thenThrow(getAirException(404));
	}
	
	protected ApplianceType givenASInAir(String initConfId, boolean development) {
		ApplianceType at = new ApplianceType();
		at.setAuthor("marek");
		at.setName("BaseAS");
		at.setDescription("description");
		at.setDevelopment(development);
		
		when(air.getTypeFromConfig(initConfId)).thenReturn(at);
		
		return at;
	}
}
