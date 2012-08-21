package pl.cyfronet.coin.impl.action;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.allocation.OperationStatus;
import pl.cyfronet.dyrealla.allocation.impl.ManagerResponseImpl;

public class StopWorkflowActionTest extends WorkflowActionTest {

	@Test
	public void shouldStopWorkflow() throws Exception {
		givenAirStateWithWorkflowListAndMockedStopWorkflowAction();
		whenStopWorkflow();
		thenCheckWorkflowStopped();		
	}	

	private void givenAirStateWithWorkflowListAndMockedStopWorkflowAction() {
		givenWorkflowStarted();
		mockStopWorkflowInAtmosphere();		
	}

	private void whenStopWorkflow() {
		stopWorkflow(contextId);
	}
	
	private void stopWorkflow(String contextId) {
		StopWorkflowAction action = actionFactory.createStopWorkflowAction(contextId, username);
		action.execute();		
	}
	
	private void thenCheckWorkflowStopped() {
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
		givenWorkflowStarted();
		mockStopWorkflowInAtmosphereWithError(contextId);

		try {
			whenStopWorkflow();
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
		givenWorkflowStarted();
		mockStopWorkflowInAtmosphereWithWarning(contextId);

		// when
		whenStopWorkflow();

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

		// when
		when(air.getWorkflow(nonExistingContextId)).thenThrow(
				getAirException(404));
		try {
			// workflow does not exist
			whenStopNonExistingWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// shoud be thrown
		}

		// then
		verify(air, times(1)).getWorkflow(nonExistingContextId);
	}

	private void whenStopNonExistingWorkflow() {
		stopWorkflow("nonExisting");		
	}

	@Test
	public void shouldThrowWorkfloNotFoundExceptionWhileStoppingNotOwnWorkflow()
			throws Exception {
		// given
		WorkflowDetail w1 = new WorkflowDetail();
		w1.setVph_username("myUser");
		w1.setName("w1");
		w1.setId(contextId);
		w1.setState(Status.running);
		w1.setWorkflow_type(WorkflowType.development);

		// when
		when(air.getWorkflow(contextId)).thenReturn(w1);

		try {
			whenStopWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// shoud be thrown
		}

		// then
		verify(air, times(1)).getWorkflow(contextId);
	}
}