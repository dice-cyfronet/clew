package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInitialConfigsEditorView extends IsWidget {
	interface IInitialConfigsEditorPresenter {
		
	}

	void showModal(boolean b);
	HasWidgets getContainer();
	void showProgressIndicator();
	void showNoConfigsLabel();
}