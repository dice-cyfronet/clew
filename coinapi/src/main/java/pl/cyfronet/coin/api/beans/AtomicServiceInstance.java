/*
 * Copyright 2011 ACC CYFRONET AGH
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

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean which describes atomic service instance (vm instance).
 * @author <a href="d.harezlak@cyfronet.pl>Daniel Harezlak</a>
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement
public class AtomicServiceInstance extends StatusBean {

	private String atomicServiceId;

	@Deprecated
	private Credential credential;

	private List<Redirection> redirections;

	private String configurationId;

	private String instanceId;

	private String publicKeyId;

	/**
	 * @return the credential
	 */
	@Deprecated
	public Credential getCredential() {
		return credential;
	}

	/**
	 * @param credential the credential to set
	 */
	@Deprecated
	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public String getAtomicServiceId() {
		return atomicServiceId;
	}

	public void setAtomicServiceId(String atomicServiceId) {
		this.atomicServiceId = atomicServiceId;
	}

	/**
	 * @return the redirections
	 */
	public List<Redirection> getRedirections() {
		return redirections;
	}

	/**
	 * @param redirections the redirections to set
	 */
	public void setRedirections(List<Redirection> redirections) {
		this.redirections = redirections;
	}

	/**
	 * @return the configurationId
	 */
	public String getConfigurationId() {
		return configurationId;
	}

	/**
	 * @param configurationId the configurationId to set
	 */
	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}

	/**
	 * @return the instanceId
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * @return the publicKeyId
	 */
	public String getPublicKeyId() {
		return publicKeyId;
	}

	/**
	 * @param publicKeyId the publicKeyId to set
	 */
	public void setPublicKeyId(String publicKeyId) {
		this.publicKeyId = publicKeyId;
	}

	@Override
	public String toString() {
		return "AtomicServiceInstance [atomicServiceId=" + atomicServiceId
				+ ", credential=" + credential + ", redirections="
				+ redirections + ", configurationId=" + configurationId
				+ ", instanceId=" + instanceId + ", publicKeyId=" + publicKeyId
				+ "]";
	}
}