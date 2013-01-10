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
package pl.cyfronet.coin.api.beans;

public class Endpoint {
	private String id;
	private String description;
	private String descriptor;
	private String invocationPath;
	private Integer port;
	private String serviceName;
	private EndpointType type; 
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String serviceDescription) {
		this.description = serviceDescription;
	}
	public String getInvocationPath() {
		return invocationPath;
	}
	public void setInvocationPath(String invocationPath) {
		this.invocationPath = invocationPath;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	/**
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}
	/**
	 * @param descriptor the descriptor to set
	 */
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the type
	 */
	public EndpointType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(EndpointType type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Endpoint [id=" + id + ", description=" + description
				+ ", descriptor=" + descriptor + ", invocationPath="
				+ invocationPath + ", port=" + port + ", serviceName="
				+ serviceName + ", type=" + type + "]";
	}
}