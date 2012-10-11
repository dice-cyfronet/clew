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

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class Redirection {

	private String name;
	
	private boolean http;
	
	private Integer toPort;
	
	private Integer fromPort;

	private String host;
	
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
	 * @return the http
	 */
	public boolean isHttp() {
		return http;
	}

	/**
	 * @param http the http to set
	 */
	public void setHttp(boolean http) {
		this.http = http;
	}

	/**
	 * @return the port
	 */
	public Integer getToPort() {
		return toPort;
	}

	/**
	 * @param port the port to set
	 */
	public void setToPort(Integer port) {
		this.toPort = port;
	}

	/**
	 * @return the fromPort
	 */
	public Integer getFromPort() {
		return fromPort;
	}

	/**
	 * @param fromPort the fromPort to set
	 */
	public void setFromPort(Integer fromPort) {
		this.fromPort = fromPort;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Redirection [name=" + name + ", http=" + http + ", toPort="
				+ toPort + ", fromPort=" + fromPort + ", host=" + host + "]";
	}
}
