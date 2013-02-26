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

import pl.cyfronet.coin.api.RedirectionType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class Redirection {
	private String id; 
	private String name;
	private Integer toPort;
	private Integer fromPort;
	private String host;
	private RedirectionType type;
	private String postfix;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getToPort() {
		return toPort;
	}

	public void setToPort(Integer port) {
		this.toPort = port;
	}

	public Integer getFromPort() {
		return fromPort;
	}

	public void setFromPort(Integer fromPort) {
		this.fromPort = fromPort;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public RedirectionType getType() {
		return type;
	}

	public void setType(RedirectionType type) {
		this.type = type;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	@Override
	public String toString() {
		return "Redirection [id=" + id + ", name=" + name + ", toPort="
				+ toPort + ", fromPort=" + fromPort + ", host=" + host
				+ ", type=" + type + ", postfix=" + postfix + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
