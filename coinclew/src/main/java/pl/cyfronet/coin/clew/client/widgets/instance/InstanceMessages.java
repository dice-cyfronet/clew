package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.i18n.client.Messages;

public interface InstanceMessages extends Messages {
	String getSpec(String cpu, String ram, String disk);
	String shutdownConfirmation();
}