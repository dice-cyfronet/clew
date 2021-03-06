package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import pl.cyfronet.coin.clew.client.widgets.initialconfigseditor.IInitialConfigsEditorView.IInitialConfigsEditorPresenter;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class InitialConfigsEditorView extends Composite implements IInitialConfigsEditorView, ReverseViewInterface<IInitialConfigsEditorPresenter> {
	private static InitialConfigsEditorViewUiBinder uiBinder = GWT.create(InitialConfigsEditorViewUiBinder.class);
	interface InitialConfigsEditorViewUiBinder extends UiBinder<Widget, InitialConfigsEditorView> {}
	
	private IInitialConfigsEditorPresenter presenter;

	@UiField Modal modal;
	@UiField FlowPanel container;
	@UiField InitialConfigsEditorMessages messages;
	@UiField Label errorLabel;
	@UiField TextBox name;
	@UiField TextArea payload;
	@UiField Button process;
	
	public InitialConfigsEditorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void onCloseClicked(ClickEvent event) {
		getPresenter().onClose();
	}
	
	@UiHandler("process")
	void processClicked(ClickEvent event) {
		getPresenter().onProcess();
	}
	
	@Override
	public void setPresenter(IInitialConfigsEditorPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IInitialConfigsEditorPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void showModal(boolean show) {
		if (show) {
			modal.show();
		} else {
			modal.hide();
		}
	}

	@Override
	public HasWidgets getContainer() {
		return container;
	}

	@Override
	public void showProgressIndicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.addStyleName("icon-spin");
		container.add(icon);
	}

	@Override
	public void showNoConfigsLabel() {
		Label label = new Label(messages.noConfigsLabel());
		container.add(label);
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public HasText getPayload() {
		return payload;
	}

	@Override
	public void displayNameEmptyMessage() {
		errorLabel.setText(messages.nameEmpty());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void clearMessages() {
		errorLabel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		errorLabel.setText("");
	}

	@Override
	public void displayNameNotUniqueMessage() {
		errorLabel.setText(messages.nameNotUnique());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void setEditLabel(boolean edit) {
		if (edit) {
			process.setText(messages.getEditLabel());
		} else {
			process.setText(messages.processConfig());
		}
	}

	@Override
	public boolean confirmRemoval() {
		return Window.confirm(messages.removalConfirmMessage());
	}
}