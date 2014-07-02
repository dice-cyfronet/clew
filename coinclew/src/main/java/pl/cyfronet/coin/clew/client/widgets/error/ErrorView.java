package pl.cyfronet.coin.clew.client.widgets.error;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ErrorView extends Composite implements IErrorView {
	private static ErrorViewUiBinder uiBinder = GWT.create(ErrorViewUiBinder.class);
	interface ErrorViewUiBinder extends UiBinder<Widget, ErrorView> {}

	public ErrorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField PopupPanel	popup;
	@UiField HTML message;
	@UiField ErrorViewMessages messages;

	@Override
	public void setMessage(String message) {
		this.message.setText(message);
	}

	@Override
	public String getForbiddenMessage() {
		return messages.accessForbidden();
	}

	@Override
	public void show() {
		popup.center();
		popup.setPopupPosition(popup.getPopupLeft(), Window.getScrollTop());
		popup.show();
	}

	@Override
	public void hide() {
		popup.hide();
	}
}