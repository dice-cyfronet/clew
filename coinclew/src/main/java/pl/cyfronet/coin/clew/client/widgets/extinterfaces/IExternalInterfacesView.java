package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import java.util.Map;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IExternalInterfacesView extends IsWidget {
	interface IExternalInterfacesPresenter {
		void onTransportProtocolChanged();
		void onRemoveMapping(String mappingId);
		void onUpdateExternalInterface();
		void onUpdateEndpoint();
		void onRemoveEndpoint(String endpointId);
		void onEditMapping(String mappingId);
		void onEditEndpoint(String endpointId);
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
	IsWidget addMapping(int beforePosition, String mappingId, String serviceName, int targetPort, String transportProtocol, String httpUrl, String httpsUrl, String publicIp, String sourcePort, Map<String, String> properties);
	void removeMappingTemplate(IsWidget widget);
	HasText getExternalInterfaceName();
	void setUpdateExternalInterfaceBusyState(boolean busy);
	HasText getExternalInterfacePort();
	HasValue<String> getApplicationProtocol();
	void displayNameOrPortEmptyMessage();
	void displayWrongPortFormatMessages();
	void clearErrorMessages();
	HasText getEndpointName();
	HasText getInvocationPath();
	HasValue<String> getEndpointType();
	HasValue<String> getTargetPort();
	HasText getEndpointDescription();
	HasText getEndpointDescriptor();
	IsWidget addEndpoint(int beforePosition, String endpointId, String name, boolean secured, String httpUrl, String httpsUrl);
	void removeEndpoint(IsWidget isWidget);
	void addHttpMappingEndpointOption(String id, String serviceName, int targetPort);
	void showEndpointTargetPortHelpBlock(boolean show);
	void clearEndpointTargetPorts();
	void displayEndpointNameInvocationPathOrPortMappingIdEmptyMessage();
	void showCannotRemoveMappingMessage();
	boolean confirmMappingRemoval();
	boolean confirmEndpointRemoval();
	void selectFirstTargetPort();
	HasValue<String> getProxySendTimeout();
	HasValue<String> getProxyReadTimeout();
	void displayWorngProxySendTimeoutMessage();
	void displayWorngProxyReadTimeoutMessage();
	void removeHttpMappingEndpointOption(String mappingId);
	void switchToMappingsTab();
	void enableEndpoints(boolean enable);
	HasValue<Boolean> getSecured();
	void setMappingEditLabel(boolean show);
	void setEndpointEditLabel(boolean show);
}