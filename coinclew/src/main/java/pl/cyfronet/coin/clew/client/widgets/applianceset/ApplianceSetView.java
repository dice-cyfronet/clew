package pl.cyfronet.coin.clew.client.widgets.applianceset;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class ApplianceSetView extends Composite implements IApplianceSetView {
	private static AppliancesetViewUiBinder uiBinder = GWT.create(AppliancesetViewUiBinder.class);
	interface AppliancesetViewUiBinder extends UiBinder<Widget, ApplianceSetView> {}

	@UiField HTML name;
	
	public ApplianceSetView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasText getName() {
		return name;
	}
}