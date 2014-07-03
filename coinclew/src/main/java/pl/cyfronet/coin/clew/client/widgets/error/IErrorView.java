package pl.cyfronet.coin.clew.client.widgets.error;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;

import com.google.gwt.user.client.ui.IsWidget;

public interface IErrorView extends IsWidget {
	interface IErrorPresenter {
	}

	void setError(CloudFacadeError error);
	void show();
	void hide();
}