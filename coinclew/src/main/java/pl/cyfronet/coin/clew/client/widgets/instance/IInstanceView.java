package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInstanceView extends IsWidget {
	public interface IInstancePresenter {
		void onShutdownClicked();
		void onExternalInterfacesClicked();
		void onSave();
		void onReboot();
		void onSaveInPlace();
		void onShowAllIps();
	}

	HasText getName();
//	HasText getSpec();
	String getSpecStanza(String cpu, String ram, String disk);
	void setIp(String ip, boolean multipleInstances);
	HasHTML getLocation();
	void setShutdownBusyState(boolean busy);
	boolean confirmInstanceShutdown();
	void addShutdownControl();
	IsWidget addService(String name, String httpUrl, String httpsUrl, String descriptor, String redirectionId, String httpUrlStatus, String httpsUrlStatus);
	IsWidget addWebApplication(String name, String httpUrl, String httpsUrl, String redirectionId, String httpUrlStatus, String httpsUrlStatus);
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
	HasText getCost();
	void setStatus(String string);
	void enableSave(boolean enable);
	void enableExternalInterfaces(boolean enable);
	void enableCollapsable(boolean enable);
	void collapseDetails();
	void updateHttpStatus(String redirectionId, String status);
	void updateHttpsStatus(String redirectionId, String ststus);
	void setFlavorDetails(String prepaidUntil, String flavorName, Float cpus, Float ram, Float hdd);
	void showDetailsPanel(boolean show);
	void showNoVmsLabel(boolean show);
	String getNoVmsLabel();
	void addRebootControl();
	void enableReboot(boolean enable);
	boolean confirmInstanceReboot();
	void setRebootBusyState(boolean busy);
	void addSaveInPlaceControl();
	boolean saveInPlaceConfirmation();
	String missingApplianceType();
	void setSaveInPlaceBusyState(boolean state);
	void enableSaveInPlace(boolean enable);
	void showIpsModal();
	void addIpToModal(String ip);
}