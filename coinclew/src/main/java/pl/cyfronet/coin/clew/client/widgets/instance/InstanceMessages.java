package pl.cyfronet.coin.clew.client.widgets.instance;

import com.google.gwt.i18n.client.Messages;

public interface InstanceMessages extends Messages {
	String getSpec(String cpu, String ram, String disk);

	String shutdownConfirmation();

	String webApplicationsLabel();

	String restApplicationsLabel();

	String noServicesLabel();

	String noWebApplicationsLabel();

	String detailsTooltip();

	String shutdownTooltip();

	String externalInterfacesTooltip();

	String saveTooltip();

	String noDescriptorButtonTooltip();

	String otherServicesLabel();

	String noOtherServices();

	String unsatisfiedLabel();

	String sshHelpBlock(String publicIp, String sourcePort);

	String sshHelpHeader();

	String getEmptyDescription();

	String prepaidUntil(String prepaidUntil, String flavorName, Float cpus, Float ram, Float hdd);

	String noVmsLabel();

	String noVmsShortLabel();

	String rebootTooltip();

	String confirmReboot();

	String saveInPlaceTooltip();

	String missingApplianceType();

	String confirmSaveInPlace();

	String initializingLabel();

	String pauseTooltip();

	String resumeTooltip();

	String saveInPlaceElaboratedTooltip();
}
