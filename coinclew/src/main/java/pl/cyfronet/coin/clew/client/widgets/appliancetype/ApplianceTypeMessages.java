package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface ApplianceTypeMessages extends Messages {
	String emptyDescriptionLabel();
	String noInitialConfigLabel();
	String startTooltip();
	String pickInitConfigLabel();
	String flavorError();
	String flavorInfo(String name, String cost);
	SafeHtml loadingFlavor();
}