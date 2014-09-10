package pl.cyfronet.coin.clew.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.Timer;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.widgets.error.ErrorView;

public class PopupErrorHandler {
	private static final Logger log = LoggerFactory.getLogger(PopupErrorHandler.class);
	private ErrorView view;
	private Timer timer;
	
	public PopupErrorHandler() {
		view = new ErrorView();
	}
	
	public void displayError(CloudFacadeError error) {
		log.error(error.getMessage());
		
		view.setError(error);
		view.show();
		
		if(timer != null) {
			timer.cancel();
		}
		
		timer = new Timer() {
			@Override
			public void run() {
				view.hide();
				timer = null;
			}
		};
		timer.schedule(5000);
	}
}