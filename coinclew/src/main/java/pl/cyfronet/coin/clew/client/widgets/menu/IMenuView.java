package pl.cyfronet.coin.clew.client.widgets.menu;

import com.google.gwt.user.client.ui.IsWidget;

public interface IMenuView extends IsWidget {
	public interface IMenuPresenter {
		void onApplicationsMenuItemClicked();
		void onWorkflowsMenuItemClicked();
		void onDevelopmentMenuItemClicked();
		void onSuMenuItemClicked();
	}

	void activateWorkflowsMenuItem(boolean activate);
	void activateApplicationsMenuItem(boolean activate);
	void activateDevelopmentMenuItem(boolean activate);
	void showDevTab(boolean show);
	void showSuTab(boolean show);
	void activateSuMenuItem(boolean activate);
	void seSuUSer(String suUser);
}