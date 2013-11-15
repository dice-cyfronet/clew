package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class InitialConfigsEditorView extends Composite implements IInitialConfigsEditorView {
	private static InitialConfigsEditorViewUiBinder uiBinder = GWT.create(InitialConfigsEditorViewUiBinder.class);
	interface InitialConfigsEditorViewUiBinder extends UiBinder<Widget, InitialConfigsEditorView> {}

	@UiField Modal modal;
	
	public InitialConfigsEditorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void showModal(boolean show) {
		if (show) {
			modal.show();
		} else {
			modal.hide();
		}
	}
}