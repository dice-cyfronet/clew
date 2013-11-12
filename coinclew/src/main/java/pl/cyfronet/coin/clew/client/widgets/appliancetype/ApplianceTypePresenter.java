package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.appliancetype.IApplianceTypeView.IApplianceTypePresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplianceTypeView.class, multiple = true)
public class ApplianceTypePresenter extends BasePresenter<IApplianceTypeView, MainEventBus> implements IApplianceTypePresenter {
	private String applianceTypeId;

	public void setApplianceType(ApplianceType applianceType) {
		applianceTypeId = applianceType.getId();
		view.getName().setText(applianceType.getName());
		
		if (applianceType.getDescription().trim().isEmpty()) {
			view.setEmptyDescription();
		} else {
			view.getDescription().setText(applianceType.getDescription());
		}
	}

	@Override
	public void onStartApplianceType() {
		view.setStartButtonBusyState(true);
	}
}