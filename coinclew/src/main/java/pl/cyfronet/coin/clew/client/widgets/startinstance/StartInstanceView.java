package pl.cyfronet.coin.clew.client.widgets.startinstance;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class StartInstanceView extends Composite implements IStartInstanceView {
	private static StartInstanceViewUiBinder uiBinder = GWT.create(StartInstanceViewUiBinder.class);
	interface StartInstanceViewUiBinder extends UiBinder<Widget, StartInstanceView> {}
	
	@UiField Modal startInstanceModal;
	@UiField FlowPanel applianceTypeContainer;
	@UiField StartInstanceMessages messages;

	public StartInstanceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void closeClicked(ClickEvent event) {
		startInstanceModal.hide();
	}

	@Override
	public void show() {
		startInstanceModal.show();
	}

	@Override
	public void showProgressIndicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.addStyleName("icon-spin");
		applianceTypeContainer.add(icon);
	}

	@Override
	public void clearApplianceTypeContainer() {
		applianceTypeContainer.clear();
	}

	@Override
	public void addNoApplianceTypesLabel() {
		applianceTypeContainer.add(new Label(messages.noApplianceTypesLabel()));
	}

	@Override
	public HasWidgets getApplianceTypeContainer() {
		return applianceTypeContainer;
	}
}