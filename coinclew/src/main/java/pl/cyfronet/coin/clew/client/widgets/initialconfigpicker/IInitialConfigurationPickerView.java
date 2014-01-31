package pl.cyfronet.coin.clew.client.widgets.initialconfigpicker;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInitialConfigurationPickerView extends IsWidget {
	interface IInitialConfigurationPickerPresenter {
		void onStartInstance();
	}

	void showModal(boolean show);
	void addConfig(String configId, String configName);
	void clearConfigs();
	HasValue<String> getConfig();
}