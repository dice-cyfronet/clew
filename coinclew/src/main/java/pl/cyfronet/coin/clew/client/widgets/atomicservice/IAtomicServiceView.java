package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IAtomicServiceView extends IsWidget {
	interface IAtomicServicePresenter {
		void onEditInitialConfigs();
		void onEditProperties();
		void onRemove();
		void onEditExternalInterfaces();
		void onStartInstance();
	}

	HasText getName();
	void addRemoveButton();
	void setRemoveBusyState(boolean busy);
	boolean confirmRemoval();
	void showInactiveLabel(boolean active, boolean saving);
	HasHTML getDescription();
	void setStartInstanceBusyState(boolean busy);
	void showNoInitialConfigurationsMessage();
	void enableStartButton(boolean enable);
}