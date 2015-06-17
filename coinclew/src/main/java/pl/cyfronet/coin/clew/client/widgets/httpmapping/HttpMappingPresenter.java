package pl.cyfronet.coin.clew.client.widgets.httpmapping;

import javax.inject.Inject;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.UrlHelper;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.AliasCallback;
import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.AliasResponseHttpMapping;
import pl.cyfronet.coin.clew.client.widgets.httpmapping.IHttpMappingView.IHttpMappingPresenter;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = HttpMappingView.class, multiple = true)
public class HttpMappingPresenter extends BasePresenter<IHttpMappingView, MainEventBus> implements IHttpMappingPresenter {
	private String descriptor;
	private String httpMappingId;
	private String httpsMappingId;
	private String currentMappingId;
	private CloudFacadeController cloudFacadeController;
	private String invocationPath;
	
	@Inject
	public HttpMappingPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}
	
	public void onHttpMappingAliasChanged(String httpMappingId, AliasResponseHttpMapping httpMapping) {
		if(this.httpMappingId != null && this.httpMappingId.equals(httpMappingId)) {
			if(httpMapping.getCustomName() != null && httpMapping.getCustomUrl() != null) {
				view.showNoHttpAliasLabel(false);
				view.setHttpAlias(httpMapping.getCustomName(), UrlHelper.joinUrl(httpMapping.getCustomUrl(), this.invocationPath, null, null));
				view.showHttpAlias(true);
			} else {
				view.showHttpAlias(false);
				view.showNoHttpAliasLabel(true);
			}
		}
		
		if(this.httpsMappingId != null && this.httpsMappingId.equals(httpMappingId)) {
			if(httpMapping.getCustomName() != null && httpMapping.getCustomUrl() != null) {
				view.showNoHttpsAliasLabel(false);
				view.setHttpsAlias(httpMapping.getCustomName(), UrlHelper.joinUrl(httpMapping.getCustomUrl(), this.invocationPath, null, null));
				view.showHttpsAlias(true);
			} else {
				view.showHttpsAlias(false);
				view.showNoHttpsAliasLabel(true);
			}
		}
	}

	public void updateHttpsStatus(String httpsUrlStatus) {
		view.updateHttpsStatus(httpsUrlStatus);
	}

	public void updateHttpStatus(String httpUrlStatus) {
		view.updateHttpStatus(httpUrlStatus);
	}

	public void setHttpMapping(String name, String httpUrl, String httpsUrl, String httpUrlStatus, String httpsUrlStatus, String customHttpAlias,
			String customHttpUrl, String customHttpsAlias, String customHttpsUrl, String httpMappingId, String httpsMappingId, String invocationPath,
			String descriptor) {
		this.httpMappingId = httpMappingId;
		this.httpsMappingId = httpsMappingId;
		this.invocationPath = invocationPath;
		this.descriptor = descriptor;
		view.getName().setText(name);
		
		if(httpUrl != null) {
			view.showHttpPanel(true);
			view.setHttpHref(httpUrl);
			view.updateHttpStatus(httpUrlStatus);
			
			if(customHttpAlias != null && customHttpUrl != null) {
				view.setHttpAlias(customHttpAlias, customHttpUrl);
				view.showHttpAlias(true);
			} else {
				view.showNoHttpAliasLabel(true);
			}
		}
		
		if(httpsUrl != null) {
			view.showHttpsPanel(true);
			view.setHttpsHref(httpsUrl);
			view.updateHttpsStatus(httpsUrlStatus);
			
			if(customHttpsAlias != null && customHttpsUrl != null) {
				view.setHttpsAlias(customHttpsAlias, customHttpsUrl);
				view.showHttpsAlias(true);
			} else {
				view.showNoHttpsAliasLabel(true);
			}
		}
		
		if(descriptor != null) {
			view.showDescriptorButton(true);
		}
	}

	@Override
	public void onShowDescriptor() {
		view.getDescriptor().setHTML(SafeHtmlUtils.fromString(descriptor).asString());
		view.showDescriptorModal(true);
	}

	@Override
	public void onInitChangeHttpAlias() {
		currentMappingId = httpMappingId;
		view.showChangeAliasModal(true);
		view.getAlias().setText("");
	}

	@Override
	public void onInitChangeHttpsAlias() {
		currentMappingId = httpsMappingId;
		view.showChangeAliasModal(true);
		view.getAlias().setText("");
	}

	@Override
	public void onUpdateAlias() {
		String alias = view.getAlias().getText();
		view.showAliasBusyState(true);
		
		if(alias == null || alias.trim().isEmpty()) {
			alias = null;
		} else {
			alias = alias.trim();
		}
		
		cloudFacadeController.setAlias(currentMappingId, alias, new AliasCallback() {
			@Override
			public void onError(CloudFacadeError error) {
				view.showAliasBusyState(false);
			}
			
			@Override
			public void onAliasChanged(AliasResponseHttpMapping httpMapping) {
				view.showAliasBusyState(false);
				view.showChangeAliasModal(false);
				view.getAlias().setText("");
				eventBus.httpMappingAliasChanged(currentMappingId, httpMapping);
			}
		});
	}
}