package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IAtomicServiceView extends IsWidget {
	interface IAtomicServicePresenter {
		void onEditInitialConfigs();
		void onEditProperties();
		void onRemove();
	}

	HasText getName();
	void updateAuthor(String fullName);
	void addRemoveButton();
	void setRemoveBusyState(boolean busy);
	boolean confirmRemoval();
}