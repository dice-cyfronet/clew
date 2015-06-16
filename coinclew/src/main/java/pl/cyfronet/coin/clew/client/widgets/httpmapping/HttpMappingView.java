package pl.cyfronet.coin.clew.client.widgets.httpmapping;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class HttpMappingView extends Composite implements IHttpMappingView {
	private static HttpMappingViewUiBinder uiBinder = GWT.create(HttpMappingViewUiBinder.class);
	interface HttpMappingViewUiBinder extends UiBinder<Widget, HttpMappingView> {}

	public HttpMappingView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}