package pl.cyfronet.coin.clew.client.widgets.dev;

import pl.cyfronet.coin.clew.client.widgets.dev.DevPresenter.View;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DevPanel extends Composite implements View {
	private static DevPanelUiBinder uiBinder = GWT.create(DevPanelUiBinder.class);
	interface DevPanelUiBinder extends UiBinder<Widget, DevPanel> {}
	
	private Provider<Presenter> presenter;
	private DevMessages messages;
	
	@UiField Modal addApplianceTypeModal;
	@UiField FlexTable applianceTypeList;
	@UiField TextBox applianceTypeName;
	@UiField TextArea applianceTypeDescription;
	@UiField Modal initialConfigurationsModal;
	@UiField FlexTable initialConfigurationList;
	@UiField Label noInitialConfigurationsLabel;
	@UiField TextBox initialConfigurationName;
	@UiField TextArea initialConfigurationPayload;
	@UiField Label initialConfigurationErrorLabel;

	@Inject
	public DevPanel(Provider<Presenter> presenter, DevMessages messages) {
		this.presenter = presenter;
		this.messages = messages;
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
	
	@UiHandler("closeInitialConfigurationModal")
	void onCloseInitialConfigurationModal(ClickEvent event) {
		initialConfigurationsModal.hide();
	}
	
	@UiHandler("addInitialConfiguration")
	void onAddInitialConfiguration(ClickEvent event) {
		presenter.get().onAddInitialConfiguration();
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
	public void addApplianceType(final String applianceTypeId, String name, String description) {
		int newRowIndex = applianceTypeList.getRowCount();
		applianceTypeList.setText(newRowIndex, 0, name);
		applianceTypeList.setText(newRowIndex, 1, description);
		
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.get().onInitialConfigurationModal(applianceTypeId);
			}
		};
		Button initialConfigurationButton = new Button("Initial configurations", clickHandler);
		applianceTypeList.setWidget(newRowIndex, 2, initialConfigurationButton);
	}

	@Override
	public HasText getNewApplianceTypeName() {
		return applianceTypeName;
	}

	@Override
	public HasText getNewApplianceTypeDescription() {
		return applianceTypeDescription;
	}

	@Override
	public void addInitialConfiguration(String applianceTypeId, String name, String payload) {
		int nextRowIndex = initialConfigurationList.getRowCount();
		initialConfigurationList.setText(nextRowIndex, 0, name);
		initialConfigurationList.setText(nextRowIndex, 1, payload);
	}

	@Override
	public void showInitialConfigurationModel(boolean show) {
		if (show) {
			initialConfigurationsModal.show();
		} else {
			initialConfigurationsModal.hide();
		}
	}

	@Override
	public void showNoInitialconfigurationsLabel(boolean show) {
		noInitialConfigurationsLabel.setVisible(show);
	}

	@Override
	public HasText getInitialConfigurationName() {
		return initialConfigurationName;
	}

	@Override
	public HasText getInitialConfigurationPayload() {
		return initialConfigurationPayload;
	}

	@Override
	public void displayInitialConfigurationNameOrPayloadEmptyMessage() {
		initialConfigurationErrorLabel.setText(messages.initialConfigurationNameOrPayloadEmpty());
	}
}