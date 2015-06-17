package pl.cyfronet.coin.clew.client;

import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.AliasResponseHttpMapping;
import pl.cyfronet.coin.clew.client.widgets.appliancedetails.ApplianceDetailsPresenter;
import pl.cyfronet.coin.clew.client.widgets.appliancetypeeditor.ApplianceTypeEditorPresenter;
import pl.cyfronet.coin.clew.client.widgets.applications.ApplicationsPresenter;
import pl.cyfronet.coin.clew.client.widgets.development.DevelopmentPresenter;
import pl.cyfronet.coin.clew.client.widgets.extinterfaces.ExternalInterfacesEditorPresenter;
import pl.cyfronet.coin.clew.client.widgets.httpmapping.HttpMappingPresenter;
import pl.cyfronet.coin.clew.client.widgets.initialconfigembed.InitialConfigEmbedPresenter;
import pl.cyfronet.coin.clew.client.widgets.initialconfigpicker.InitialConfigurationPickerPresenter;
import pl.cyfronet.coin.clew.client.widgets.initialconfigseditor.InitialConfigsEditorPresenter;
import pl.cyfronet.coin.clew.client.widgets.keymanager.KeyManagerPresenter;
import pl.cyfronet.coin.clew.client.widgets.menu.MenuPresenter;
import pl.cyfronet.coin.clew.client.widgets.root.RootPresenter;
import pl.cyfronet.coin.clew.client.widgets.startinstance.StartInstancePresenter;
import pl.cyfronet.coin.clew.client.widgets.su.SuPresenter;
import pl.cyfronet.coin.clew.client.widgets.workflows.WorkflowsPresenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;

@Events(startPresenter = RootPresenter.class, historyOnStart = true)
public interface MainEventBus extends EventBusWithLookup {
	@Start
	@Event(handlers = {MenuPresenter.class, StartInstancePresenter.class,
			KeyManagerPresenter.class, InitialConfigsEditorPresenter.class,
			InitialConfigEmbedPresenter.class, ExternalInterfacesEditorPresenter.class,
			ApplianceTypeEditorPresenter.class, ApplianceDetailsPresenter.class,
			InitialConfigurationPickerPresenter.class, RootPresenter.class})
	void start();
	
	@Event(handlers = RootPresenter.class)
	void displayError(CloudFacadeError error);
	
	@Event(handlers = RootPresenter.class)
	void setMenu(IsWidget menu);
	
	@Event(handlers = RootPresenter.class)		
	void setBody(IsWidget widget);
	
	@Event(handlers = RootPresenter.class)
	void addPopup(IsWidget widget);
	
	@Event(handlers = {MenuPresenter.class, ApplicationsPresenter.class}, historyConverter = TabHistoryConverter.class)
	void switchToApplicationsView();

	@Event(handlers = {MenuPresenter.class, WorkflowsPresenter.class}, historyConverter = TabHistoryConverter.class)
	void switchToWorkflowsView();
	
	@InitHistory
	@Event(handlers = {MenuPresenter.class, DevelopmentPresenter.class}, historyConverter = TabHistoryConverter.class)
	void switchToDevelopmentView();

	@Event(handlers = StartInstancePresenter.class)
	void showStartInstanceDialog(boolean developmentMode);

	@Event(handlers = StartInstancePresenter.class)
	void hideStartInstanceModal();

	@Event(handlers = {ApplicationsPresenter.class, DevelopmentPresenter.class})
	void removeInstance(String applianceInstanceId);

	@Event(handlers = ApplicationsPresenter.class)
	void refreshInstanceList();

	@Event(handlers = WorkflowsPresenter.class)
	void deactivateWorkflowsRefresh();

	@Event(handlers = WorkflowsPresenter.class)
	void removeApplianceSet(String applianceSetId);

	@Event(handlers = KeyManagerPresenter.class)
	void showKeyManagerDialog();

	@Event(handlers = KeyManagerPresenter.class)
	void removeUserKey(String id);

	@Event(handlers = InitialConfigsEditorPresenter.class)
	void showInitialConfigsEditor(String applianceTypeId);

	@Event(handlers = InitialConfigsEditorPresenter.class)
	void removeInitialConfiguration(String initialConfigurationId);

	@Event(handlers = InitialConfigsEditorPresenter.class)
	void editInitialConfiguration(String initialConfigurationId);

	@Event(handlers = InitialConfigEmbedPresenter.class)
	void startApplications(List<String> initialConfigurationIds, Map<String, List<String>> computeSiteIds, boolean developmentMode, Map<String, String> teams);

	@Event(handlers = ApplicationsPresenter.class)
	void deactivateApplicationsRefresh();

	@Event(handlers = DevelopmentPresenter.class)
	void refreshDevelopmentInstanceList();

	@Event(handlers = ExternalInterfacesEditorPresenter.class)
	void showExternalInterfacesEditor(String applianceInstanceId);

	@Event(handlers = ApplianceTypeEditorPresenter.class)
	void showAtomicServiceEditor(String applianceTypeId, boolean saveMode);

	@Event(handlers = DevelopmentPresenter.class)
	void updateApplianceTypeView(ApplianceType applianceType);

	@Event(handlers = ApplianceDetailsPresenter.class)
	void showApplianceStartDetailsEditorForConfigIds(List<String> initialConfigurationIds, Map<String, List<String>> computeSiteIds, Map<String, String> teams);

	@Event(handlers = ApplianceDetailsPresenter.class)
	void showApplianceStartDetailsEditorForConfigParams(Map<String, Map<String, String>> parameterValues, Map<String, List<String>> computeSiteIds,
			Map<String, String> teams);

	@Event(handlers = DevelopmentPresenter.class)
	void deactivateDevelopmentRefresh();

	@Event(handlers = DevelopmentPresenter.class)
	void externalInterfacesChanged(String applianceInstanceId);

	@Event(handlers = DevelopmentPresenter.class)
	void endpointsChanged(String applianceInstanceId);

	@Event(handlers = DevelopmentPresenter.class)
	void removeApplianceType(String id);

	@Event(handlers = ExternalInterfacesEditorPresenter.class)
	void showExternalInterfacesEditorForApplianceType(String applianceTypeId);

	@Event(handlers = InitialConfigurationPickerPresenter.class)
	void showInitialConfigPicker(List<ApplianceConfiguration> applianceConfigurations, boolean developmentMode);

	@Event(handlers = InitialConfigsEditorPresenter.class)
	void cancelEditInitialConfiguration(String initialConfigurationId);
	
	@Event(handlers = ApplicationsPresenter.class, historyConverter = TabHistoryConverter.class)
	void startInstance(String applianceTypeId);

	@Event(handlers = RootPresenter.class)
	void showStartApplicationProgress(boolean show);

	@Event(handlers = {MenuPresenter.class, SuPresenter.class}, historyConverter = TabHistoryConverter.class)
	void switchToSuView();

	@Event(handlers = MenuPresenter.class)
	void suUserChanged(String suUser);

	@Event(handlers = HttpMappingPresenter.class)
	void httpMappingAliasChanged(String httpMappingId, AliasResponseHttpMapping httpMapping);
}
