package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.overlay.OwnedApplianceType;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.IAtomicServiceView.IAtomicServicePresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = AtomicServiceView.class, multiple = true)
public class AtomicServicePresenter extends BasePresenter<IAtomicServiceView, MainEventBus> implements IAtomicServicePresenter {
	private OwnedApplianceType applianceType;

	@Override
	public void onEditInitialConfigs() {
		eventBus.showInitialConfigsEditor(applianceType.getApplianceType().getId());
	}

	public void setApplianceType(OwnedApplianceType applianceType) {
		this.applianceType = applianceType;
		view.getName().setText(applianceType.getApplianceType().getName());
		view.updateAuthor(applianceType.getUser().getFullName());
	}

	@Override
	public void onEditProperties() {
		eventBus.showAtomicServiceEditor(applianceType.getApplianceType().getId(), false);
	}
}