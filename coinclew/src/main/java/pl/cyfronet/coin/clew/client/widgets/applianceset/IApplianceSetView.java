package pl.cyfronet.coin.clew.client.widgets.applianceset;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplianceSetView extends IsWidget {
	interface IApplianceSetPresenter {
		void onShutdown();
	}

	HasText getName();
	void showNoInstancesLabel(boolean show);
	HasWidgets getInstanceContainer();
	void setShutdownBusyState(boolean busy);
	boolean confirmShutdown();
	void showEmptyNamePlaceholder();
}