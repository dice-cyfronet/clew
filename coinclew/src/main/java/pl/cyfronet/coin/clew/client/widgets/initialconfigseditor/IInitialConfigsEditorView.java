package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInitialConfigsEditorView extends IsWidget {
	interface IInitialConfigsEditorPresenter {
		void onProcess();
	}

	void showModal(boolean b);
	HasWidgets getContainer();
	void showProgressIndicator();
	void showNoConfigsLabel();
	HasText getName();
	HasText getPayload();
	void displayNameOrPayloadEmptyMessage();
	void clearMessages();
	void displayNameNotUniqueMessage();
	void setEditLabel(boolean edit);
}