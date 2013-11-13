package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class InstanceView extends Composite implements IInstanceView {
	private static InstanceViewUiBinder uiBinder = GWT.create(InstanceViewUiBinder.class);
	interface InstanceViewUiBinder extends UiBinder<Widget, InstanceView> {}
	
	@UiField HTML name;
	@UiField HTML spec;
	@UiField InstanceMessages messages;
	@UiField HTML ip;
	@UiField HTML location;
	@UiField HTML status;

	public InstanceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public HasText getSpec() {
		return spec;
	}

	@Override
	public String getSpecStanza(String cpu, String ram, String disk) {
		return messages.getSpec(cpu != null ? cpu : "0", ram != null ? ram : "0", disk != null ? disk : "0");
	}

	@Override
	public HasText getIp() {
		return ip;
	}

	@Override
	public HasText getLocation() {
		return location;
	}

	@Override
	public HasText getStatus() {
		return status;
	}
}