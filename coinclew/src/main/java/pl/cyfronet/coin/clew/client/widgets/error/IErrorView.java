package pl.cyfronet.coin.clew.client.widgets.error;

import com.google.gwt.user.client.ui.IsWidget;

public interface IErrorView extends IsWidget {
	interface IErrorPresenter {
	}

	void setMessage(String message);
	String getForbiddenMessage();
	void show();
	void hide();
}