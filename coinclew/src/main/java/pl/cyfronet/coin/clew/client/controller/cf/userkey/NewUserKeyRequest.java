package pl.cyfronet.coin.clew.client.controller.cf.userkey;

import org.fusesource.restygwt.client.Json;

public class NewUserKeyRequest {
	@Json(name = "user_key")
	private NewUserKey userKey;

	public NewUserKey getUserKey() {
		return userKey;
	}

	public void setUserKey(NewUserKey userKey) {
		this.userKey = userKey;
	}
}