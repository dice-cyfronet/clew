package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.i18n.client.Messages;

public interface InstanceMessages extends Messages {
	String getSpec(String cpu, String ram, String disk);
	String shutdownConfirmation();
	String webApplicationsLabel();
	String restApplicationsLabel();
	String noServicesLabel();
	String noWebApplicationsLabel();
	String descriptorModalTitle();
	String detailsTooltip();
	String shutdownTooltip();
	String externalInterfacesTooltip();
	String saveTooltip();
	String descriptorButtonTooltip();
	String noDescriptorButtonTooltip();
	String otherServicesLabel();
	String noOtherServices();
	String unsatisfiedLabel();
}