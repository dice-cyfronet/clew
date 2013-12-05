package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import com.google.gwt.i18n.client.Messages;

public interface ExternalInterfacesEditorMessages extends Messages {
	String modalHeader();
	String closeButtonLabel();
	String externalInterfacesTabLabel();
	String endpointsTabLabel();
	String externalInterfaceName();
	String externalInterfacePlaceholder();
	String externalInterfacePortNumber();
	String externalInterfacePortNumberPlaceholder();
	String externalInterfaceTransportProtocol();
	String addExternalInterfaceButtonLabel();
	String externalInterfaceHttpType();
	String externalInterfaceHttpsType();
	String externalInterfaceHttpAndHttpsType();
	String noExternalInterfacesLabel();
	String noEndpointsLabel();
	String externalInterfaceUdpProtocol();
	String externalInterfaceTcpProtocol();
	String externalInterfaceNoneType();
	String getTcpUdpMapping(String publicIp, String sourcePort, int targetPort);
	String externalInterfaceApplicationProtocol();
	String wrongPortFormat();
	String nameOrPortEmpty();
	String endpointTypeLabel();
	String restEndpointType();
	String wsEndpointType();
	String webappEndpointType();
	String endpointNamePlaceholder();
	String endpointName();
	String endpointInvocationPath();
	String endpointInvocationPathPlaceholder();
	String endpointTargetPort();
	String endpointDescription();
	String endpointDescriptionPlaceholder();
	String endpointDescriptor();
	String endpointDescriptorPlaceholder();
	String addEndpointButtonLabel();
	String endpointTargetPortHelp();
	String endpointNameInvocationPathOrPortMappingIdEmptyMessage();
	String cannotRemoveMappingEndpointsExist();
	String confirmMappingRemoval();
	String confirmEndpointRemoval();
}