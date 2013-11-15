package pl.cyfronet.coin.clew.client.widgets.keymanager;

import pl.cyfronet.coin.clew.client.widgets.keymanager.IKeyManagerView.IKeyManagerPresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class KeyManagerView extends Composite implements IKeyManagerView, ReverseViewInterface<IKeyManagerPresenter> {
	private static KeyManagerViewUiBinder uiBinder = GWT.create(KeyManagerViewUiBinder.class);
	interface KeyManagerViewUiBinder extends UiBinder<Widget, KeyManagerView> {}
	
	private IKeyManagerPresenter presenter;
	private Label noKeysLabel;
	
	@UiField Modal keyModal;
	@UiField FlowPanel keyContainer;
	@UiField KeyManagerMessages messages;
	@UiField TextBox keyName;
	@UiField TextArea keyPayload;
	@UiField Label errorLabel;
	@UiField Button uploadKey;

	public KeyManagerView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void closeClicked(ClickEvent event) {
		getPresenter().onClose();
	}
	
	@UiHandler("uploadKey")
	void uploadKeyClicked(ClickEvent event) {
		getPresenter().onKeyUploadClicked();
		event.preventDefault();
	}
	
	@Override
	public void setPresenter(IKeyManagerPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IKeyManagerPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void showModal(boolean show) {
		if (show) {
			keyModal.show();
		} else {
			keyModal.hide();
		}
	}

	@Override
	public HasWidgets getKeysContainer() {
		return keyContainer;
	}

	@Override
	public void showLoadingProgressIndicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.addStyleName("icon-spin");
		keyContainer.add(icon);
	}

	@Override
	public void showNoKeysLabel(boolean show) {
		if (show) {
			if (noKeysLabel == null) {
				noKeysLabel = new Label(messages.noUserKeysLabel());
				keyContainer.add(noKeysLabel);
			}
		} else {
			if (noKeysLabel != null) {
				keyContainer.remove(noKeysLabel);
				noKeysLabel = null;
			}
		}
	}

	@Override
	public HasText getKeyName() {
		return keyName;
	}

	@Override
	public HasText getKeyContents() {
		return keyPayload;
	}

	@Override
	public void clearMessages() {
		errorLabel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		errorLabel.setText("");
	}

	@Override
	public void setUploadBusyState(boolean busy) {
		if (busy) {
			uploadKey.state().loading();
		} else {
			uploadKey.state().reset();
		}
	}

	@Override
	public void displayNameOrContentsEmptyMessage() {
		errorLabel.setText(messages.nameOrContentsEmptyMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void displayRequestError(String errorMessage) {
		errorLabel.setText(errorMessage);
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}
}