package pl.cyfronet.coin.clew.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.widgets.error.ErrorView;

public class PopupErrorHandler {
	private static final Logger log = LoggerFactory.getLogger(PopupErrorHandler.class);
	private ErrorView view;
	
	public PopupErrorHandler() {
		view = new ErrorView(this);
	}
	
	public void displayError(CloudFacadeError error) {
		log.error(error.getMessage());
		
		view.setError(error);
		view.show();
	}

	public void onClose() {
		view.hide();
	}
}