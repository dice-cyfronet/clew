package pl.cyfronet.coin.clew.client.controller.cf.userkey;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class UserKeysResponse {
	@Json(name = "user_keys")
	private List<UserKey> userKeys;

	public List<UserKey> getUserKeys() {
		return userKeys;
	}

	public void setUserKeys(List<UserKey> userKeys) {
		this.userKeys = userKeys;
	}
}