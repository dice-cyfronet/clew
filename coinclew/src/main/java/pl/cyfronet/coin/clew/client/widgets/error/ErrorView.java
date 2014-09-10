package pl.cyfronet.coin.clew.client.widgets.error;

import java.util.List;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ErrorView extends Composite implements IErrorView {
	private static ErrorViewUiBinder uiBinder = GWT.create(ErrorViewUiBinder.class);
	interface ErrorViewUiBinder extends UiBinder<Widget, ErrorView> {}
	interface Style extends CssResource {
		String fieldError();
	}

	public ErrorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField PopupPanel	popup;
	@UiField HTML message;
	@UiField ErrorViewMessages messages;
	@UiField HTMLPanel details;
	@UiField FlowPanel detailsList;
	@UiField Style style;

	@Override
	public void setError(CloudFacadeError error) {
		this.message.setText(error.getMessage());
		
		if(error.getDetails() != null && error.getDetails().size() > 0) {
			detailsList.clear();
			
			for(String key : error.getDetails().keySet()) {
				if(error.getDetails().get(key) != null && error.getDetails().get(key).size() > 0) {
					InlineHTML header = new InlineHTML(key + ":");
					header.addStyleName(style.fieldError());
					
					InlineHTML content = new InlineHTML(merge(error.getDetails().get(key)));
					FlowPanel panel = new FlowPanel();
					panel.add(header);
					panel.add(content);
					detailsList.add(panel);
				}
			}
			
			details.setVisible(true);
		} else {
			details.setVisible(false);
		}
	}

	private String merge(List<String> list) {
		StringBuilder builder = new StringBuilder();
		
		for(String item: list) {
			builder.append(item).append(", ");
		}
		
		return builder.substring(0, builder.length() - 2);
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