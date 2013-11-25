package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInstanceView extends IsWidget {
	public interface IInstancePresenter {
		void onShutdownClicked();
		void onExternalInterfacesClicked();
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
	void addService(String name, String httpUrl, String httpsUrl, String descriptor);
	void addWebApplication(String name, String httpUrl, String httpsUrl);
	void addNoServicesLabel();
	void addNoWebApplicationsLabel();
	void addExternalInterfacesControl();
}