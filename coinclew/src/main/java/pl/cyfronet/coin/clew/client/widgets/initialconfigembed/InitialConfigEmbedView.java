package pl.cyfronet.coin.clew.client.widgets.initialconfigembed;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class InitialConfigEmbedView extends Composite implements IInitialConfigEmbedView {
	private static InitialConfigEmbedViewUiBinder uiBinder = GWT.create(InitialConfigEmbedViewUiBinder.class);
	interface InitialConfigEmbedViewUiBinder extends UiBinder<Widget, InitialConfigEmbedView> {}
	
	@UiField Modal modal;

	public InitialConfigEmbedView() {
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
}