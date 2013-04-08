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

import java.util.List;

import pl.cyfronet.coin.api.beans.Status;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class Vms {

	private String name;

	private Specs specs;

	private String configuration;

	private String appliance_type;
	
	private String appliance_type_name;
	
	private String source_template;	
	
	private String vms_id;

	private Status state;

	private String user_key;
	
	
	/**
	 * @see #1407
	 */
	private String site_id;

	private List<PortMapping> internal_port_mappings;

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
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * @param conf_id the conf_id to set
	 */
	public void setConfiguration(String conf_id) {
		this.configuration = conf_id;
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
	 * @return the appliance_type_name
	 */
	public String getAppliance_type_name() {
		return appliance_type_name;
	}

	/**
	 * @param appliance_type_name the appliance_type_name to set
	 */
	public void setAppliance_type_name(String appliance_type_name) {
		this.appliance_type_name = appliance_type_name;
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

	/**
	 * @return the site_id
	 */
	public String getSite_id() {
		return site_id;
	}

	/**
	 * @param site_id the site_id to set
	 */
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	/**
	 * @return the internal_port_mappings
	 */
	public List<PortMapping> getInternal_port_mappings() {
		return internal_port_mappings;
	}

	/**
	 * @param internal_port_mappings the internal_port_mappings to set
	 */
	public void setInternal_port_mappings(
			List<PortMapping> internal_port_mappings) {
		this.internal_port_mappings = internal_port_mappings;
	}

	/**
	 * @return the user_key
	 */
	public String getUser_key() {
		return user_key;
	}

	/**
	 * @param user_key the user_key to set
	 */
	public void setUser_key(String user_key) {
		this.user_key = user_key;
	}

	@Override
	public String toString() {
		return "Vms [name=" + name + ", specs=" + specs + ", conf_id="
				+ configuration + ", appliance_type=" + appliance_type
				+ ", appliance_type_name=" + appliance_type_name
				+ ", source_template=" + source_template + ", vms_id=" + vms_id
				+ ", state=" + state + ", user_key=" + user_key + ", site_id="
				+ site_id + ", internal_port_mappings="
				+ internal_port_mappings + "]";
	}
}
