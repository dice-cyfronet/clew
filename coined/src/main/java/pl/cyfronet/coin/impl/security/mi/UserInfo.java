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

package pl.cyfronet.coin.impl.security.mi;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UserInfo {
	private String userId;
	private Set<String> roles;
	private String userData;
	private String tokens;

	public UserInfo(String ticket) throws WrongTicketFormatException {
		if (ticket.length() <= 40) {
			throw new WrongTicketFormatException();
		}

		String parts[] = ticket.substring(40).split("!");

		if (parts.length == 2) {
			userId = parts[0];
			tokens = "";
			userData = parts[1];
			roles = new HashSet<String>();
		} else if (parts.length == 3) {
			userId = parts[0];
			tokens = parts[1];
			userData = parts[2];
			roles = new HashSet<String>(Arrays.asList(tokens.split(",")));
		} else {
			throw new WrongTicketFormatException();
		}
	}

	public String getUserId() {
		return userId;
	}

	public Collection<String> getRoles() {
		return roles;
	}

	public String getTokens() {
		return tokens;
	}

	public String getUserData() {
		return userData;
	}
}
