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

package pl.cyfronet.coin.impl.air.client;

import pl.cyfronet.coin.api.beans.Status;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class Vms {

	private String name;
	
	private Specs specs;
	
	private String conf_id;
	
	private String appliance_type;
	
	private String source_template;

	private String vms_id;
	
	private Status state;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the specs
	 */
	public Specs getSpecs() {
		return specs;
	}

	/**
	 * @param specs the specs to set
	 */
	public void setSpecs(Specs specs) {
		this.specs = specs;
	}

	/**
	 * @return the conf_id
	 */
	public String getConf_id() {
		return conf_id;
	}

	/**
	 * @param conf_id the conf_id to set
	 */
	public void setConf_id(String conf_id) {
		this.conf_id = conf_id;
	}

	/**
	 * @return the appliance_type
	 */
	public String getAppliance_type() {
		return appliance_type;
	}

	/**
	 * @param appliance_type the appliance_type to set
	 */
	public void setAppliance_type(String appliance_type) {
		this.appliance_type = appliance_type;
	}

	/**
	 * @return the source_template
	 */
	public String getSource_template() {
		return source_template;
	}

	/**
	 * @param source_template the source_template to set
	 */
	public void setSource_template(String source_template) {
		this.source_template = source_template;
	}

	/**
	 * @return the vms_id
	 */
	public String getVms_id() {
		return vms_id;
	}

	/**
	 * @param vms_id the vms_id to set
	 */
	public void setVms_id(String vms_id) {
		this.vms_id = vms_id;
	}

	/**
	 * @return the state
	 */
	public Status getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(Status state) {
		this.state = state;
	}
	
	
}
