package pl.cyfronet.coin.clew.client.controller.cf.userkey;

import org.fusesource.restygwt.client.Json;

public class NewUserKey {
	private String name;
	@Json(name = "public_key")
	private String publicKey;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
	@Override
	public String toString() {
		return "NewUserKeyRequest [name=" + name + ", publicKey=" + publicKey
				+ "]";
	}
}