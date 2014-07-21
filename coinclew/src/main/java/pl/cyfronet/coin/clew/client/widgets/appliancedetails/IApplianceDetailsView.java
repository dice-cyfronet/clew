package pl.cyfronet.coin.clew.client.widgets.appliancedetails;

import java.util.Map;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplianceDetailsView extends IsWidget {
	interface IApplianceDetailsPresenter {
		void onStartInstance();
		void onPreferenceChanged(String applianceTypeId);
	}

	void showModal(boolean show);
	HasValue<Boolean> addKey(String id, String name);
	void setStartBusyState(boolean busy);
	HasText addName(String name);
	HasWidgets getKeyContainer();
	HasWidgets getNameContainer();
	String getDefaultValueLabel();
	HasValue<String> addCores(Map<String, String> options, String value, String applianceTypeId);
	HasValue<String> addRam(Map<String, String> options, String value, String applianceTypeId);
	HasValue<String> addDisk(Map<String, String> options, String value, String applianceTypeId);
	void showFlavorProgress(HasWidgets container, boolean show);
	void showFlavorError(HasWidgets container);
	void showFlavorInformation(HasWidgets container, String name, Integer hourlyCost);
	HasWidgets addFlavorContainer();
	void showKeyProgress(boolean show);
	void showDetailsProgress(boolean show);
	void showPreferencesError(HasWidgets container);
	String getAnyComputeSiteLabel();
	HasValue<String> addComputeSites(Map<String, String> computeSiteLabels, String chosenComputeSiteId);
}