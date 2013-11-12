package pl.cyfronet.coin.clew.client.widgets.applications;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplicationsView extends IsWidget {
	public interface IApplicationsPresenter {
		
	}

	HasWidgets getInstanceContainer();
	void showLoadingInicator();
	void clearInstanceContainer();
	void addNoInstancesLabel();
}