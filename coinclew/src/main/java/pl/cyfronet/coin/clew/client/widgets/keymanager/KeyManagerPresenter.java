package pl.cyfronet.coin.clew.client.widgets.keymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.KeyUploadCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.UserKeysCallback;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKey;
import pl.cyfronet.coin.clew.client.widgets.keymanager.IKeyManagerView.IKeyManagerPresenter;
import pl.cyfronet.coin.clew.client.widgets.userkey.UserKeyPresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = KeyManagerView.class)
public class KeyManagerPresenter extends BasePresenter<IKeyManagerView, MainEventBus> implements IKeyManagerPresenter {
	private CloudFacadeController cloudFacadeController;
	private Map<String, UserKeyPresenter> keyPresenters;

	@Inject
	public KeyManagerPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		keyPresenters = new HashMap<String, UserKeyPresenter>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowKeyManagerDialog() {
		view.showModal(true);
		loadKeys();
	}
	
	public void onRemoveUserKey(String userKeyId) {
		UserKeyPresenter presenter = keyPresenters.get(userKeyId);
		
		if (presenter != null) {
			eventBus.removeHandler(presenter);
			view.getKeysContainer().remove(presenter.getView().asWidget());
			keyPresenters.remove(userKeyId);
		}
	}

	@Override
	public void onKeyUploadClicked() {
		String keyName = view.getKeyName().getText().trim();
		String keyContents = view.getKeyContents().getText().trim();
		view.clearMessages();
		
		if (keyName.isEmpty() || keyContents.isEmpty()) {
			view.displayNameOrContentsEmptyMessage();
		} else {
			view.setUploadBusyState(true);
			cloudFacadeController.uploadUserKey(keyName, keyContents, new KeyUploadCallback() {
				@Override
				public void onSuccess(UserKey userKey) {
					UserKeyPresenter presenter = eventBus.addHandler(UserKeyPresenter.class);
					view.getKeysContainer().add(presenter.getView().asWidget());
					keyPresenters.put(userKey.getId(), presenter);
					presenter.setUserKey(userKey);
					view.setUploadBusyState(false);
					view.showNoKeysLabel(false);
					view.getKeyName().setText("");
					view.getKeyContents().setText("");
				}

				@Override
				public void onError(String errorMessage) {
					view.displayRequestError(errorMessage);
					view.setUploadBusyState(false);
				}});
		}
	}

	@Override
	public void onClose() {
		view.clearMessages();
		view.showModal(false);
	}
	
	private void clearKeyPresenters() {
		for (String userKeyId : keyPresenters.keySet()) {
			eventBus.removeHandler(keyPresenters.get(userKeyId));
		}
		
		keyPresenters.clear();
	}
	
	private void loadKeys() {
		clearKeyPresenters();
		view.getKeysContainer().clear();
		view.showLoadingProgressIndicator();
		cloudFacadeController.getUserKeys(new UserKeysCallback() {
			@Override
			public void processUserKeys(List<UserKey> userKeys) {
				view.getKeysContainer().clear();
				
				if (userKeys.size() == 0) {
					view.getKeysContainer().clear();
					view.showNoKeysLabel(true);
				} else {
					for (UserKey userKey : userKeys) {
						UserKeyPresenter presenter = eventBus.addHandler(UserKeyPresenter.class);
						view.getKeysContainer().add(presenter.getView().asWidget());
						keyPresenters.put(userKey.getId(), presenter);
						presenter.setUserKey(userKey);
					}
				}
			}});
	}
}