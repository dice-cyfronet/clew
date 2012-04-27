/*
 * Copyright 2011 ACC CYFRONET AGH
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

package pl.cyfronet.coin.impl;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.manager.CloudManager;
import pl.cyfronet.coin.impl.utils.FileUtils;
import pl.cyfronet.coin.impl.utils.UrlUtils;

/**
 * Web service which exposes functionality given by the cloud manager.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudFacadeImpl extends UsernameAwareService implements
		CloudFacade {

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CloudFacadeImpl.class);

	/**
	 * Cloud manager.
	 */
	private CloudManager manager;

	private String coinBaseUrl;

	private String serviceTemplatesTemplate = FileUtils
			.getFileContent("services_set/serviceDescriptions.tpl");
	
	private String providerTemplate = FileUtils
			.getFileContent("services_set/provider.tpl");

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.api.ws.CloudFacade#getAtomicServices()
	 */
	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		logger.debug("Get atomic services");
		List<AtomicService> ases = manager.getAtomicServices(); 
		return ases;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.ws.CloudFacade#createAtomicService(java.lang.String,
	 * pl.cyfronet.coin.api.beans.AtomicService)
	 */
	@Override
	public String createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		logger.debug("Create atomic service from {}", atomicServiceInstanceId);
		try {
			return manager.createAtomicService(atomicServiceInstanceId,
					atomicService, getUsername());
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.CloudFacade#getInitialConfigurations(java.lang.String
	 * )
	 */
	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId) throws AtomicServiceNotFoundException {
		logger.debug("Get initial configurations for: {}", atomicServiceId);
		return manager.getInitialConfigurations(atomicServiceId);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.CloudFacade#addInitialConfiguration(java.lang.String
	 * , pl.cyfronet.coin.api.beans.InitialConfiguration)
	 */
	@Override
	public String addInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration)
			throws AtomicServiceNotFoundException,
			AtomicServiceNotFoundException, CloudFacadeException,
			InitialConfigurationAlreadyExistException {

		logger.debug("Creating new atomic service from {}, metadata: {}",
				atomicServiceId, initialConfiguration);

		return manager.addInitialConfiguration(atomicServiceId,
				initialConfiguration);
	}

	@Override
	public String getServicesSet() {

		StringBuilder sb = new StringBuilder();

		List<AtomicService> atomicServices = manager.getAtomicServices();
		for (AtomicService atomicService : atomicServices) {
			writeAtomicServiceEndpointIntoServicesSet(sb, atomicService);
		}

		return String.format(serviceTemplatesTemplate, sb);
	}

	/**
	 * @param sb
	 * @param atomicService
	 */
	private void writeAtomicServiceEndpointIntoServicesSet(StringBuilder sb,
			AtomicService atomicService) {
		for (Endpoint endpoint : atomicService.getEndpoints()) {
			logger.debug("endpoint type: {}", endpoint.getType());
			if (endpoint.getType() == EndpointType.WS) {
				String descriptorUrl = getDescriptorUrl(
						atomicService.getName(), endpoint);
				String providerSection = getProviderServiceSetSection(descriptorUrl);
				sb.append(providerSection);
			}
		}
	}

	private String getProviderServiceSetSection(String providerDescriptorUrl) {
		return String.format(providerTemplate, providerDescriptorUrl);
	}

	private String getDescriptorUrl(String atomicServiceId, Endpoint m) {
		String descriptorUrl = String.format("%s/as/%s/endpoint/%s/%s",
				new Object[] { coinBaseUrl, atomicServiceId, m.getPort(),
						getDecodedInvocationPath(m.getInvocationPath()) });
		return UrlUtils.convertToURLEscapingIllegalCharacters(descriptorUrl);
	}

	private String getDecodedInvocationPath(String path) {
		return path.replaceAll("^/+", "");
	}

	@Override
	public String getEndpointDescriptor(String atomicServiceId,
			int servicePort, String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		logger.debug("Getting endpoint descriptor for {}:{}/{}", new Object[] {
				atomicServiceId, servicePort, invocationPath });

		String descriptor = manager.getEndpointPayload(atomicServiceId,
				servicePort, invocationPath);

		logger.debug("Descriptor value: {}", descriptor);

		return descriptor;
	}

	@Override
	public String getAtomicServiceId(String atomicServiceId, int servicePort,
			String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		// check if atomic service with given id is registered in Atmosphere.
		AtomicService atomicService = manager.getAtomicService(atomicServiceId);
		return atomicService.getAtomicServiceId();
	}

	/**
	 * Set cloud manager.
	 * @param manager Cloud manager implementation.
	 */
	public void setManager(CloudManager manager) {
		this.manager = manager;
	}

	/**
	 * @param coinBaseUrl the coinBaseUrl to set
	 */
	public void setCoinBaseUrl(String coinBaseUrl) {
		this.coinBaseUrl = coinBaseUrl;
	}
}
