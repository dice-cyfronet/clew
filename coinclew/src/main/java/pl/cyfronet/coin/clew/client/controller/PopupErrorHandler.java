package pl.cyfronet.coin.clew.client.controller;

import com.google.gwt.user.client.Window;

public class PopupErrorHandler {
	public void displayError(String errorMessage) {
		Window.alert(errorMessage);
	}
}