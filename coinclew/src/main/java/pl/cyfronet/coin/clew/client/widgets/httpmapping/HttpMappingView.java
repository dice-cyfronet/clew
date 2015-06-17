package pl.cyfronet.coin.clew.client.widgets.httpmapping;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.httpmapping.IHttpMappingView.IHttpMappingPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class HttpMappingView extends Composite implements IHttpMappingView, ReverseViewInterface<IHttpMappingPresenter> {
	private static HttpMappingViewUiBinder uiBinder = GWT.create(HttpMappingViewUiBinder.class);
	interface HttpMappingViewUiBinder extends UiBinder<Widget, HttpMappingView> {}
	
	private IHttpMappingPresenter presenter;
	
	@UiField com.google.gwt.user.client.ui.Label name;
	@UiField com.google.gwt.user.client.ui.Label noHttpAlias;
	@UiField com.google.gwt.user.client.ui.Label noHttpsAlias;
	@UiField Label httpStatus;
	@UiField Label httpsStatus;
	@UiField FlowPanel httpPanel;
	@UiField FlowPanel httpsPanel;
	@UiField Anchor httpLink;
	@UiField Anchor httpsLink;
	@UiField Button showDescriptor;
	@UiField Modal descriptorModal;
	@UiField HTML descriptor;
	@UiField Anchor httpAliasAnchor;
	@UiField Anchor httpsAliasAnchor;
	@UiField Modal aliasModal;
	@UiField TextBox alias;
	@UiField Button updateAlias;

	public HttpMappingView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("showDescriptor")
	void showDescriptor(ClickEvent event) {
		getPresenter().onShowDescriptor();
	}
	
	@UiHandler("changeHttpAlias")
	void changeHttpAlias(ClickEvent event) {
		getPresenter().onInitChangeHttpAlias();
	}
	
	@UiHandler("changeHttpsAlias")
	void changeHttpsAlias(ClickEvent event) {
		getPresenter().onInitChangeHttpsAlias();
	}
	
	@UiHandler("updateAlias")
	void updateAlias(ClickEvent event) {
		getPresenter().onUpdateAlias();
	}
	
	@Override
	public void updateHttpStatus(String status) {
		httpStatus.setType(getStatusLabelType(status));
		httpStatus.setText(status);
	}

	@Override
	public void updateHttpsStatus(String status) {
		httpsStatus.setType(getStatusLabelType(status));
		httpsStatus.setText(status);
	}
	
	private LabelType getStatusLabelType(String status) {
		if("pending".equals(status)) {
			return LabelType.WARNING;
		} else if("ok".equals(status)) {
			return LabelType.SUCCESS;
		} else if("lost".equals(status)) {
			return LabelType.DANGER;
		} else {
			return LabelType.DEFAULT;
		}
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public void showHttpPanel(boolean show) {
		httpPanel.setVisible(show);
	}

	@Override
	public void showHttpsPanel(boolean show) {
		httpsPanel.setVisible(show);
	}

	@Override
	public void setHttpHref(String httpUrl) {
		httpLink.setHref(httpUrl);
	}

	@Override
	public void setHttpsHref(String httpsUrl) {
		httpsLink.setHref(httpsUrl);
	}

	@Override
	public void showDescriptorButton(boolean show) {
		showDescriptor.setVisible(show);
	}

	@Override
	public void showNoHttpAliasLabel(boolean show) {
		noHttpAlias.setVisible(show);
	}

	@Override
	public void showHttpAlias(boolean show) {
		httpAliasAnchor.setVisible(show);
	}

	@Override
	public void setHttpAlias(String httpAlias, String httpUrl) {
		httpAliasAnchor.setText(httpAlias);
		httpAliasAnchor.setHref(httpUrl);
	}

	@Override
	public void showNoHttpsAliasLabel(boolean show) {
		noHttpsAlias.setVisible(show);
	}

	@Override
	public void showHttpsAlias(boolean show) {
		httpsAliasAnchor.setVisible(show);
	}

	@Override
	public void setHttpsAlias(String httpsAlias, String httpsUrl) {
		httpsAliasAnchor.setText(httpsAlias);
		httpsAliasAnchor.setHref(httpsUrl);
	}

	@Override
	public void setPresenter(IHttpMappingPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IHttpMappingPresenter getPresenter() {
		return presenter;
	}

	@Override
	public HasHTML getDescriptor() {
		return descriptor;
	}

	@Override
	public void showDescriptorModal(boolean show) {
		descriptorModal.show();
	}

	@Override
	public void showChangeAliasModal(boolean show) {
		if(show) {
			aliasModal.show();
		} else {
			aliasModal.hide();
		}
	}

	@Override
	public HasText getAlias() {
		return alias;
	}

	@Override
	public void showAliasBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(updateAlias, busy);
	}
}