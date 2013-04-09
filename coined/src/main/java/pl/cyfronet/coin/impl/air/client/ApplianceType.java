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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement(name = "applianceType")
public class ApplianceType {

	private String id;

	private String name;

	private String author;

	private boolean published;

	private boolean scalable;

	private boolean shared;

	private boolean development;

	private String description;

	private int templates_count;

	private String proxy_conf_name;

	private List<ApplianceConfiguration> configurations;

	private List<ATEndpoint> endpoints;

	private List<ATPortMapping> port_mappings;

	/**
	 * #1516
	 * @since 1.3.0
	 */
	private ApplianceSla appliance_sla;
	
	/**
	 * #1516
	 * @since 1.3.0
	 */
	private AppliancePreferences appliance_preferences;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public boolean isScalable() {
		return scalable;
	}

	public void setScalable(boolean scalable) {
		this.scalable = scalable;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTemplates_count() {
		return templates_count;
	}

	public void setTemplates_count(int templates_count) {
		this.templates_count = templates_count;
	}

	public String getProxy_conf_name() {
		return proxy_conf_name;
	}

	public void setProxy_conf_name(String proxy_conf_name) {
		this.proxy_conf_name = proxy_conf_name;
	}

	public List<ApplianceConfiguration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ApplianceConfiguration> configurations) {
		this.configurations = configurations;
	}

	public List<ATEndpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<ATEndpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isDevelopment() {
		return development;
	}

	public void setDevelopment(boolean development) {
		this.development = development;
	}

	public List<ATPortMapping> getPort_mappings() {
		return port_mappings;
	}

	public void setPort_mappings(List<ATPortMapping> port_mappings) {
		this.port_mappings = port_mappings;
	}

	public ApplianceSla getAppliance_sla() {
		return appliance_sla;
	}

	public void setAppliance_sla(ApplianceSla appliance_sla) {
		this.appliance_sla = appliance_sla;
	}

	public AppliancePreferences getAppliance_preferences() {
		return appliance_preferences;
	}

	public void setAppliance_preferences(AppliancePreferences appliance_preferences) {
		this.appliance_preferences = appliance_preferences;
	}

	@Override
	public String toString() {
		return "ApplianceType [id=" + id + ", name=" + name + ", author="
				+ author + ", published=" + published + ", scalable="
				+ scalable + ", shared=" + shared + ", development="
				+ development + ", description=" + description
				+ ", templates_count=" + templates_count + ", proxy_conf_name="
				+ proxy_conf_name + ", configurations=" + configurations
				+ ", endpoints=" + endpoints + ", port_mappings="
				+ port_mappings + ", appliance_sla=" + appliance_sla
				+ ", appliance_preferences=" + appliance_preferences + "]";
	}
}
