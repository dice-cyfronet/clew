package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInstanceView extends IsWidget {
	public interface IInstancePresenter {
		void onShutdownClicked();
	}

	HasText getName();
	HasText getSpec();
	String getSpecStanza(String cpu, String ram, String disk);
	HasText getIp();
	HasText getLocation();
	HasText getStatus();
	void setShutdownBusyState(boolean busy);
	boolean confirmInstanceShutdown();
	void addShutdownControl();
	void addService(String url);
	void addWebApplication(String url);
	void addNoServicesLabel();
	void addNoWebApplicationsLabel();
}