package pl.cyfronet.coin.clew.client.widgets.appliancedetails;

import com.google.gwt.i18n.client.Messages;

public interface ApplianceDetailsMessages extends Messages {
	String closeButtonLabel();
	String modalHeader();
	String nameLabel(String name);
	String namePlaceholder();
	String startButtonLabel();
	String keysLabel();
	String defaultValueLabel();
	String coresLabel();
	String ramLabel();
	String diskLabel();
	String loadingFlavor();
	String flavorError();
	String flavorDetails(String name, String cost);
	String preferencesError();
	String anyComputeSiteLabel();
	String computeSiteLabel();
	String anyTeamLabel();
	String teamsLabel();
}