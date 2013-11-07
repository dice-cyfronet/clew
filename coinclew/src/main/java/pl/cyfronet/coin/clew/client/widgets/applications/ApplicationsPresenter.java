package pl.cyfronet.coin.clew.client.widgets.applications;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.widgets.applications.IApplicationsView.IApplicationsPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplicationsView.class)
public class ApplicationsPresenter extends BasePresenter<IApplicationsView, MainEventBus> implements IApplicationsPresenter {
	public void onStart() {
		eventBus.setBody(view);
	}
	
	public void onSwitchToApplicationsView() {
		eventBus.setBody(view);
	}
}