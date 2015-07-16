package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplianceTypeView extends IsWidget {
	interface IApplianceTypePresenter {
		void onStartApplianceType();
		void onComputeSiteChanged();
	}

	HasText getName();
	HasText getDescription();
	void setEmptyDescription();
	void setStartButtonBusyState(boolean busy);
	void addInitialConfigsProgressIndicator();
	void clearInitialConfigsContainer();
	void addNoInitialConfigsLabel();
	void addInitialConfigValue(String configId, String configName);
	void enableControls(boolean b);
	HasValue<Boolean> getChecked();
	void showInitialConfigs();
	HasValue<String> getInitialConfigs();
	void showFlavorProgress(boolean show);
	void showFlavorInformation(String name, Integer hourlyCost);
	void showFlavorError();
	void showComputeSiteProgressIndicator(boolean show);
	void showNoComputeSitesMessage();
	void addComputeSite(String computeSiteId, String computeSiteName);
	void showComputeSiteSelector();
	String getAnyComputeSiteLabel();
	HasValue<String> getComputeSites();
	void showNoFlavorInformation();
	void showTeamSelector();
	void addTeam(String teamId, String teamName);
	String getAnyTeamLabel();
	HasValue<String> getTeams();
	void enableStartButton(boolean disable);
}