package pl.cyfronet.coin.clew.client.widgets.development;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.widgets.development.IDevelopmentView.IDevelopmentPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = DevelopmentView.class)
public class DevelopmentPresenter extends BasePresenter<IDevelopmentView, MainEventBus> implements IDevelopmentPresenter {
	public void onSwitchToDevelopmentView() {
		eventBus.setBody(view);
		loadDevelopmentResources();
	}

	private void loadDevelopmentResources() {
		view.addNoAtomicServicesLabel();
		view.addNoRunningInstancesLabel();
	}
}