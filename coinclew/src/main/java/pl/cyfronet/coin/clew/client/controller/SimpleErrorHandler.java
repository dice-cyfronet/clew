package pl.cyfronet.coin.clew.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleErrorHandler {
	private static final Logger log = LoggerFactory.getLogger(SimpleErrorHandler.class);
	
	public void displayError(String errorMessage) {
		log.error(errorMessage);
	}
}