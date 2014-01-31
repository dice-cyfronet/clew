package pl.cyfronet.coin.clew.client.widgets.initialconfigpicker;

import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.widgets.initialconfigpicker.IInitialConfigurationPickerView.IInitialConfigurationPickerPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InitialConfigurationPickerView.class)
public class InitialConfigurationPickerPresenter extends BasePresenter<IInitialConfigurationPickerView, MainEventBus> implements IInitialConfigurationPickerPresenter {
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowInitialConfigPicker(List<ApplianceConfiguration> applianceConfigurations) {
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
		eventBus.startApplications(Arrays.asList(new String[] {configId}), true);
	}
}