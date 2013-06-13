package pl.cyfronet.coin.impl;

import static pl.cyfronet.coin.impl.utils.Validator.validateId;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.CloudFacadeOld;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceRequest;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.InvocationPathInfo;
import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.api.exception.NotAcceptableException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.auth.annotation.Public;
import pl.cyfronet.coin.auth.annotation.Role;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

public class CloudFacadeImplOld extends UsernameAwareService implements
		CloudFacadeOld {

	private static final String ADMIN_ROLE = "cloudadmin";

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CloudFacadeImpl.class);

	private ActionFactory actionFactory;

	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		logger.debug("Get atomic services");
		Action<List<AtomicService>> action = actionFactory
				.createListAtomicServicesAction(getUsername(), true);

		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public String createAtomicService(NewAtomicService newAtomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		String username = getUsername();
		logger.info("{} creates atomic service from {} [{}]", new Object[] {
				username, newAtomicService.getSourceAsiId(), newAtomicService });
		try {
			Action<String> action = actionFactory
					.createCreateAtomicServiceAction(username, newAtomicService);
			return action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId, boolean loadPayload)
			throws AtomicServiceNotFoundException {
		logger.debug("Get initial configurations for: {}", atomicServiceId);
		validateId(atomicServiceId);
		Action<List<InitialConfiguration>> action = actionFactory
				.createListInitialConfigurationsAction(atomicServiceId,
						loadPayload);
		return action.execute();
	}

	@Override
	public String addInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration)
			throws AtomicServiceNotFoundException,
			AtomicServiceNotFoundException, CloudFacadeException,
			InitialConfigurationAlreadyExistException {
		String username = getUsername();
		logger.info("{} creates new {} atomic service initial configuration",
				username, atomicServiceId);
		validateId(atomicServiceId);

		Action<String> action = actionFactory.createAddInitialConfiguration(
				atomicServiceId, initialConfiguration);

		return action.execute();
	}

	@Override
	@Public
	public String getServicesSet() {
		Action<String> action = actionFactory.createGetServicesSetAction();
		return action.execute();
	}

	@Override
	public String getEndpointDescriptor(String atomicServiceId,
			String serviceName, String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		logger.debug("Getting endpoint descriptor for {}:{}/{}", new Object[] {
				atomicServiceId, serviceName, invocationPath });

		validateId(atomicServiceId);
		Action<String> action = actionFactory.createGetEndpointPayloadAction(
				atomicServiceId, serviceName, invocationPath);

		String descriptor = action.execute();

		logger.debug("Descriptor value: {}", descriptor);

		return descriptor;
	}

	@Override
	public InvocationPathInfo getInvocationPathInfo(String atomicServiceId,
			String serviceName, String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		validateId(atomicServiceId);
		// check if atomic service with given id is registered in Atmosphere.
		Action<InvocationPathInfo> action = actionFactory
				.createGetInvocationPathInfo(atomicServiceId, serviceName);
		return action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}

	@Override
	public void deleteAtomicService(String atomicServiceId)
			throws AtomicServiceNotFoundException, NotAcceptableException,
			NotAllowedException {
		String username = getUsername();
		logger.info("{} deletes {} atomic service", username, atomicServiceId);
		validateId(atomicServiceId);
		Action<Class<Void>> action = actionFactory
				.createDeleteAtomicServiceAction(username, atomicServiceId,
						hasRole(ADMIN_ROLE));
		action.execute();
	}

	@Override
	public void updateAtomicService(String atomicServiceId,
			AtomicServiceRequest updateRequest)
			throws AtomicServiceNotFoundException, NotAcceptableException,
			NotAllowedException {
		String username = getUsername();
		logger.info("{} updates {} atomic service [{}]", new Object[] {
				username, atomicServiceId, updateRequest });

		validateId(atomicServiceId);
		Action<Class<Void>> action = actionFactory
				.createUpdateAtomicServiceAction(username, atomicServiceId,
						updateRequest, hasRole(ADMIN_ROLE));
		action.execute();
	}
}
