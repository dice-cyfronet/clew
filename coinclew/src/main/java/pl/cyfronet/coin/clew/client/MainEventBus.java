package pl.cyfronet.coin.clew.client;

import java.util.List;

import pl.cyfronet.coin.clew.client.widgets.applications.ApplicationsPresenter;
import pl.cyfronet.coin.clew.client.widgets.development.DevelopmentPresenter;
import pl.cyfronet.coin.clew.client.widgets.initialconfigembed.InitialConfigEmbedPresenter;
import pl.cyfronet.coin.clew.client.widgets.initialconfigseditor.InitialConfigsEditorPresenter;
import pl.cyfronet.coin.clew.client.widgets.keymanager.KeyManagerPresenter;
import pl.cyfronet.coin.clew.client.widgets.menu.MenuPresenter;
import pl.cyfronet.coin.clew.client.widgets.root.RootPresenter;
import pl.cyfronet.coin.clew.client.widgets.startinstance.StartInstancePresenter;
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
			InitialConfigEmbedPresenter.class})
	void start();
	
	@Event(handlers = RootPresenter.class)
	void displayError(ErrorCode errorCode);
	
	@Event(handlers = RootPresenter.class)
	void setMenu(IsWidget menu);
	
	@Event(handlers = RootPresenter.class)
	void setBody(IsWidget widget);
	
	@Event(handlers = RootPresenter.class)
	void addPopup(IsWidget widget);
	
	@InitHistory
	@Event(handlers = {MenuPresenter.class, ApplicationsPresenter.class}, historyConverter = TabHistoryConverter.class)
	void switchToApplicationsView();

	@Event(handlers = {MenuPresenter.class, WorkflowsPresenter.class}, historyConverter = TabHistoryConverter.class)
	void switchToWorkflowsView();
	
	@Event(handlers = {MenuPresenter.class, DevelopmentPresenter.class}, historyConverter = TabHistoryConverter.class)
	void switchToDevelopmentView();

	@Event(handlers = StartInstancePresenter.class)
	void showStartInstanceDialog(boolean developmentMode);

	@Event(handlers = StartInstancePresenter.class)
	void hideStartInstanceModal();

	@Event(handlers = ApplicationsPresenter.class)
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
	void startApplications(List<String> initialConfigurationIds, boolean developmentMode);

	@Event(handlers = ApplicationsPresenter.class)
	void deactivateApplicationsRefresh();

	@Event(handlers = DevelopmentPresenter.class)
	void refreshDevelopmentInstanceList();
}