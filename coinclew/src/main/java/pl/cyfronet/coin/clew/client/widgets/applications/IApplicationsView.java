package pl.cyfronet.coin.clew.client.widgets.applications;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface IApplicationsView extends IsWidget {
	public interface IApplicationsPresenter {
		void onStartInstance();
	}

	HasWidgets getInstanceContainer();
	void showLoadingIndicator(boolean show);
	void showNoInstancesLabel(boolean show);
	void showHeaderRow(boolean b);
	void showNoInitialConfigurationsMessage();
	void insertInstance(Widget widget, int index);
}