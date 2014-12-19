package pl.cyfronet.coin.clew.client.widgets.root;

import pl.cyfronet.coin.clew.client.CloudFacadeOverrideProperties;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.widgets.root.IRootView.IRootPresenter;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = RootView.class)
public class RootPresenter extends BasePresenter<IRootView, MainEventBus> implements IRootPresenter {
	private Timer errorTimer;
	private CloudFacadeOverrideProperties properties;

	@Inject
	public RootPresenter(CloudFacadeOverrideProperties properties) {
		this.properties = properties;
	}
	
	public void onStart() {
		view.setBuildInfo(properties.getBuildInfo());
	}
	
	public void onSetMenu(IsWidget widget) {
		view.setMenu(widget);
	}
	
	public void onSetBody(IsWidget widget) {
		view.setBody(widget);
	}
	
	public void onAddPopup(IsWidget widget) {
		view.addPopup(widget);
	}
	
	public void onDisplayError(CloudFacadeError error) {
		view.getErrorLabel().setText(error.getMessage());
		view.setErrorLabelVisible(true);
		
		if (errorTimer != null) {
			errorTimer.cancel();
		}
		
		errorTimer = new Timer() {
			@Override
			public void run() {
				view.setErrorLabelVisible(false);
				view.getErrorLabel().setText("");
				errorTimer = null;
			}
		};
		errorTimer.schedule(5000);
	}
	
	public void onShowStartApplicationProgress(boolean show) {
		view.showStartApplicationLabel(show);
	}
}
