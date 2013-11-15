package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.widgets.initialconfig.InitialConfigPresenter;
import pl.cyfronet.coin.clew.client.widgets.initialconfigseditor.IInitialConfigsEditorView.IInitialConfigsEditorPresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InitialConfigsEditorView.class)
public class InitialConfigsEditorPresenter extends BasePresenter<IInitialConfigsEditorView, MainEventBus> implements IInitialConfigsEditorPresenter {
	private CloudFacadeController cloudFacadeController;
	private String applianceTypeId;

	@Inject
	public InitialConfigsEditorPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowInitialConfigsEditor(String applianceTypeId) {
		this.applianceTypeId = applianceTypeId;
		view.showModal(true);
		loadInitialConfigs();
	}

	private void loadInitialConfigs() {
		view.getContainer().clear();
		view.showProgressIndicator();
		cloudFacadeController.getInitialConfigurations(applianceTypeId, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations) {
				view.getContainer().clear();
				
				if (applianceConfigurations.size() == 0) {
					view.showNoConfigsLabel();
				} else {
					for (ApplianceConfiguration configuration : applianceConfigurations) {
						InitialConfigPresenter presenter = eventBus.addHandler(InitialConfigPresenter.class);
						view.getContainer().add(presenter.getView().asWidget());
						presenter.setInitialConfig(configuration);
					}
				}
			}
		});
	}
}