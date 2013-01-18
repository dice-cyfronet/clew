/*
 * Copyright 2013 ACC CYFRONET AGH
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

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.SecurityPolicyAlreadyExistException;
import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class SecurityPolicyExceptionMapper extends CloudFacadeExceptionMapper {

	private static final Logger logger = LoggerFactory
			.getLogger(WorkflowServiceExceptionMapper.class);

	@Override
	public Throwable fromResponse(Response r) {
		String message = getMessage(r);
		int status = r.getStatus();
		logger.info("Response to be mapped: {} -> {}", status, message);
		switch (status) {
		case 404:
			return new SecurityPolicyNotFoundException();
		case 409:
			return new SecurityPolicyAlreadyExistException();
		default:
			return new CloudFacadeException(message);
		}

	}

}