package pl.cyfronet.coin.portlet.cloudmanager;

import org.hibernate.validator.constraints.NotEmpty;

public class UploadKeyRequest {
	@NotEmpty
	private String keyName;
	@NotEmpty
	private String keyBody;
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	public String getKeyBody() {
		return keyBody;
	}
	public void setKeyBody(String keyBody) {
		this.keyBody = keyBody;
	}
	
	@Override
	public String toString() {
		return "UploadKeyRequest [keyName=" + keyName + ", keyBody=" + keyBody
				+ "]";
	}
}