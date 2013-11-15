package pl.cyfronet.coin.clew.client.controller.cf.userkey;

public class UserKey extends NewUserKey {
	private String id;
	private String fingerprint;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UserKey [id=" + id + ", getName()=" + getName()
				+ ", getPublicKey()=" + getPublicKey() + "]";
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
}