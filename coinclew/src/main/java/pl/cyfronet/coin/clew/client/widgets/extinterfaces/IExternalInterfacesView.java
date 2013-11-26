package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IExternalInterfacesView extends IsWidget {
	interface IExternalInterfacesPresenter {
		void onTransportProtocolChanged();
		void onRemoveMapping(String mappingId);
		void onAddExternalInterface();
	}

	void showModal(boolean show);
	void showEndpointsLoadingIndicator(boolean show);
	void showNoEndpointsLabel(boolean show);
	HasWidgets getEndpointsContainer();
	void showExternalInterfacesLoadingIndicator(boolean show);
	void showNoExternalInterfacesLabel(boolean show);
	HasWidgets getExternalInterfaceContainer();
	HasValue<String> getTransportProtocol();
	void setApplicationProtocolEnabled(boolean enabled);
	IsWidget addMapping(String mappingId, String serviceName, int targetPort, String transportProtocol, String httpUrl, String httpsUrl, String publicIp, String sourcePort);
	void removeMappingTemplate(IsWidget widget);
	HasText getExternalInterfaceName();
	void setAddExternalInterfaceBusyState(boolean busy);
	HasText getExternalInterfacePort();
	HasValue<String> getApplicationProtocol();
	void displayNameOrPortEmptyMessage();
	void displayWrongPortFormatMessages();
	void clearErrorMessages();
}