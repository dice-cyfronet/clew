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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.management.OperationsException;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.allocation.OperationStatus;
import pl.cyfronet.dyrealla.allocation.impl.ManagerResponseImpl;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StopWorkflowTest extends AbstractCloudManagerTest {

	@Test
	public void shouldStopWorkflow() throws Exception {
		// when
		mockGetWorkflow();
		mockStopWorkflowInAtmosphere();
		manager.stopWorkflow(contextId, username);

		// then
		verify(air, times(1)).stopWorkflow(contextId);
		verify(air, times(1)).getWorkflow(contextId);
		verify(atmosphere, times(1)).removeRequiredAppliances(contextId);
	}

	private void mockStopWorkflowInAtmosphere() {
		ManagerResponseImpl atmosphereManagerResponse = new ManagerResponseImpl();
		atmosphereManagerResponse
				.setOperationStatus(OperationStatus.SUCCESSFUL);

		when(atmosphere.removeRequiredAppliances(contextId)).thenReturn(
				atmosphereManagerResponse);
	}

	//FIXME waiting for atmo improvement
	@Test(enabled=false)
	public void shouldThrowCloudExceptionWhenAtmosphereFail() throws Exception {
		// when
		mockGetWorkflow();
		mockStopWorkflowInAtmosphereWithError(contextId);

		try {
			manager.stopWorkflow(contextId, username);
			fail();
		} catch (CloudFacadeException e) {
			// ok
		}

		// then
		verify(air, times(1)).getWorkflow(contextId);
		verify(atmosphere, times(1)).removeRequiredAppliances(contextId);
	}

	//FIXME waiting for atmo improvement
	@Test(enabled=false)
	public void shouldStopWorkflowWhenOnAtmosphereWarning() throws Exception {
		// given
		mockGetWorkflow();
		mockStopWorkflowInAtmosphereWithWarning(contextId);

		// when
		manager.stopWorkflow(contextId, username);

		// then
		verify(air, times(1)).getWorkflow(contextId);
		verify(air, times(1)).stopWorkflow(contextId);
		verify(atmosphere, times(1)).removeRequiredAppliances(contextId);

	}

	private void mockStopWorkflowInAtmosphereWithWarning(String contextId) {
		//FIXME
//		mockStopWorkflowInAtmosphereWithReturnStatus(contextId,
//				OperationStatus.COMPLETED_WITH_ERRORS);
	}

	private void mockStopWorkflowInAtmosphereWithError(String contextId) {
		mockStopWorkflowInAtmosphereWithReturnStatus(contextId,
				OperationStatus.FAILED);
	}

	private void mockStopWorkflowInAtmosphereWithReturnStatus(String contextId,
			OperationStatus returnStatus) {
		ManagerResponseImpl atmosphereFailureResponse = new ManagerResponseImpl();
		atmosphereFailureResponse.setOperationStatus(returnStatus);
		atmosphereFailureResponse.addError("key", "something wrong happend "
				+ returnStatus);

		when(atmosphere.removeRequiredAppliances(contextId)).thenReturn(
				atmosphereFailureResponse);
	}

	@Test
	public void shouldTrowWorkflowNotFoundWhenStoppingNonExistingWorkflow()
			throws Exception {
		// given
		String nonExistingContextId = "nonExisting";
		String username = "existingUser";

		// when
		when(air.getWorkflow(nonExistingContextId)).thenThrow(
				getAirException(404));
		try {
			// workflow does not exist
			manager.stopWorkflow(nonExistingContextId, username);
			fail();
		} catch (WorkflowNotFoundException e) {
			// shoud be thrown
		}

		// then
		verify(air, times(1)).getWorkflow(nonExistingContextId);
	}

	@Test
	public void shouldThrowWorkfloNotFoundExceptionWhileStoppingNotOwnWorkflow()
			throws Exception {
		// given
		String existingContextId = "id1";
		String username = "existingUser";

		WorkflowDetail w1 = new WorkflowDetail();
		w1.setVph_username("myUser");
		w1.setName("w1");
		w1.setId("id1");
		w1.setState(Status.running);
		w1.setWorkflow_type(WorkflowType.development);

		// when
		when(air.getWorkflow(existingContextId)).thenReturn(w1);

		try {
			// workflow belongs to othere user
			manager.stopWorkflow(existingContextId, username);
			fail();
		} catch (WorkflowNotFoundException e) {
			// shoud be thrown
		}

		// then
		verify(air, times(1)).getWorkflow(existingContextId);
	}
}
