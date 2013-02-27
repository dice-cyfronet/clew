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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
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

	private String contextId;
	private Integer defaultPriority;
	private String keyName;
	private List<String> initConfigIds;
	private List<String> asNames;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client.
	 * @param initConfigId Initial configuration id.
	 * @param name New instance name.
	 * @param contextId Context id.
	 * @param username User name.
	 */
	StartAtomicServiceAction(AirClient air, DyReAllaManagerService atmosphere,
			String username, String initConfigId, String asName,
			String contextId, Integer priority, String keyName) {
		this(air, atmosphere, username, Arrays.asList(initConfigId), Arrays
				.asList(asName), contextId, priority, keyName);
	}

	public StartAtomicServiceAction(AirClient air,
			DyReAllaManagerService atmosphere, String username,
			List<String> initConfIds, List<String> asNames, String contextId,
			Integer priority, String keyName) {
		super(air, atmosphere, username);
		this.initConfigIds = initConfIds;
		this.asNames = asNames;
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
		logger.debug(
				"Add atomic service [{} {}] into workflow [{}] with key {}",
				new Object[] { asNames, initConfigIds, contextId, keyName });

		List<ApplianceType> types = getTypes();
		if (anyInDevelopment(types)) {
			throw new AtomicServiceNotFoundException();
		}

		if (workflow.getWorkflow_type() == WorkflowType.development) {
			initConfigIds = createDevelopmentAtomicServices(types);
		}

		registerVms(contextId, initConfigIds, asNames, defaultPriority,
				workflow.getWorkflow_type(), keyName);

		// TODO information from Atmosphere about atomis service instance id
		// needed!
		return null;
	}

	private List<String> createDevelopmentAtomicServices(
			List<ApplianceType> types) {
		List<String> newInitConfs = new ArrayList<>();

		for (int i = 0; i < types.size(); i++) {
			ApplianceType baseAS = types.get(i);
			String initConfId = initConfigIds.get(i);
			newInitConfs
					.add(createDevelopmentAtomicService(baseAS, initConfId));
		}

		return newInitConfs;
	}

	private List<ApplianceType> getTypes() {
		List<ApplianceType> types = new ArrayList<>();
		for (String initConfId : initConfigIds) {
			types.add(getAir().getTypeFromConfig(initConfId));
		}
		return types;
	}

	private boolean anyInDevelopment(List<ApplianceType> types) {
		for (ApplianceType type : types) {
			if (type.isDevelopment()) {
				return true;
			}
		}
		return false;
	}

	private String createDevelopmentAtomicService(ApplianceType baseAS,
			String initConfId) {
		baseAS.setDevelopment(true);
		baseAS.setName(String.format("%s-%s", baseAS.getName(),
				System.currentTimeMillis()));

		CreateAtomicServiceInAirAction createASAction = new CreateAtomicServiceInAirAction(
				getAir(), getUsername(), baseAS, baseAS.getId());
		String devAsId = createASAction.execute();

		return createInitConfCopy(devAsId, initConfId);
	}

	private String createInitConfCopy(String asName, String srcInitConfId) {
		String initConfPayload = getAir().getApplianceConfig(srcInitConfId);

		InitialConfiguration initConf = new InitialConfiguration();
		initConf.setName(System.currentTimeMillis() + "");
		initConf.setPayload(initConfPayload);
		AddInitialConfigurationAction addInitConfAction = new AddInitialConfigurationAction(
				getAir(), asName, initConf);

		return addInitConfAction.execute();
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}
}
