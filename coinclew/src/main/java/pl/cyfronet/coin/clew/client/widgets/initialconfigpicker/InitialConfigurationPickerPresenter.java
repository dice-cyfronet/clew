package pl.cyfronet.coin.clew.client.widgets.initialconfigpicker;

import static java.util.Arrays.asList;

import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.widgets.initialconfigpicker.IInitialConfigurationPickerView.IInitialConfigurationPickerPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InitialConfigurationPickerView.class)
public class InitialConfigurationPickerPresenter extends BasePresenter<IInitialConfigurationPickerView, MainEventBus> implements IInitialConfigurationPickerPresenter {
	private boolean developmentMode;

	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowInitialConfigPicker(List<ApplianceConfiguration> applianceConfigurations, boolean deelopmentMode) {
		this.developmentMode = deelopmentMode;
		view.clearConfigs();
		
		for (ApplianceConfiguration config : applianceConfigurations) {
			view.addConfig(config.getId(), config.getName());
		}
		
		view.showModal(true);
	}

	@Override
	public void onStartInstance() {
		String configId = view.getConfig().getValue();
		view.showModal(false);
		eventBus.startApplications(asList(configId), null, developmentMode, null);
	}
}