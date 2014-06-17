package pl.cyfronet.coin.clew.client.widgets.menu;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.widgets.menu.IMenuView.IMenuPresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = MenuView.class)
public class MenuPresenter extends BasePresenter<IMenuView, MainEventBus> implements IMenuPresenter {
	private MiTicketReader ticketReader;

	@Inject
	public MenuPresenter(MiTicketReader ticketReader) {
		this.ticketReader = ticketReader;
	}
	
	public void onStart() {
		authorizeMenuItems();
		eventBus.setMenu(view);
		view.activateApplicationsMenuItem(true);
	}

	public void onSwitchToWorkflowsView() {
		view.activateApplicationsMenuItem(false);
		view.activateWorkflowsMenuItem(true);
		view.activateDevelopmentMenuItem(false);
		view.activateSuMenuItem(false);
		eventBus.deactivateApplicationsRefresh();
		eventBus.deactivateDevelopmentRefresh();
	}
	
	public void onSwitchToApplicationsView() {
		view.activateApplicationsMenuItem(true);
		view.activateWorkflowsMenuItem(false);
		view.activateDevelopmentMenuItem(false);
		view.activateSuMenuItem(false);
		eventBus.deactivateWorkflowsRefresh();
		eventBus.deactivateDevelopmentRefresh();
	}
	
	public void onSwitchToDevelopmentView() {
		if (ticketReader.isDeveloper()) {
			view.activateApplicationsMenuItem(false);
			view.activateWorkflowsMenuItem(false);
			view.activateDevelopmentMenuItem(true);
			view.activateSuMenuItem(false);
			eventBus.deactivateWorkflowsRefresh();
			eventBus.deactivateApplicationsRefresh();
		} else {
			eventBus.switchToApplicationsView();
		}
	}

	@Override
	public void onApplicationsMenuItemClicked() {
		eventBus.switchToApplicationsView();
	}

	@Override
	public void onWorkflowsMenuItemClicked() {
		eventBus.switchToWorkflowsView();
	}

	@Override
	public void onDevelopmentMenuItemClicked() {
		eventBus.switchToDevelopmentView();
	}
	
	@Override
	public void onSuMenuItemClicked() {
		eventBus.switchToSuView();
	}
	
	public void onSwitchToSuView() {
		view.activateApplicationsMenuItem(false);
		view.activateWorkflowsMenuItem(false);
		view.activateDevelopmentMenuItem(false);
		view.activateSuMenuItem(true);
		eventBus.deactivateApplicationsRefresh();
		eventBus.deactivateDevelopmentRefresh();
	}
	
	public void onSuUserChanged(String suUser) {
		view.seSuUSer(suUser);
	}
	
	private void authorizeMenuItems() {
		if(!ticketReader.isDeveloper()) {
			view.showDevTab(false);
		}
		
		if(!ticketReader.isCloudAdmin()) {
			view.showSuTab(false);
		}
	}
}