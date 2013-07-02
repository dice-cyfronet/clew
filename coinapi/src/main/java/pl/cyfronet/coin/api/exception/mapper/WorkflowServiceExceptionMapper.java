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

package pl.cyfronet.coin.api.exception.mapper;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceInUseException;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInProductionModeException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class WorkflowServiceExceptionMapper extends CloudFacadeExceptionMapper {

	private static final Logger logger = LoggerFactory
			.getLogger(WorkflowServiceExceptionMapper.class);

	@Override
	public Throwable fromResponse(Response r) {
		String message = getMessage(r);
		int status = r.getStatus();
		logger.info("Response to be mapped: {} -> {}", status, message);
		switch (status) {
		case 400:
			return new CloudFacadeException(message,
					Response.Status.BAD_REQUEST);
		case 403:
			if (WorkflowNotInDevelopmentModeException.ERROR_MESSAGE
					.equals(message)) {
				throw new WorkflowNotInDevelopmentModeException();
			} else if (WorkflowNotInProductionModeException.ERROR_MESSAGE
					.equals(message)) {
				throw new WorkflowNotInProductionModeException();
			} else if (AtomicServiceInstanceInUseException.ERROR_MESSAGE.equals(message)) {
				throw new AtomicServiceInstanceInUseException();
			}
		case 404:
			if (WorkflowNotFoundException.ERROR_MESSAGE.equals(message)) {
				return new WorkflowNotFoundException();
			} else if (AtomicServiceNotFoundException.ERROR_MESSAGE
					.equals(message)) {
				throw new AtomicServiceNotFoundException();
			} else if (AtomicServiceInstanceNotFoundException.ERROR_MESSAGE
					.equals(message)) {
				throw new AtomicServiceInstanceNotFoundException();
			} else if (RedirectionNotFoundException.ERROR_MESSAGE
					.equals(message)) {
				throw new RedirectionNotFoundException();
			} else if (EndpointNotFoundException.ERROR_MESSAGE.equals(message)) {
				throw new EndpointNotFoundException();
			}
		default:
			return new CloudFacadeException(message);
		}

	}

}
