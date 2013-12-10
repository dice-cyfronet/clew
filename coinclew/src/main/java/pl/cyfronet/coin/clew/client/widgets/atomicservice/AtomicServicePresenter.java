package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.overlay.OwnedApplianceType;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.IAtomicServiceView.IAtomicServicePresenter;

import com.google.gwt.user.client.Command;
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
		view.updateAuthor(applianceType.getUser().getFullName());
		view.showInactiveLabel(!applianceType.getApplianceType().isActive());
		
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
		if (view.confirmRemoval()) {
			view.setRemoveBusyState(true);
			cloudFacadeController.removeApplianceType(applianceType.getApplianceType().getId(), new Command() {
				@Override
				public void execute() {
					view.setRemoveBusyState(false);
					eventBus.removeApplianceType(applianceType.getApplianceType().getId());
				}
			});
		}
	}

	public boolean isInactive() {
		return !applianceType.getApplianceType().isActive();
	}
}