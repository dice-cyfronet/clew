package pl.cyfronet.coin.clew.client.widgets.appliancedetails;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplianceDetailsView extends IsWidget {
	interface IApplianceDetailsPresenter {
		void onStartInstance();
	}

	void showModal(boolean show);
	HasValue<Boolean> addKey(String id, String name);
	void setStartBusyState(boolean busy);
	HasText addName(String name);
	HasWidgets getContainer();
}