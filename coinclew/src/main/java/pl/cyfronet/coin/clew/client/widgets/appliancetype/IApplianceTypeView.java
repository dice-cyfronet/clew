package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplianceTypeView extends IsWidget {
	interface IApplianceTypePresenter {
		void onStartApplianceType();
	}

	HasText getName();
	HasText getDescription();
	void setEmptyDescription();
	void setStartButtonBusyState(boolean busy);
	void addInitialConfigsProgressIndicator();
	void clearInitialConfigsContainer();
	void addNoInitialConfigsLabel();
	void addInitialConfigValue(String configId, String configName);
	void enableControls(boolean b);
	HasValue<Boolean> getChecked();
	void showInitialConfigs();
	HasValue<String> getInitialConfigs();
}