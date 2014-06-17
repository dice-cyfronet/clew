package pl.cyfronet.coin.clew.client.widgets.su;

import com.google.gwt.user.client.ui.IsWidget;

public interface ISuView extends IsWidget{
	public interface ISuPresenter {
		void onSwitchUser();
	}

	void showLoadingIndicator(boolean show);
	void clearUsers();
	void addNoneOption(String value);
	void addUserOption(String value, String label);
	String getSelectedUser();
	void setSuUser(String login);
}