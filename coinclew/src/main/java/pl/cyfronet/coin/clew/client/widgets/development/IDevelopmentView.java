package pl.cyfronet.coin.clew.client.widgets.development;

import com.google.gwt.user.client.ui.IsWidget;

public interface IDevelopmentView extends IsWidget {
	interface IDevelopmentPresenter {
		
	}

	void addNoRunningInstancesLabel();
	void addNoAtomicServicesLabel();
}