package pl.cyfronet.coin.clew.client.widgets.dev;

import pl.cyfronet.coin.clew.client.widgets.dev.DevPresenter.View;

import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DevPanel extends Composite implements View {
	private static DevPanelUiBinder uiBinder = GWT.create(DevPanelUiBinder.class);
	interface DevPanelUiBinder extends UiBinder<Widget, DevPanel> {}
	
	private Provider<Presenter> presenter;
	
	@UiField Modal addApplianceTypeModal;
	@UiField HTMLPanel applianceTypeList;
	@UiField TextBox applianceTypeName;
	@UiField TextArea applianceTypeDescription;

	@Inject
	public DevPanel(Provider<Presenter> presenter) {
		this.presenter = presenter;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("showNewApplianceTypeModal")
	void onShowNewApplianceTypeModal(ClickEvent event) {
		presenter.get().onShowNewApplianceModal();
	}
	
	@UiHandler("addApplianceType")
	void onAddApplianceType(ClickEvent event) {
		presenter.get().onAddApplianceType();
	}

	@Override
	public void showNewApplianceModal(boolean show) {
		if (show) {
			addApplianceTypeModal.show();
		} else {
			addApplianceTypeModal.hide();
		}
	}

	@Override
	public void showApplianceType(String name, String description) {
		applianceTypeList.add(new HTML("<dl><dt>" + name + "</dt><dd>" + description + "</dd></dl>"));
	}

	@Override
	public HasText getNewApplianceTypeName() {
		return applianceTypeName;
	}

	@Override
	public HasText getNewApplianceTypeDescription() {
		return applianceTypeDescription;
	}
}