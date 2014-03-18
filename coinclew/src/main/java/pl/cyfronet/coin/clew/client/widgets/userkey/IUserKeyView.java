package pl.cyfronet.coin.clew.client.widgets.userkey;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IUserKeyView extends IsWidget {
	interface IUserKeyPresenter {
		void onRemoveClicked();
	}

	HasText getName();
	HasText getFingerprint();
	void setRemoveBusyState(boolean busy);
	boolean confirmKeyRemoval();
	void showRemovalError();
}