package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IAtomicServiceView extends IsWidget {
	interface IAtomicServicePresenter {
		void onEditInitialConfigs();
		void onEditProperties();
	}

	HasText getName();
	void updateAuthor(String fullName);
}