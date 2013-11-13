package pl.cyfronet.coin.clew.client.widgets.menu;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.widgets.menu.IMenuView.IMenuPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = MenuView.class)
public class MenuPresenter extends BasePresenter<IMenuView, MainEventBus> implements IMenuPresenter {
	public void onStart() {
		eventBus.setMenu(view);
		view.activateApplicationsMenuItem(true);
	}
	
	public void onSwitchToWorkflowsView() {
		view.activateApplicationsMenuItem(false);
		view.activateWorkflowsMenuItem(true);
	}
	
	public void onSwitchToApplicationsView() {
		view.activateApplicationsMenuItem(true);
		view.activateWorkflowsMenuItem(false);
		eventBus.deactivateWorkflowsRefresh();
	}

	@Override
	public void onApplicationsMenuItemClicked() {
		eventBus.switchToApplicationsView();
	}

	@Override
	public void onWorkflowsMenuItemClicked() {
		eventBus.switchToWorkflowsView();
	}
}