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
package pl.cyfronet.coin.impl.action.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AddAsToWorkflow;
import pl.cyfronet.coin.api.beans.AddAsWithKeyToWorkflow;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AtomicServiceWorkflowAction;
import pl.cyfronet.coin.impl.air.client.AppliancePreferences;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

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

	private static final Logger logger = LoggerFactory
			.getLogger(StartAtomicServiceAction.class);

	private String contextId;
	private Integer defaultPriority;
	private String keyName;
	private List<String> developmentASes;
	private List<AddAsToWorkflow> requiredAses;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client.
	 * @param initConfigId Initial configuration id.
	 * @param name New instance name.
	 * @param contextId Context id.
	 * @param username User name.
	 */
	public StartAtomicServiceAction(ActionFactory actionFactory,
			String username, String contextId, Integer priority,
			AddAsWithKeyToWorkflow requiredAs) {
		this(actionFactory, username, contextId, priority, Arrays
				.asList((AddAsToWorkflow) requiredAs), requiredAs.getKeyId());
	}

	public StartAtomicServiceAction(ActionFactory actionFactory,
			String username, String contextId, Integer priority,
			List<AddAsToWorkflow> requiredAses, String keyName) {
		super(actionFactory, username);
		this.contextId = contextId;
		this.defaultPriority = priority;
		this.keyName = keyName;
		this.requiredAses = requiredAses;
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
		logger.debug("Add atomic services {} into workflow [{}] with key {}",
				new Object[] { requiredAses, contextId, keyName });

		List<ApplianceType> types = getTypes();
		if (anyInDevelopment(types)) {
			throw new AtomicServiceNotFoundException();
		}

		if (isInDevelopmentMode(workflow)) {
			createDevelopmentAtomicServices(types);
		}

		try {
			registerVms(contextId, requiredAses, defaultPriority,
					workflow.getWorkflow_type(), keyName);
		} catch (CloudFacadeException e) {
			if (isInDevelopmentMode(workflow)) {
				removeDevelopmentASes();
			}
			throw e;
		}

		return null;
	}

	private void removeDevelopmentASes() {
		for (String devAsiId : developmentASes) {
			try {
				Action<Class<Void>> action = getActionFactory()
						.createDeleteAtomicServiceFromAirAction(devAsiId);
				action.execute();
			} catch (Exception e) {
				logger.error("Unable to delete development AS");
			}
		}
	}

	private boolean isInDevelopmentMode(WorkflowDetail workflow) {
		return workflow.getWorkflow_type() == WorkflowType.development;
	}

	private void createDevelopmentAtomicServices(List<ApplianceType> types) {
		developmentASes = new ArrayList<>();

		for (int i = 0; i < types.size(); i++) {
			ApplianceType baseAS = types.get(i);
			AddAsToWorkflow requiredAs = requiredAses.get(i);
			String initConfId = requiredAs.getAsConfigId();
			String newInitConf = createDevelopmentAtomicService(baseAS,
					requiredAs, initConfId, developmentASes);
			requiredAs.setAsConfigId(newInitConf);
		}
	}

	private List<ApplianceType> getTypes() {
		List<ApplianceType> types = new ArrayList<>();
		for (AddAsToWorkflow requiredAs : requiredAses) {
			types.add(getAir().getTypeFromConfig(requiredAs.getAsConfigId()));
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
			AddAsToWorkflow requiredAs, String initConfId,
			List<String> developmentASes) {
		baseAS.setDevelopment(true);
		baseAS.setName(String.format("%s-%s", baseAS.getName(),
				System.currentTimeMillis()));
		fillProps(baseAS, requiredAs);
		
		Action<String> createASAction = getActionFactory()
				.createCreateAtomicServiceInAirAction(getUsername(), baseAS,
						baseAS.getId());
		String devAsId = createASAction.execute();
		developmentASes.add(devAsId);
		return createInitConfCopy(devAsId, initConfId);
	}

	private void fillProps(ApplianceType baseAS, AddAsToWorkflow requiredAs) {
		AppliancePreferences prefs = baseAS.getAppliance_preferences();
		if(prefs == null) {
			prefs = new AppliancePreferences();
		}
		boolean prefsModified = false;
		if(requiredAs.getCpu() != null) {
			prefs.setCpu(requiredAs.getCpu());
			prefsModified = true;
		}
		if(requiredAs.getDisk() != null) {
			prefs.setDisk(requiredAs.getDisk());
			prefsModified = true;
		}
		if(requiredAs.getMemory() != null) {
			prefs.setMemory(requiredAs.getMemory());
			prefsModified = true;
		}
		if(prefsModified) {
			baseAS.setAppliance_preferences(prefs);
		}
	}

	private String createInitConfCopy(String asName, String srcInitConfId) {
		String initConfPayload = getAir().getApplianceConfig(srcInitConfId);

		InitialConfiguration initConf = new InitialConfiguration();
		initConf.setName(System.currentTimeMillis() + "");
		initConf.setPayload(initConfPayload);
		Action<String> addInitConfAction = getActionFactory()
				.createAddInitialConfiguration(asName, initConf);

		return addInitConfAction.execute();
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}
}
