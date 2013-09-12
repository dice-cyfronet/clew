package pl.cyfronet.coin.clew.client.controller.cf;

public class AtomicService {
	private String id;
	private String name;
	private String description;
	
	public AtomicService(String name, String description) {
		//TODO(DH): temporary
		this.id = name;
		this.name = name;
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}