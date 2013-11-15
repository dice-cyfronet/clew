package pl.cyfronet.coin.clew.client.widgets.keymanager;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IKeyManagerView extends IsWidget {
	interface IKeyManagerPresenter {
		void onKeyUploadClicked();
		void onClose();
	}

	void showModal(boolean show);
	HasWidgets getKeysContainer();
	void showLoadingProgressIndicator();
	void showNoKeysLabel(boolean show);
	HasText getKeyName();
	HasText getKeyContents();
	void clearMessages();
	void setUploadBusyState(boolean busy);
	void displayNameOrContentsEmptyMessage();
	void displayRequestError(String errorMessage);
}