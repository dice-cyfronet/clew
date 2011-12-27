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

package pl.cyfronet.coin.impl.manager;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.ws.exception.CloudFacadeException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DummyCloudManagerLogger implements CloudManager {

	private static final Logger logger = LoggerFactory
			.getLogger(DummyCloudManagerLogger.class);

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.CloudManager#getAtomicServiceInstances(java.lang.String)
	 */
	@Override
	public List<AtomicServiceInstance> getAtomicServiceInstances(
			String contextId) throws CloudFacadeException {
		logger.info(
				"Get atomic services instances for \"{}\" context - returning two dummy atomic service instances",
				contextId);
		return Arrays.asList(new AtomicServiceInstance(),
				new AtomicServiceInstance());
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.CloudManager#getAtomicServices()
	 */
	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		logger.info("Get atomic services - returning two dummy atomic services");
		return Arrays.asList(new AtomicService(), new AtomicService());
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.CloudManager#startAtomicService(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String startAtomicService(String atomicServiceId, String contextId)
			throws CloudFacadeException {
		logger.info(
				"Start atomic service [{}] in {} context - generated id returned",
				atomicServiceId, contextId);
		return System.currentTimeMillis() + "";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.CloudManager#getAtomicServiceStatus(java.lang.String)
	 */
	@Override
	public AtomicServiceInstance getAtomicServiceStatus(
			String atomicServiceInstanceId) throws CloudFacadeException {
		logger.info(
				"Get atomic service status for {} - default atomic service instance returned",
				atomicServiceInstanceId);
		return new AtomicServiceInstance();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.CloudManager#stopAtomicServiceInstance(java.lang.String)
	 */
	@Override
	public void stopAtomicServiceInstance(String atomicServiceInstance)
			throws CloudFacadeException {
		logger.info("Stop {} atomic service instance.", atomicServiceInstance);
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.CloudManager#createAtomicService(java.lang.String,
	 * pl.cyfronet.coin.api.beans.AtomicService)
	 */
	@Override
	public void createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService) throws CloudFacadeException {
		logger.info("Create atomic service from {}", atomicServiceInstanceId);
	}

}
