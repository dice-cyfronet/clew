package pl.cyfronet.coin.portlet.cloudmanager;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class UploadKeyRequest {
	@NotEmpty
	private String keyName;
	private MultipartFile keyBody;
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	public MultipartFile getKeyBody() {
		return keyBody;
	}
	public void setKeyBody(MultipartFile keyBody) {
		this.keyBody = keyBody;
	}
	
	@Override
	public String toString() {
		return "UploadKeyRequest [keyName=" + keyName + ", keyBody=" + keyBody
				+ "]";
	}
}