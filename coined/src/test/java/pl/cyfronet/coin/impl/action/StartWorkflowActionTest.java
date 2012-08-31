package pl.cyfronet.coin.impl.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.ws.rs.WebApplicationException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.mock.atmosphere.ManagerResponseTestImpl;
import pl.cyfronet.coin.impl.mock.matcher.AddRequiredAppliancesRequestMatcher;
import pl.cyfronet.dyrealla.allocation.AddRequiredAppliancesRequest;
import pl.cyfronet.dyrealla.allocation.OperationStatus;

public class StartWorkflowActionTest extends WorkflowActionTest {

	private String name = "name";
	private String description = "description";
	private int priority = 40;
	private WorkflowStartRequest startRequest;
	private WorkflowType workflowType = WorkflowType.workflow;
	private String createdContextId;

	@Test
	public void shouldStartEmptyWorkflow() throws Exception {
		givenWorkflowStartRequest();
		whenStartWorkflow();
		thenWorkflowRegistered();
	}

	@Test
	public void shouldStartWorkflowWithAtomicService() throws Exception {
		givenWorkflowStartRequestWithAsIds(OperationStatus.SUCCESSFUL, "id1",
				"id2");
		whenStartWorkflow();
		thenWorkflowRegisteredAndRequiredAsesStarted();
	}

	@Test
	public void shouldNotStartWorkflowWithAsWhenAtmosphereOperationFailed()
			throws Exception {
		givenWorkflowStartRequestWithAsIds(OperationStatus.FAILED, "id1", "id2");
		try {
			whenStartWorkflow();
			fail();
		} catch (WorkflowStartException e) {
			thanWorkflowNotRegisteredAndErrorReturned(e);
		}
	}

	@Test
	public void shouldStartWorkflowForNewUser() throws Exception {
		givenPortalWorkflowStartRequestForNewUser();
		whenStartWorkflow();
		thenPortalWorkflowRegistered();
	}

	@DataProvider(name = "workflowType")
	protected Object[][] getWorkflowTypes() {
		return new Object[][] { { WorkflowType.development },
				{ WorkflowType.portal } };
	}

	@Test(dataProvider = "workflowType")
	public void shouldNotAllowStart2PortalOrDevelopmentWorkflows(
			WorkflowType type) throws Exception {
		givenWorkflowStartRequestAndUserHasRunningWorkflowOfType(type);

		try {
			whenStartWorkflow();
			fail();
		} catch (WorkflowStartException e) {
			thanCannotStartTwoWorkflowsOfType(e, type);
		}
	}

	private void givenWorkflowStartRequestAndUserHasRunningWorkflowOfType(
			WorkflowType type) {
		givenWorkflowStartRequest();
		startRequest.setType(type);

		WorkflowDetail w1 = new WorkflowDetail();
		w1.setWorkflow_type(WorkflowType.workflow);
		w1.setState(Status.running);

		WorkflowDetail w2 = new WorkflowDetail();
		w2.setWorkflow_type(type);
		w2.setState(Status.running);

		when(air.getUserWorkflows(username)).thenReturn(Arrays.asList(w1, w2));
	}

	private void givenWorkflowStartRequestWithAsIds(
			OperationStatus resultOperationStatus, String... ids) {
		givenWorkflowStartRequest();
		startRequest.setAsConfigIds(Arrays.asList(ids));
		mockAtmosphereAddAppliancesWithResponse(resultOperationStatus);
	}

	private void givenWorkflowStartRequest() {
		workflowType = WorkflowType.workflow;
		createWorkflowStartRequest();
		mockAirStartMethod();
	}

	private void givenPortalWorkflowStartRequestForNewUser() {
		workflowType = WorkflowType.portal;
		createWorkflowStartRequest();
		mockAirStartMethod();

		when(air.getUserWorkflows(username)).thenThrow(
				new WebApplicationException(400));
	}

	private void createWorkflowStartRequest() {
		startRequest = new WorkflowStartRequest();

		startRequest.setName(name);
		startRequest.setDescription(description);
		startRequest.setPriority(priority);
		startRequest.setType(workflowType);
	}

	private void mockAirStartMethod() {
		when(
				air.startWorkflow(name, username, description, priority,
						workflowType)).thenReturn(contextId);
	}

	private void mockAtmosphereAddAppliancesWithResponse(
			OperationStatus returnStatus) {
		AddRequiredAppliancesRequestMatcher matcher = getAddAppliancesMather();
		when(atmosphere.addRequiredAppliances(argThat(matcher))).thenReturn(
				new ManagerResponseTestImpl(returnStatus));
	}

	private AddRequiredAppliancesRequestMatcher getAddAppliancesMather() {
		return new AddRequiredAppliancesRequestMatcher(contextId,
				defaultPriority, startRequest.getAsConfigIds().toArray(
						new String[0]));
	}

	private void whenStartWorkflow() throws WorkflowStartException {
		StartWorkflowAction action = actionFactory.createStartWorkflowAction(
				startRequest, username);
		createdContextId = action.execute();
	}

	private void thanWorkflowNotRegisteredAndErrorReturned(
			WorkflowStartException e) {
		verify(air, times(1)).startWorkflow(name, username, description,
				priority, workflowType);
		verify(atmosphere, times(1)).addRequiredAppliances(
				argThat(getAddAppliancesMather()));
		verify(air, times(1)).getWorkflow(any(String.class));
	}

	private void thenWorkflowRegisteredAndRequiredAsesStarted() {
		checkWorkflowRegisteredInAir(workflowType);
		verify(atmosphere, times(1)).addRequiredAppliances(
				argThat(getAddAppliancesMather()));
	}

	private void thenPortalWorkflowRegistered() {
		checkWorkflowRegisteredInAir(workflowType);

		verify(air, times(1)).getUserWorkflows(username);
		verify(atmosphere, times(0)).addRequiredAppliances(
				any(AddRequiredAppliancesRequest.class));
	}

	private void thenWorkflowRegistered() {
		checkWorkflowRegisteredInAir(workflowType);

		verify(atmosphere, times(0)).addRequiredAppliances(
				any(AddRequiredAppliancesRequest.class));
	}

	private void thanCannotStartTwoWorkflowsOfType(WorkflowStartException e,
			WorkflowType type) {
		assertEquals(String.format("Cannot start two %s workflows", type), e
				.getResponse().getEntity());
		verify(air, times(1)).getUserWorkflows(username);
		verify(air, times(0)).startWorkflow(name, username, description,
				priority, workflowType);
	}

	private void checkWorkflowRegisteredInAir(WorkflowType workflowType) {
		verify(air, times(1)).startWorkflow(name, username, description,
				priority, workflowType);
		assertEquals(contextId, createdContextId);
	}
}
