package pl.cyfronet.coin.impl;

import static pl.cyfronet.coin.impl.utils.Validator.validateASName;
import static pl.cyfronet.coin.impl.utils.Validator.validateId;
import static pl.cyfronet.coin.impl.utils.Validator.validateRedirectionName;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.WorkflowManagementOld;
import pl.cyfronet.coin.api.beans.AddAsWithKeyToWorkflow;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.UserWorkflows;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.auth.annotation.Role;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

public class WorkflowManagementImplOld extends UsernameAwareService implements
		WorkflowManagementOld {

	// throw new WebApplicationException(403);

	private ActionFactory actionFactory;

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WorkflowManagementImpl.class);

	@Override
	public String startWorkflow(WorkflowStartRequest workflow)
			throws WorkflowStartException {
		String username = getUsername();
		logger.info("{} starts workflow {}", username, workflow);
		Action<String> action = actionFactory.createStartWorkflowAction(
				workflow, username);
		return action.execute();
	}

	@Override
	public void stopWorkflow(String workflowId) {
		String username = getUsername();
		logger.info("{} stops {} workflow", username, workflowId);
		validateId(workflowId);
		Action<Class<Void>> action = actionFactory.createStopWorkflowAction(
				workflowId, username);
		try {
			action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public void addAtomicServiceToWorkflow(String contextId,
			AddAsWithKeyToWorkflow request) throws WorkflowNotFoundException,
			CloudFacadeException {
		String username = getUsername();
		logger.info("{} adds atomic service {} to worklow {} [{}]",
				new Object[] { username, request.getAsConfigId(), contextId,
						request });
		validateId(contextId, request.getAsConfigId());
		validateASName(request.getName());
		if (request.getKeyId() != null) {
			validateId(request.getKeyId());
		}
		Action<String> action = actionFactory.createStartAtomicServiceAction(
				username, contextId, request);
		action.execute();
	}

	@Override
	public void removeAtomicServiceFromWorkflow(String workflowId,
			String asConfigId) {
		String username = getUsername();
		logger.info("{} removes {} atomic service from {} workflow",
				new Object[] { username, asConfigId, workflowId });
		validateId(workflowId);
		Action<Class<Void>> action = actionFactory
				.createRemoveAtomicServiceFromWorkflowAction(username,
						workflowId, asConfigId);
		action.execute();
	}

	@Override
	public void removeAtomicServiceInstanceFromWorkflow(String workflowId,
			String asInstanceId) throws WorkflowNotFoundException,
			WorkflowNotInDevelopmentModeException, CloudFacadeException {
		String username = getUsername();
		logger.info("{} removes {} atomic service instance from {} workflow",
				new Object[] { username, asInstanceId, workflowId });
		validateId(workflowId);
		Action<Class<Void>> action = actionFactory
				.createRemoveASIFromWorkflowAction(username, workflowId,
						asInstanceId);
		action.execute();
	}

	@Override
	public UserWorkflows getWorkflows() {
		String username = getUsername();
		UserWorkflows wrapper = new UserWorkflows();
		wrapper.setUsername(username);

		Action<List<WorkflowBaseInfo>> action = actionFactory
				.createGetUserWorkflowsAction(username);
		try {
			List<WorkflowBaseInfo> workflows = action.execute();
			wrapper.setWorkflows(workflows);
		} catch (Exception e) {
			wrapper.setWorkflows(new ArrayList<WorkflowBaseInfo>());
		}
		return wrapper;
	}

	@Override
	public Workflow getWorkflow(String workflowId) {
		Action<Workflow> action = actionFactory.createGetUserWorkflowAction(
				workflowId, getUsername());
		validateId(workflowId);
		try {
			return action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public AtomicServiceInstance getWorkflowAtomicServiceInstance(
			String workflowId, String asiId) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException {
		validateId(workflowId, asiId);
		Action<AtomicServiceInstance> action = actionFactory
				.createGetWorkflowAtomicServiceInstanceAction(getUsername(),
						workflowId, asiId);
		return action.execute();
	}

	@Override
	public List<Redirection> getRedirections(String contextId, String asiId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException {
		validateId(contextId);
		Action<List<Redirection>> action = actionFactory
				.createGetAsiRedirectionsAction(contextId, getUsername(), asiId);
		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public String addRedirection(String contextId, String asiId, String name,
			int port, RedirectionType type) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException {
		validateRedirectionName(name);
		String username = getUsername();
		logger.info(
				"{} adds new redirection for {} ASI belonging to {} workflow [{}:{} {} type]",
				new Object[] { username, asiId, contextId, name, port, type });
		validateId(contextId);
		Action<String> action = actionFactory.createAddAsiRedirectionAction(
				getUsername(), contextId, asiId, name, port, type);
		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public void deleteRedirection(String contextId, String asiId,
			String redirectionId) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException, RedirectionNotFoundException {
		String username = getUsername();
		logger.info(
				"{} delets {} redirecition for {} ASI belonging to {} workflow",
				new Object[] { username, redirectionId, asiId, contextId });
		validateId(contextId, redirectionId);
		Action<Class<Void>> action = actionFactory
				.createRemoveAsiRedirectionAction(username, contextId, asiId,
						redirectionId);
		action.execute();
	}

	@Override
	public List<Endpoint> getEndpoints(String contextId, String asiId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException {
		validateId(contextId);
		Action<List<Endpoint>> action = actionFactory
				.createListAsiEndpointsAction(getUsername(), contextId, asiId);
		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public String addEndpoint(String contextId, String asiId, Endpoint endpoint)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException {
		String username = getUsername();
		logger.info(
				"{} adds {} endpoint into {} Atomic Service Instance belonging to {} workflow",
				new Object[] { username, endpoint, asiId, contextId });

		validateId(contextId);
		Action<String> action = actionFactory.createAddAsiEndpointAction(
				username, contextId, asiId, endpoint);
		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public void deleteEndpoint(String contextId, String asiId, String endpointId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException, EndpointNotFoundException {
		String username = getUsername();
		logger.info(
				"{} removes {} endpoint from {} Atomic Service Instance belonging to {} workflow",
				new Object[] { username, endpointId, asiId, contextId });

		validateId(contextId, endpointId);
		Action<Class<Void>> action = actionFactory
				.createRemoveAsiEndpointAction(username, contextId, asiId,
						endpointId);
		action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}