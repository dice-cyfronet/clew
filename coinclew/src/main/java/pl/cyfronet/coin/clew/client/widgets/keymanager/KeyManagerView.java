package pl.cyfronet.coin.clew.client.widgets.keymanager;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.keymanager.IKeyManagerView.IKeyManagerPresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.FileUpload;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteEvent;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Hidden;
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
	@UiField FileUpload keyUpload;
	@UiField Form keyUploadForm;
	@UiField RadioButton keyCopiedRadio;

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
	
	@UiHandler("keyCopiedRadio")
	void keyCopiedRadioSelected(ValueChangeEvent<Boolean> event) {
		if (event.getValue()) {
			getPresenter().onCopiedKeyOptionSelected();
		}
	}
	
	@UiHandler("keyFromFileRadio")
	void keyFromFileRadioSelected(ValueChangeEvent<Boolean> event) {
		if (event.getValue()) {
			getPresenter().onKeyFromFileOptionSelected();
		}
	}
	
	@UiHandler("keyUploadForm")
	void formSubmitted(SubmitCompleteEvent event) {
		getPresenter().onKeyFromFileUploaded();
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
		BootstrapHelpers.setButtonBusyState(uploadKey, busy);
	}

	@Override
	public void displayNameOrContentsEmptyMessage() {
		errorLabel.setText(messages.nameOrContentsEmptyMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void displayUnknownErrorMessage() {
		errorLabel.setText(messages.unknownErrorMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void displayInvalidKeyMessage() {
		errorLabel.setText(messages.invalidKeyMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void enableKeyUpload(boolean enable) {
		keyUpload.setEnabled(enable);
	}

	@Override
	public void enableKeyTextBox(boolean enable) {
		keyPayload.setEnabled(enable);
	}

	@Override
	public void addHiddenField(String name, String value) {
		keyUploadForm.add(new Hidden(name, value));
	}

	@Override
	public boolean isCopiedKey() {
		return keyCopiedRadio.getValue();
	}

	@Override
	public void displayNameOrFileEmptyMessage() {
		errorLabel.setText(messages.nameOrFileEmptyMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public boolean isKeyFileSelected() {
		return keyUpload.getFilename() != null && !keyUpload.getFilename().isEmpty();
	}

	@Override
	public void resetForm() {
		keyUploadForm.reset();
		keyUpload.setEnabled(false);
		keyPayload.setEnabled(true);
	}

	@Override
	public void submitForm() {
		keyUploadForm.submit();
	}

	@Override
	public void setFormAction(String action) {
		keyUploadForm.setAction(action);
	}
}