package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface AtomicServiceMessages extends Messages {
	String editInitialConfigsTooltip();

	String editPropertiesTooltip();

	String authorLabel(String authorName);

	String confirmRemoval();

	String inactiveLabel();

	String editExternalInterfacesTooltip();

	String startInstanceTooltip();

	String noInitialConfigsMessage();

	String savingLabel();

	SafeHtml noComputeSiteLabels();

	String computeSiteLabel(String computeSiteName);

	String anyComputeSiteLabel();
}