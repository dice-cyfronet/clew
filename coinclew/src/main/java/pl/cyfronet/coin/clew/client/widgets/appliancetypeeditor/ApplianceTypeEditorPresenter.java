package pl.cyfronet.coin.clew.client.widgets.appliancetypeeditor;

import pl.cyfronet.coin.clew.client.ClewProperties;
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
	private String applianceId;
	private boolean saveMode;
	private ClewProperties properties;

	@Inject
	public ApplianceTypeEditorPresenter(CloudFacadeController cloudFacadeController, ClewProperties properties) {
		this.cloudFacadeController = cloudFacadeController;
		this.properties = properties;
	}
	
	public void onStart() {
		setOptions();
		eventBus.addPopup(view);
	}

	public void onShowAtomicServiceEditor(String applianceTypeOrInstanceId, boolean saveMode) {
		if (saveMode) {
			applianceId = applianceTypeOrInstanceId;
		} else {
			applianceTypeId = applianceTypeOrInstanceId;
		}
		
		this.saveMode = saveMode;
		view.showModal(true);
		applyMode();
		
		if (saveMode) {
			clearControls();
		} else {
			loadProperties();
		}
	}
	
	private void setOptions() {
		for(String value : properties.coreOptions()) {
			view.addCoreOption(value, value.equals("0") ? view.getDefaultOptionLabel() : value);
		}
		
		for(String value : properties.ramOptions()) {
			view.addRamOption(value, value.equals("0") ? view.getDefaultOptionLabel() : value);
		}
		
		for(String value : properties.diskOptions()) {
			view.addDiskOption(value, value.equals("0") ? view.getDefaultOptionLabel() : value);
		}
	}

	private void clearControls() {
		view.getName().setText("");
		view.getDescription().setText("");
		view.getShared().setValue(false);
		view.getScalable().setValue(false);
		view.getVisibleFor().setValue("all");
		view.getCores().setValue("0");
		view.getRam().setValue("0");
		view.getDisk().setValue("0");
	}

	private void applyMode() {
		if (saveMode) {
			view.showSaveControl(true);
			view.showUpdateControl(false);
		} else {
			view.showSaveControl(false);
			view.showUpdateControl(true);
		}
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
				view.getVisibleFor().setValue(applianceType.getVisibleTo());
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

	@Override
	public void onSave() {
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
			view.setSaveBusyState(true);
			cloudFacadeController.saveApplianceType(applianceId, name, description, shared, scalable,
					visibleFor, cores, ram, disk, new ApplianceTypeCallback() {
						@Override
						public void processApplianceType(ApplianceType applianceType) {
							view.setSaveBusyState(false);
							view.showModal(false);
							eventBus.updateApplianceTypeView(applianceType);
						}
					}, new ErrorCallback() {
						@Override
						public void onError(CloudFacadeErrorCodes errorCodes) {
							view.setSaveBusyState(false);
							view.displayGeneralSaveError();
						}});
		}
	}
}