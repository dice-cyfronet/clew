package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.RemoveApplianceTypeCallback;
import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.overlay.OwnedApplianceType;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.IAtomicServiceView.IAtomicServicePresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = AtomicServiceView.class, multiple = true)
public class AtomicServicePresenter extends BasePresenter<IAtomicServiceView, MainEventBus> implements IAtomicServicePresenter {
	private OwnedApplianceType applianceType;
	private MiTicketReader ticketReader;
	private CloudFacadeController cloudFacadeController;
	
	@Inject
	public AtomicServicePresenter(CloudFacadeController cloudFacadeController, MiTicketReader ticketReader) {
		this.cloudFacadeController = cloudFacadeController;
		this.ticketReader = ticketReader;
	}

	@Override
	public void onEditInitialConfigs() {
		eventBus.showInitialConfigsEditor(applianceType.getApplianceType().getId());
	}

	public void setApplianceType(OwnedApplianceType applianceType) {
		this.applianceType = applianceType;
		view.getName().setText(applianceType.getApplianceType().getName());
		view.showInactiveLabel(!applianceType.getApplianceType().isActive());
		
		if(applianceType.getApplianceType().isActive()) {
			view.enableStartButton(true);
		} else {
			view.enableStartButton(false);
		}
		
		String description = applianceType.getApplianceType().getDescription();
		view.getDescription().setHTML(description != null && !description.isEmpty() ? description : "&nbsp;");
		
		if (applianceType.getUser().getLogin().equals(ticketReader.getUserLogin())) {
			view.addRemoveButton();
		}
	}

	@Override
	public void onEditProperties() {
		eventBus.showAtomicServiceEditor(applianceType.getApplianceType().getId(), false);
	}

	@Override
	public void onRemove() {
		if(view.confirmRemoval()) {
			view.setRemoveBusyState(true);
			cloudFacadeController.removeApplianceType(applianceType.getApplianceType().getId(), new RemoveApplianceTypeCallback() {
				@Override
				public void onError(CloudFacadeError error) {
					view.setRemoveBusyState(false);
				}

				@Override
				public void onApplianceTypeRemoved() {
					view.setRemoveBusyState(false);
					eventBus.removeApplianceType(applianceType.getApplianceType().getId());
				}});
		}
	}

	public boolean isInactive() {
		return !applianceType.getApplianceType().isActive();
	}

	@Override
	public void onEditExternalInterfaces() {
		eventBus.showExternalInterfacesEditorForApplianceType(applianceType.getApplianceType().getId());
	}

	@Override
	public void onStartInstance() {
		view.setStartInstanceBusyState(true);
		cloudFacadeController.getInitialConfigurations(applianceType.getApplianceType().getId(), new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations) {
				view.setStartInstanceBusyState(false);
				
				if (applianceConfigurations.size() == 0) {
					view.showNoInitialConfigurationsMessage();
				} else if (applianceConfigurations.size() == 1) {
					eventBus.startApplications(
							Arrays.asList(new String[] {applianceConfigurations.get(0).getId()}), null, true);
				} else {
					eventBus.showInitialConfigPicker(applianceConfigurations, true);
				}
			}
		});
	}

	public OwnedApplianceType getApplianceType() {
		return applianceType;
	}
}