package pl.cyfronet.coin.clew.client.widgets.appliancetypeeditor;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypeCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ErrorCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeErrorCodes;
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
		view.clearErrorMessages(); 
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
		String name = view.getName().getText().trim();
		String description = view.getDescription().getText().trim();
		boolean shared = view.getShared().getValue();
		boolean scalable = view.getScalable().getValue();
		String visibleFor = view.getVisibleFor().getValue();
		String cores = view.getCores().getValue();
		String ram = view.getRam().getValue();
		String disk = view.getDisk().getValue();
		view.clearErrorMessages();
		
		if (name.isEmpty()) {
			view.displayNameEmptyMessage();
		} else {
			view.setUpdateBusyState(true);
			cloudFacadeController.updateApplianceType(applianceTypeId, name, description, shared, scalable,
					visibleFor, cores, ram, disk, new ApplianceTypeCallback() {
						@Override
						public void processApplianceType(ApplianceType applianceType) {
							view.setUpdateBusyState(false);
							view.showModal(false);
							eventBus.updateApplianceTypeView(applianceType);
						}
					}, new ErrorCallback() {
						@Override
						public void onError(CloudFacadeErrorCodes errorCodes) {
							view.setUpdateBusyState(false);
							view.displayGeneralError();
						}});
		}
	}
}