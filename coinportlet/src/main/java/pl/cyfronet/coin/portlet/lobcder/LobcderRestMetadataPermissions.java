package pl.cyfronet.coin.portlet.lobcder;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "permissions")
public class LobcderRestMetadataPermissions {
	private String owner;
	private List<String> readGroups;
	private List<String> writeGroups;

	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@XmlElement(name = "read")
	public List<String> getReadGroups() {
		return readGroups;
	}
	public void setReadGroups(List<String> readGroups) {
		this.readGroups = readGroups;
	}
	
	@Override
	public String toString() {
		return "LobcderRestMetadataPermissions [owner=" + owner
				+ ", readGroups=" + readGroups + "]";
	}
	
	@XmlElement(name = "write")
	public List<String> getWriteGroups() {
		return writeGroups;
	}
	public void setWriteGroups(List<String> writeGroups) {
		this.writeGroups = writeGroups;
	}
}