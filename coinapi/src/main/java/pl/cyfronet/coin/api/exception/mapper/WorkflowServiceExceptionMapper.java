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

import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInProductionModeException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class WorkflowServiceExceptionMapper extends CloudFacadeExceptionMapper {

	@Override
	public Throwable fromResponse(Response r) {
		String message = getMessage(r);
		switch (r.getStatus()) {
		case 403:
			if (WorkflowNotInDevelopmentModeException.ERROR_MESSAGE
					.equals(message)) {
				throw new WorkflowNotInDevelopmentModeException();
			} else if (WorkflowNotInProductionModeException.ERROR_MESSAGE
					.equals(message)) {
				throw new WorkflowNotInProductionModeException();
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
			}
		default:
			return new CloudFacadeException();
		}

	}

}
