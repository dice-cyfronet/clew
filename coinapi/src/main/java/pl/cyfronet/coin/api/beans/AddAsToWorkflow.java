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

package pl.cyfronet.coin.api.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement
public class AddAsToWorkflow {
	private String asConfigId;
	private String name;
	private Float cpu;
	private Integer memory;
	private Integer disk;

	public String getAsConfigId() {
		return asConfigId;
	}

	public void setAsConfigId(String asConfigId) {
		this.asConfigId = asConfigId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getCpu() {
		return cpu;
	}

	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public Integer getDisk() {
		return disk;
	}

	public void setDisk(Integer disk) {
		this.disk = disk;
	}

	@Override
	public String toString() {
		return "AddAsToWorkflow [asConfigId=" + asConfigId + ", name=" + name
				+ ", cpu=" + cpu + ", memory=" + memory + ", disk=" + disk
				+ "]";
	}
}
