package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInstanceView extends IsWidget {
	public interface IInstancePresenter {
		void onShutdownClicked();
		void onExternalInterfacesClicked();
		void onSave();
	}

	HasText getName();
//	HasText getSpec();
	String getSpecStanza(String cpu, String ram, String disk);
	HasHTML getIp();
	HasText getLocation();
	HasHTML getStatus();
	void setShutdownBusyState(boolean busy);
	boolean confirmInstanceShutdown();
	void addShutdownControl();
	IsWidget addService(String name, String httpUrl, String httpsUrl, String descriptor);
	IsWidget addWebApplication(String name, String httpUrl, String httpsUrl);
	void showNoServicesLabel(boolean show);
	void showNoWebApplicationsLabel(boolean show);
	void addExternalInterfacesControl();
	void addSaveControl();
	void removeWebapp(IsWidget remove);
	void removeService(IsWidget widget);
	IsWidget addOtherService(String serviceName, String publicIp, String port, String helpBlock);
	void showNoOtherServicesLabel(boolean show);
	void removeOtherService(IsWidget remove);
	void setUnsatisfiedState(String stateExplanation);
	String getSshHelpBlock(String publicIp, String sourcePort);
	void setNoDescription();
	HasText getDescription();
}