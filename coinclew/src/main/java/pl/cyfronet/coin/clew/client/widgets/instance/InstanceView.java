package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class InstanceView extends Composite implements IInstanceView {
	private static InstanceViewUiBinder uiBinder = GWT.create(InstanceViewUiBinder.class);
	interface InstanceViewUiBinder extends UiBinder<Widget, InstanceView> {}

	public InstanceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}