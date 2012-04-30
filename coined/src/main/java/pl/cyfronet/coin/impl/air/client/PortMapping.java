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

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class PortMapping {

	private int vm_port;
	
	private int headnode_port;

	/**
	 * @return the vm_port
	 */
	public int getVm_port() {
		return vm_port;
	}

	/**
	 * @param vm_port the vm_port to set
	 */
	public void setVm_port(int vm_port) {
		this.vm_port = vm_port;
	}

	/**
	 * @return the headnode_port
	 */
	public int getHeadnode_port() {
		return headnode_port;
	}

	/**
	 * @param headnode_port the headnode_port to set
	 */
	public void setHeadnode_port(int headnode_port) {
		this.headnode_port = headnode_port;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PortMapping [vm_port=" + vm_port + ", headnode_port="
				+ headnode_port + "]";
	}
}