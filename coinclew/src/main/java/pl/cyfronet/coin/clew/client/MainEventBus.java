package pl.cyfronet.coin.clew.client;

import pl.cyfronet.coin.clew.client.widgets.applications.ApplicationsPresenter;
import pl.cyfronet.coin.clew.client.widgets.menu.MenuPresenter;
import pl.cyfronet.coin.clew.client.widgets.root.RootPresenter;
import pl.cyfronet.coin.clew.client.widgets.startinstance.StartInstancePresenter;
import pl.cyfronet.coin.clew.client.widgets.workflows.WorkflowsPresenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

@Events(startPresenter = RootPresenter.class)
public interface MainEventBus extends EventBus {
	@Start
	@Event(handlers = {MenuPresenter.class, ApplicationsPresenter.class, StartInstancePresenter.class})
	void start();
	
	@Event(handlers = RootPresenter.class)
	void displayError(ErrorCode errorCode);
	
	@Event(handlers = RootPresenter.class)
	void setMenu(IsWidget menu);
	
	@Event(handlers = RootPresenter.class)
	void setBody(IsWidget widget);
	
	@Event(handlers = RootPresenter.class)
	void addPopup(IsWidget widget);
	
	@Event(handlers = {MenuPresenter.class, ApplicationsPresenter.class})
	void switchToApplicationsView();

	@Event(handlers = {MenuPresenter.class, WorkflowsPresenter.class})
	void switchToWorkflowsView();

	@Event(handlers = StartInstancePresenter.class)
	void showStartInstanceDialog();

	@Event(handlers = StartInstancePresenter.class)
	void hideStartInstanceModal();
}