package pl.cyfronet.coin.clew.client.widgets.keymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.ClewProperties;
import pl.cyfronet.coin.clew.client.CloudFacadeEndpointProperty;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.KeyUploadCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.UserKeysCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeErrorCodes;
import pl.cyfronet.coin.clew.client.controller.cf.userkey.UserKey;
import pl.cyfronet.coin.clew.client.widgets.keymanager.IKeyManagerView.IKeyManagerPresenter;
import pl.cyfronet.coin.clew.client.widgets.userkey.UserKeyPresenter;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = KeyManagerView.class)
public class KeyManagerPresenter extends BasePresenter<IKeyManagerView, MainEventBus> implements IKeyManagerPresenter {
	private CloudFacadeController cloudFacadeController;
	private Map<String, UserKeyPresenter> keyPresenters;
	private MiTicketReader ticketReader;
	private ClewProperties properties;
	private CloudFacadeEndpointProperty cloudFacadeEndpointProperty;

	@Inject
	public KeyManagerPresenter(CloudFacadeController cloudFacadeController, MiTicketReader ticketReader,
			ClewProperties properties, CloudFacadeEndpointProperty cloudFacadeEndpointProperty) {
		this.cloudFacadeController = cloudFacadeController;
		this.ticketReader = ticketReader;
		this.properties = properties;
		this.cloudFacadeEndpointProperty = cloudFacadeEndpointProperty;
		keyPresenters = new HashMap<String, UserKeyPresenter>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
		
		String ticket = ticketReader.getTicket();
		
		if (ticket != null) {
			view.addHiddenField("mi_ticket", ticket);
		} else {
			if(Window.Location.getParameter("private_token") != null) {
				view.addHiddenField("private_token", Window.Location.getParameter("private_token"));
			} else {
				view.addHiddenField("private_token", ticketReader.getCfToken());
			}
		}
		
		view.setFormAction(cloudFacadeEndpointProperty.getCloudFacadeEndpoint() + properties.userKeyUploadPath());
	}
	
	public void onShowKeyManagerDialog() {
		view.resetForm();
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
		view.clearMessages();
		
		if (view.isCopiedKey()) {
			String keyContents = view.getKeyContents().getText().trim();
			
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
						view.resetForm();
					}

					@Override
					public void onError(CloudFacadeErrorCodes errorCode) {
						switch (errorCode) {
							case UserKeyInvalid:
								view.displayInvalidKeyMessage();
							break;
							case UnknownError:
								view.displayUnknownErrorMessage();
						}
						
						view.setUploadBusyState(false);
					}});
			}
		} else {
			if (!view.isKeyFileSelected() || keyName.isEmpty()) {
				view.displayNameOrFileEmptyMessage();
			} else {
				view.setUploadBusyState(true);
				view.submitForm();
			}
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

	@Override
	public void onKeyFromFileOptionSelected() {
		view.enableKeyTextBox(false);
		view.enableKeyUpload(true);
	}

	@Override
	public void onCopiedKeyOptionSelected() {
		view.enableKeyTextBox(true);
		view.enableKeyUpload(false);
	}

	@Override
	public void onKeyFromFileUploaded() {
		view.setUploadBusyState(false);
		view.resetForm();
		loadKeys();
	}
}