package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IExternalInterfacesView extends IsWidget {
	interface IExternalInterfacesPresenter {
		
	}

	void showModal(boolean show);
	void showEndpointsLoadingIndicator(boolean show);
	void showNoEndpointsLabel(boolean show);
	HasWidgets getEndpointsContainer();
	void showExternalInterfacesLoadingIndicator(boolean show);
	void showNoExternalInterfacesLabel(boolean show);
	HasWidgets getExternalInterfaceContainer();
}