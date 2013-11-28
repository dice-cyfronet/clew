package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.IAtomicServiceView.IAtomicServicePresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = AtomicServiceView.class, multiple = true)
public class AtomicServicePresenter extends BasePresenter<IAtomicServiceView, MainEventBus> implements IAtomicServicePresenter {
	private ApplianceType applianceType;

	@Override
	public void onEditInitialConfigs() {
		eventBus.showInitialConfigsEditor(applianceType.getId());
	}

	public void setApplianceType(ApplianceType applianceType) {
		this.applianceType = applianceType;
		view.getName().setText(applianceType.getName());
	}

	@Override
	public void onEditProperties() {
		eventBus.showAtomicServiceEditor(applianceType.getId(), false);
	}
}