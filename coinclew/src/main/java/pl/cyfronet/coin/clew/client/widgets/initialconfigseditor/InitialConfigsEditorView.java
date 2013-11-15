package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class InitialConfigsEditorView extends Composite implements IInitialConfigsEditorView {
	private static InitialConfigsEditorViewUiBinder uiBinder = GWT.create(InitialConfigsEditorViewUiBinder.class);
	interface InitialConfigsEditorViewUiBinder extends UiBinder<Widget, InitialConfigsEditorView> {}

	@UiField Modal modal;
	@UiField FlowPanel container;
	@UiField InitialConfigsEditorMessages messages;
	
	public InitialConfigsEditorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void onCloseClicked(ClickEvent event) {
		modal.hide();
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
}