package pl.cyfronet.coin.clew.client.widgets.initialconfigpicker;

import pl.cyfronet.coin.clew.client.widgets.initialconfigpicker.IInitialConfigurationPickerView.IInitialConfigurationPickerPresenter;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class InitialConfigurationPickerView extends Composite implements IInitialConfigurationPickerView, ReverseViewInterface<IInitialConfigurationPickerPresenter> {
	private static InitialConfigurationPickerViewUiBinder uiBinder = GWT.create(InitialConfigurationPickerViewUiBinder.class);
	interface InitialConfigurationPickerViewUiBinder extends UiBinder<Widget, InitialConfigurationPickerView> {}

	private IInitialConfigurationPickerPresenter presenter;
	
	@UiField Modal pickConfigModal;
	@UiField ListBox configListBox;
	
	public InitialConfigurationPickerView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("startInstance")
	void startInstance(ClickEvent event) {
		getPresenter().onStartInstance();
	}
	
	@UiHandler("close")
	void close(ClickEvent event) {
		pickConfigModal.hide();
	}

	@Override
	public void showModal(boolean show) {
		if (show) {
			pickConfigModal.show();
		} else {
			pickConfigModal.hide();
		}
	}

	@Override
	public void addConfig(String configId, String configName) {
		configListBox.addItem(configName, configId);
	}

	@Override
	public void clearConfigs() {
		configListBox.clear();
	}

	@Override
	public void setPresenter(IInitialConfigurationPickerPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IInitialConfigurationPickerPresenter getPresenter() {
		return presenter;
	}

	@Override
	public HasValue<String> getConfig() {
		return new HasValue<String>() {
			@Override
			public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public String getValue() {
				return configListBox.getValue();
			}

			@Override
			public void setValue(String value) {
				configListBox.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				configListBox.setSelectedValue(value);
			}
		};
	}
}