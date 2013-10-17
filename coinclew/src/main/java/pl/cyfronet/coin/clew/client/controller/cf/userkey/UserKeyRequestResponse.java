package pl.cyfronet.coin.clew.client.controller.cf.userkey;

import org.fusesource.restygwt.client.Json;

public class UserKeyRequestResponse {
	@Json(name = "user_key")
	private UserKey userKey;

	public UserKey getUserKey() {
		return userKey;
	}

	public void setUserKey(UserKey userKey) {
		this.userKey = userKey;
	}
}