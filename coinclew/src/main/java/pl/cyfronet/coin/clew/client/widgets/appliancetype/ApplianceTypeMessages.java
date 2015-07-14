package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import com.google.gwt.i18n.client.Messages;

public interface ApplianceTypeMessages extends Messages {
	String emptyDescriptionLabel();
	String noInitialConfigLabel();
	String startTooltip();
	String pickInitConfigLabel();
	String flavorError();
	String flavorInfo(String name, String cost);
	String loadingFlavor();
	String pickComputeSiteLabel();
	String noComputeSite();
	String anyComputeSiteLabel();
	String noFlavorInfo();
	String singleComputeSiteLabel(String computeSiteName);
}