package pl.cyfronet.coin.clew.client.widgets.userkey;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKey;
import pl.cyfronet.coin.clew.client.widgets.userkey.IUserKeyView.IUserKeyPresenter;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = UserKeyView.class, multiple = true)
public class UserKeyPresenter extends BasePresenter<IUserKeyView, MainEventBus> implements IUserKeyPresenter {
	private UserKey userKey;
	private CloudFacadeController cloudFacadeController;
	
	@Inject
	public UserKeyPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}

	public void setUserKey(UserKey userKey) {
		this.userKey = userKey;
		view.getName().setText(userKey.getName());
		view.getFingerprint().setText(userKey.getFingerprint());
	}

	@Override
	public void onRemoveClicked() {
		if (view.confirmKeyRemoval()) {
			view.setRemoveBusyState(true);
			cloudFacadeController.removeUserKey(userKey.getId(), new Command() {
				@Override
				public void execute() {
					view.setRemoveBusyState(false);
					eventBus.removeUserKey(userKey.getId());
				}
			});
		}
	}
}