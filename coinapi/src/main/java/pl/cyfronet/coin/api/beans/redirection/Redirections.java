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

package pl.cyfronet.coin.api.beans.redirection;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
@XmlRootElement
public class Redirections {

	List<HttpRedirection> http;
	
	List<NatRedirection> nat;

	public List<HttpRedirection> getHttp() {
		return http;
	}

	public void setHttp(List<HttpRedirection> http) {
		this.http = http;
	}

	public List<NatRedirection> getNat() {
		return nat;
	}

	public void setNat(List<NatRedirection> nat) {
		this.nat = nat;
	}

	@Override
	public String toString() {
		return "Redirections [http=" + http + ", nat=" + nat + "]";
	}
}
