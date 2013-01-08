package pl.cyfronet.coin.portlet.datamanager;

import org.hibernate.validator.constraints.NotEmpty;


public class CreateDirectoryRequest {
	@NotEmpty private String directoryName;

	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	@Override
	public String toString() {
		return "CreateDirectoryRequest [directoryName=" + directoryName + "]";
	}
}