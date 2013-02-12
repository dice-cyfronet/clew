package pl.cyfronet.coin.portlet.policymanager;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class UploadPolicyRequest {
	private String policyName;
	private MultipartFile policyBody;
	
	@NotEmpty(message = "Policy name cannot be empty")
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	
	public MultipartFile getPolicyBody() {
		return policyBody;
	}
	public void setPolicyBody(MultipartFile policyBody) {
		this.policyBody = policyBody;
	}
	
	@Override
	public String toString() {
		return "UploadPolicyRequest [policyName=" + policyName
				+ ", policyBody=" + policyBody + "]";
	}
}