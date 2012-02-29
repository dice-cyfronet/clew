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
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.manager.CloudManager;
import pl.cyfronet.coin.impl.manager.exception.ApplianceTypeNotFound;

/**
 * Web service which exposes functionality given by the cloud manager.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudFacadeImpl implements CloudFacade {

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CloudFacadeImpl.class);

	/**
	 * Cloud manager.
	 */
	private CloudManager manager;

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.api.ws.CloudFacade#getAtomicServices()
	 */
	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		logger.debug("Get atomic services");
		return manager.getAtomicServices();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.ws.CloudFacade#getAtomicServiceStatus(java.lang.
	 * String)
	 */
	@Override
	public AtomicServiceInstance getAtomicServiceInstance(
			String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		logger.debug("Get atomic service status for {}",
				atomicServiceInstanceId);
		return manager.getAtomicServiceStatus(atomicServiceInstanceId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.ws.CloudFacade#createAtomicService(java.lang.String,
	 * pl.cyfronet.coin.api.beans.AtomicService)
	 */
	@Override
	public void createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		logger.debug("Create atomic service from {}", atomicServiceInstanceId);
		manager.createAtomicService(atomicServiceInstanceId, atomicService);
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.api.CloudFacade#getInitialConfigurations(java.lang.String)
	 */
	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId) {
		logger.debug("Get initial configurations for: {}", atomicServiceId);
		try {
			return manager.getInitialConfigurations(atomicServiceId);
		} catch (ApplianceTypeNotFound e) {
			throw new WebApplicationException(e, 404);
		}
	}
	
	/**
	 * Set cloud manager.
	 * @param manager Cloud manager implementation.
	 */
	public void setManager(CloudManager manager) {
		this.manager = manager;
	}
}
