package pl.cyfronet.coin.clew.client.widgets.applianceset;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.ApplianceSet;
import pl.cyfronet.coin.clew.client.widgets.applianceset.IApplianceSetView.IApplianceSetPresenter;

import com.google.inject.Inject;
import com.mvp4g.client.presenter.BasePresenter;

public class ApplianceSetPresenter extends BasePresenter<IApplianceSetView, MainEventBus> implements IApplianceSetPresenter {
	private CloudFacadeController cloudFacadeController;
	private String appliancesetId;

	@Inject
	public ApplianceSetPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}

	public void setApplianceSet(ApplianceSet applianceSet) {
		appliancesetId = applianceSet.getId();
		view.getName().setText(applianceSet.getName());
	}
}