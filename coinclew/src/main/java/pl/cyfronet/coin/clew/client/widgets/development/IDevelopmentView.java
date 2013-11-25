package pl.cyfronet.coin.clew.client.widgets.development;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IDevelopmentView extends IsWidget {
	interface IDevelopmentPresenter {
		void onManageUserKeysClicked();
		void onStartDevInstance();
	}

	void showNoRunningInstancesLabel(boolean show);
	void showNoAtomicServicesLabel(boolean show);
	HasWidgets getAtomicServicesContainer();
	void addAtomicServiceProgressIndicator();
	HasWidgets getInstanceContainer();
	void showInstanceLoadingIndicator(boolean show);
}