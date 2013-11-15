package pl.cyfronet.coin.clew.client.widgets.initialconfig;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.widgets.initialconfig.IInitialConfigView.IInitialConfigPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InitialConfigView.class, multiple = true)
public class InitialConfigPresenter extends BasePresenter<IInitialConfigView, MainEventBus> implements IInitialConfigPresenter {
	public void setInitialConfig(ApplianceConfiguration configuration) {
		view.getName().setText(configuration.getName());
	}
}