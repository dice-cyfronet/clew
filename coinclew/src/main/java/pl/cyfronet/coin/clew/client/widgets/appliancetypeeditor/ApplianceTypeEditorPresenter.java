package pl.cyfronet.coin.clew.client.widgets.appliancetypeeditor;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypeCallback;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.appliancetypeeditor.IApplianceTypeEditorView.IApplianceTypeEditorPresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplianceTypeEditorView.class)
public class ApplianceTypeEditorPresenter extends BasePresenter<IApplianceTypeEditorView, MainEventBus> implements IApplianceTypeEditorPresenter {
	private CloudFacadeController cloudFacadeController;
	private String applianceTypeId;

	@Inject
	public ApplianceTypeEditorPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowAtomicServiceEditor(String applianceTypeId) {
		this.applianceTypeId = applianceTypeId;
		view.showModal(true);
		loadProperties();
	}

	private void loadProperties() {
		cloudFacadeController.getApplianceType(applianceTypeId, new ApplianceTypeCallback() {
			@Override
			public void processApplianceType(ApplianceType applianceType) {
				view.getName().setText(applianceType.getName());
				view.getDescription().setText(applianceType.getDescription());
				view.getShared().setValue(applianceType.isShared());
				view.getScalable().setValue(applianceType.isScalable());
				view.getVisibleFor().setValue(applianceType.getVisibleFor());
				view.getCores().setValue(applianceType.getPreferenceCpu());
				view.getRam().setValue(applianceType.getPreferenceMemory());
				view.getDisk().setValue(applianceType.getPreferenceDisk());
			}
		});
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		
	}
}