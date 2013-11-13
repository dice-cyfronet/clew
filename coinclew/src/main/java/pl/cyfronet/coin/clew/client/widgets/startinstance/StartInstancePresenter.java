package pl.cyfronet.coin.clew.client.widgets.startinstance;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.appliancetype.ApplianceTypePresenter;
import pl.cyfronet.coin.clew.client.widgets.startinstance.IStartInstanceView.IStartInstancePresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = StartInstanceView.class)
public class StartInstancePresenter extends BasePresenter<IStartInstanceView, MainEventBus> implements IStartInstancePresenter {
	private CloudFacadeController cloudFacadeController;
	private List<ApplianceTypePresenter> applianceTypePresenters;

	@Inject
	public StartInstancePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		applianceTypePresenters = new ArrayList<ApplianceTypePresenter>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowStartInstanceDialog() {
		view.clearApplianceTypeContainer();
		view.showProgressIndicator();
		view.show();
		cloudFacadeController.getApplianceTypes(new ApplianceTypesCallback() {
			@Override
			public void processApplianceTypes(List<ApplianceType> applianceTypes) {
				view.clearApplianceTypeContainer();
				
				if (applianceTypes.size() == 0) {
					view.addNoApplianceTypesLabel();
				} else {
					for (ApplianceType applianceType : applianceTypes) {
						ApplianceTypePresenter presenter = eventBus.addHandler(ApplianceTypePresenter.class);
						applianceTypePresenters.add(presenter);
						presenter.setApplianceType(applianceType);
						view.getApplianceTypeContainer().add(presenter.getView().asWidget());
					}
				}
			}
		});
	}
	
	public void onHideStartInstanceModal() {
		view.hide();
		
		for (ApplianceTypePresenter presenter : applianceTypePresenters) {
			eventBus.removeHandler(presenter);
		}
	}
}