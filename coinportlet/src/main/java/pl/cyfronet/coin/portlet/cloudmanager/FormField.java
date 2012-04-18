package pl.cyfronet.coin.portlet.cloudmanager;

public class FormField {
	private String name, value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "FormField [name=" + name + ", value=" + value + "]";
	}
}