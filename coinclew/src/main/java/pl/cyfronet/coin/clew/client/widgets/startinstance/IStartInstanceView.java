package pl.cyfronet.coin.clew.client.widgets.startinstance;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IStartInstanceView extends IsWidget {
	interface IStartInstancePresenter {
		void onStartSelected();
		void onFilterTextChanged();
	}

	void show();
	void showProgressIndicator();
	void clearApplianceTypeContainer();
	void addNoApplianceTypesLabel();
	HasWidgets getApplianceTypeContainer();
	void hide();
	void showNoApplianceTypesSelected();
	void setStartSelectedBusyState(boolean b);
	HasText getFilter();
	void setDevelopmentModeTitle();
	void setPortalModeTitle();
}