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

package pl.cyfronet.coin.ws;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.ws.CloudFacade;
import pl.cyfronet.coin.api.ws.exception.CloudFacadeException;
import pl.cyfronet.coin.manager.CloudManager;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudFacadeImpl implements CloudFacade {

	private static final Logger logger = LoggerFactory
			.getLogger(CloudFacadeImpl.class);

	private CloudManager manager;

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.ws.CloudFacade#getAtomicServiceInstances(java.lang
	 * .String)
	 */
	@Override
	public List<AtomicServiceInstance> getAtomicServiceInstances(
			String contextId) throws CloudFacadeException {
		logger.debug("Get atomic services instances for \"{}\" context",
				contextId);
		return manager.getAtomicServiceInstances(contextId);
	}

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
	 * pl.cyfronet.coin.api.ws.CloudFacade#startAtomicService(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String startAtomicService(String atomicServiceId, String contextId)
			throws CloudFacadeException {
		logger.debug("Start atomic service [{}] in {} context",
				atomicServiceId, contextId);
		return manager.startAtomicService(atomicServiceId, contextId);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.ws.CloudFacade#getAtomicServiceStatus(java.lang.
	 * String)
	 */
	@Override
	public AtomicServiceInstance getAtomicServiceStatus(
			String atomicServiceInstanceId) throws CloudFacadeException {
		logger.debug("Get atomic service status for {}",
				atomicServiceInstanceId);
		return manager.getAtomicServiceStatus(atomicServiceInstanceId);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.ws.CloudFacade#stopAtomicServiceInstance(java.lang
	 * .String)
	 */
	@Override
	public void stopAtomicServiceInstance(String atomicServiceInstance)
			throws CloudFacadeException {
		logger.debug("Stop {} atomic service instance", atomicServiceInstance);
		manager.stopAtomicServiceInstance(atomicServiceInstance);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.api.ws.CloudFacade#createAtomicService(java.lang.String,
	 * pl.cyfronet.coin.api.beans.AtomicService)
	 */
	@Override
	public void createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService) throws CloudFacadeException {
		logger.debug("Create atomic service from {}", atomicServiceInstanceId);
		manager.createAtomicService(atomicServiceInstanceId, atomicService);
	}

	/**
	 * Set cloud manager.
	 * @param manager Cloud manager implementation.
	 */
	public void setManager(CloudManager manager) {
		this.manager = manager;
	}

}
