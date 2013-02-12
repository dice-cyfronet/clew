package pl.cyfronet.coin.portlet.lobcder;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LobcderRestMetadataList {
	@XmlElement(name = "logicalData")
	private List<LobcderRestMetadata> metadataList;

	public List<LobcderRestMetadata> getMetadataList() {
		return metadataList;
	}
	public void setMetadataList(List<LobcderRestMetadata> metadataList) {
		this.metadataList = metadataList;
	}
}