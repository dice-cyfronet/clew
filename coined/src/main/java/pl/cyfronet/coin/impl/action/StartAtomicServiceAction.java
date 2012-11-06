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
package pl.cyfronet.coin.impl.action;

import java.util.Arrays;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;

/**
 * Start atomic service in defined context. If the request is send into
 * development context than new Atomic Service Instance will be spawn. Otherwise
 * new Atomic Service required for context is registered and Atmosphere will
 * take care of the whole optimization process. The optimization process is
 * specific for every Atomic Service (e.g. some of the Atomic Services can be
 * shared/scaled other not).
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StartAtomicServiceAction extends
		AtomicServiceWorkflowAction<String> {

	private String atomicServiceId;
	private String asName;
	private String contextId;
	private Integer defaultPriority;
	private String keyName;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client.
	 * @param atomicServiceId Atomic Service id.
	 * @param name New instance name.
	 * @param contextId Context id.
	 * @param username User name.
	 */
	StartAtomicServiceAction(AirClient air, DyReAllaManagerService atmosphere,
			String username, String atomicServiceId, String asName,
			String contextId, Integer priority, String keyName) {
		super(air, atmosphere, username);
		this.atomicServiceId = atomicServiceId;
		this.asName = asName;
		this.contextId = contextId;
		this.defaultPriority = priority;
		this.keyName = keyName;
	}

	/**
	 * @return Atomic Service Instance id.
	 * @throws AtomicServiceNotFoundException Thrown when atomic service is not
	 *             found.
	 * @throws CloudFacadeException
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	@Override
	public String execute() throws CloudFacadeException {
		WorkflowDetail workflow = getUserWorkflow(contextId, getUsername());
		logger.debug("Add atomic service [{} {}] into workflow [{}]",
				new Object[] { asName, atomicServiceId, contextId });
		registerVms(contextId, Arrays.asList(atomicServiceId),
				Arrays.asList(asName), defaultPriority,
				workflow.getWorkflow_type(), keyName);

		// TODO information from Atmosphere about atomis service instance id
		// needed!
		return null;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}

}
