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

import pl.cyfronet.coin.api.beans.WorkflowType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
@XmlRootElement(name ="workflowDetail")
public class WorkflowDetail {

	private String name;
	
	private String id;
	
	private WorkflowType type;
	
	private WorkflowType workflow_type;
	
	private Integer priority;
	
	private String description;
	
	private List<Vms> vms;

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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public WorkflowType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(WorkflowType type) {
		this.type = type;
	}

	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the vms
	 */
	public List<Vms> getVms() {
		return vms;
	}

	/**
	 * @param vms the vms to set
	 */
	public void setVms(List<Vms> vms) {
		this.vms = vms;
	}

	/**
	 * @return the workflow_type
	 */
	public WorkflowType getWorkflow_type() {
		return workflow_type;
	}

	/**
	 * @param workflow_type the workflow_type to set
	 */
	public void setWorkflow_type(WorkflowType workflow_type) {
		this.workflow_type = workflow_type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WorkflowDetail [name=" + name + ", id=" + id + ", type=" + type
				+ ", workflow_type=" + workflow_type + ", priority=" + priority
				+ ", description=" + description + ", vms=" + vms + "]";
	}
}
