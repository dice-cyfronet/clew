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
package pl.cyfronet.coin.impl.action.workflow;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.mock.atmosphere.ManagerResponseTestImpl;
import pl.cyfronet.coin.impl.mock.matcher.AddRequiredAppliancesRequestMatcher;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class WorkflowActionTest extends ActionTest {

	protected final String contextId = "contextId";
	protected String username = "user";
	protected final String initConfigId = "asId";
	protected final String initConfigPayload = "initConfigPayload";
	protected final String newConfigId = "newInitConf";
	protected final String keyId = "myKey";
	protected WorkflowDetail workflowDetails;
	
	protected AddRequiredAppliancesRequestMatcher matcher;

	protected void givenWorkflowStarted() {
		givenWorkflowStarted((WorkflowType) null);
	}

	protected void givenWorkflowStarted(WorkflowType workflowType) {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(workflowType);

		givenWorkflowStarted(wd);
	}

	protected void givenWorkflowStarted(WorkflowDetail wd) {
		workflowDetails = wd;
		when(air.getWorkflow(contextId)).thenReturn(wd);
	}

	protected void mockGetNonExistingWorkflow(AirClient air, String contextId) {
		when(air.getWorkflow(contextId)).thenThrow(getAirException(404));
	}

	protected ApplianceType givenASInAir(String initConfId, boolean development) {
		ApplianceType at = new ApplianceType();
		at.setAuthor("marek");
		at.setName("BaseAS name");
		at.setId("BaseAS");
		at.setDescription("description");
		at.setDevelopment(development);

		when(air.getTypeFromConfig(initConfId)).thenReturn(at);
		when(air.getApplianceConfig(initConfId)).thenReturn(initConfigPayload);

		return at;
	}

	protected void givenAsiRequestMatcher(WorkflowType workflowType) {
		givenAsiRequestMatcher(workflowType, OperationStatus.SUCCESSFUL);
	}

	protected void givenAsiRequestMatcher(WorkflowType workflowType,
			OperationStatus operationStatus, String... configIds) {
		if (workflowType == WorkflowType.development) {
			matcher = new AddRequiredAppliancesRequestMatcher(contextId,
					defaultPriority, username, WorkflowType.development,
					getConfigs(configIds, newConfigId));
			matcher.setGivenKeyId(keyId);
		} else {
			matcher = new AddRequiredAppliancesRequestMatcher(contextId, 
					defaultPriority, username, workflowType, getConfigs(
							configIds, initConfigId));
		}

		when(atmosphere.addRequiredAppliances(argThat(matcher))).thenReturn(
				new ManagerResponseTestImpl(operationStatus));
	}

	private String[] getConfigs(String[] configIds, String defaultInitConfId) {
		if (configIds == null || configIds.length == 0) {
			return new String[] { defaultInitConfId };
		} else {
			return configIds;
		}
	}
}
