package pl.cyfronet.coin.clew.client.widgets.initialconfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class InitialConfigView extends Composite implements IInitialConfigView {
	private static InitialConfigViewUiBinder uiBinder = GWT.create(InitialConfigViewUiBinder.class);
	interface InitialConfigViewUiBinder extends UiBinder<Widget, InitialConfigView> {}
	
	@UiField HTML name;

	public InitialConfigView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasText getName() {
		return name;
	}
}