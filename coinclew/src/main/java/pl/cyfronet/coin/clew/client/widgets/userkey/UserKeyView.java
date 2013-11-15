package pl.cyfronet.coin.clew.client.widgets.userkey;

import pl.cyfronet.coin.clew.client.widgets.userkey.IUserKeyView.IUserKeyPresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class UserKeyView extends Composite implements IUserKeyView, ReverseViewInterface<IUserKeyPresenter> {
	private static UserKeyViewUiBinder uiBinder = GWT.create(UserKeyViewUiBinder.class);
	interface UserKeyViewUiBinder extends UiBinder<Widget, UserKeyView> {}
	
	private IUserKeyPresenter presenter;
	
	@UiField HTML name;
	@UiField HTML fingerprint;
	@UiField Button remove;

	public UserKeyView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("remove")
	void removeClicked(ClickEvent event) {
		getPresenter().onRemoveClicked();
	}
	
	@Override
	public void setPresenter(IUserKeyPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IUserKeyPresenter getPresenter() {
		return presenter;
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public HasText getFingerprint() {
		return fingerprint;
	}

	@Override
	public void setRemoveBusyState(boolean busy) {
		if (busy) {
			remove.state().loading();
		} else {
			remove.state().reset();
		}
	}
}