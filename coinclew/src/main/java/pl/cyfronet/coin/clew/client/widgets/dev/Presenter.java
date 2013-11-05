package pl.cyfronet.coin.clew.client.widgets.dev;


public interface Presenter {
	void onShowNewApplianceModal();
	void onAddApplianceType();
	void onInitialConfigurationModal(String applianceTypeId);
	void onAddInitialConfiguration();
}