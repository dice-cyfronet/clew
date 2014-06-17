package pl.cyfronet.coin.clew.client.widgets.su;

import pl.cyfronet.coin.clew.client.widgets.su.ISuView.ISuPresenter;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class SuView extends Composite implements ISuView, ReverseViewInterface<ISuPresenter> {
	private static SuViewUiBinder uiBinder = GWT.create(SuViewUiBinder.class);
	interface SuViewUiBinder extends UiBinder<Widget, SuView> {}
	
	private ISuPresenter presenter;
	
	@UiField Icon loadingIndicator;
	@UiField ListBox userList;
	@UiField SuViewMessages messages;
	@UiField Label userLabel;

	public SuView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("switchUser")
	void switchUserClicked(ClickEvent event) {
		getPresenter().onSwitchUser();
	}

	@Override
	public void showLoadingIndicator(boolean show) {
		loadingIndicator.setVisible(show);
	}

	@Override
	public void clearUsers() {
		userList.clear();
	}

	@Override
	public void addNoneOption(String value) {
		userList.addItem(messages.suUserNone(), value);
	}

	@Override
	public void addUserOption(String value, String label) {
		userList.addItem(messages.getUserLabel(value, label), value);
	}

	@Override
	public void setPresenter(ISuPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public ISuPresenter getPresenter() {
		return presenter;
	}

	@Override
	public String getSelectedUser() {
		return userList.getValue();
	}

	@Override
	public void setSuUser(String login) {
		if(login == null) {
			userLabel.setText(messages.suUserNone());
		} else {
			userLabel.setText(login);
		}
	}
}