package pl.cyfronet.coin.portlet.lobcder;

import javax.xml.bind.annotation.XmlElement;

public class LobcderRestMetadata {
	private String uid;
	private String name;
	private String checksum;
	private String contentType;
	private long creationDate;
	private long lastValidationDate;
	private long sizeBytes;
	private long modificationDate;
	private String owner;
	private String parent;
	private boolean supervised;

	@XmlElement(name = "UID")
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	@XmlElement(name = "contentTypesAsString")
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@XmlElement(name = "createDate")
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	
	
	public long getLastValidationDate() {
		return lastValidationDate;
	}
	public void setLastValidationDate(long lastValidationDate) {
		this.lastValidationDate = lastValidationDate;
	}
	
	@XmlElement(name = "length")
	public long getSizeBytes() {
		return sizeBytes;
	}
	public void setSizeBytes(long sizeBytes) {
		this.sizeBytes = sizeBytes;
	}
	
	@XmlElement(name = "modifiedDate")
	public long getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(long modificationDate) {
		this.modificationDate = modificationDate;
	}
	
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public boolean isSupervised() {
		return supervised;
	}
	public void setSupervised(boolean supervised) {
		this.supervised = supervised;
	}
	
	@Override
	public String toString() {
		return "LobcderRestMetadata [uid=" + uid + ", name=" + name
				+ ", checksum=" + checksum + ", contentType=" + contentType
				+ ", creationDate=" + creationDate + ", lastValidationDate="
				+ lastValidationDate + ", sizeBytes=" + sizeBytes
				+ ", modificationDate=" + modificationDate + ", owner=" + owner
				+ ", parent=" + parent + ", supervised=" + supervised + "]";
	}
}