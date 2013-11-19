package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationCallback;
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
	private Map<String, String> configNames;
	private Map<String, InitialConfigPresenter> configPresenters;
	private String editedConfigId;

	@Inject
	public InitialConfigsEditorPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		configNames = new HashMap<String, String>();
		configPresenters = new HashMap<String, InitialConfigPresenter>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowInitialConfigsEditor(String applianceTypeId) {
		this.applianceTypeId = applianceTypeId;
		view.showModal(true);
		loadInitialConfigs();
	}
	
	public void onRemoveInitialConfiguration(String initialConfigurationId) {
		InitialConfigPresenter presenter = configPresenters.get(initialConfigurationId);
		
		if (presenter != null) {
			eventBus.removeHandler(presenter);
			configPresenters.remove(initialConfigurationId);
			view.getContainer().remove(presenter.getView().asWidget());
			configNames.remove(initialConfigurationId);
			
			if (configPresenters.size() == 0) {
				view.showNoConfigsLabel();
			}
		}
	}
	
	public void onEditInitialConfiguration(String initialConfigurationId) {
		InitialConfigPresenter presenter = configPresenters.get(initialConfigurationId);
		
		if (presenter != null) {
			view.getName().setText(presenter.getConfigName());
			view.getPayload().setText(presenter.getConfigPayload());
			view.setEditLabel(true);
			editedConfigId = initialConfigurationId;
		}
	}

	@Override
	public void onProcess() {
		String configName = view.getName().getText().trim();
		String configPayload = view.getPayload().getText().trim();
		view.clearMessages();
		
		if (!configName.isEmpty() && !configPayload.isEmpty()) {
			if (editedConfigId != null) {
				cloudFacadeController.updateApplianceConfiguration(editedConfigId, applianceTypeId, configName, configPayload, new ApplianceConfigurationCallback() {
					@Override
					public void processApplianceConfiguration(ApplianceConfiguration applianceConfiguration) {
						InitialConfigPresenter presenter = configPresenters.get(applianceConfiguration.getId());
						
						if (presenter != null) {
							configNames.remove(presenter.getConfigName());
							configNames.put(applianceConfiguration.getId(), applianceConfiguration.getName());
							presenter.setInitialConfig(applianceConfiguration);
						}
						
						resetForm();
					}
				});
			} else {
				if (configNames.values().contains(configName)) {
					view.displayNameNotUniqueMessage();
				} else {
					cloudFacadeController.addApplianceConfiguration(applianceTypeId, configName, configPayload, new ApplianceConfigurationCallback() {
						@Override
						public void processApplianceConfiguration(ApplianceConfiguration applianceConfiguration) {
							view.getName().setText("");
							view.getPayload().setText("");
							
							if (configPresenters.size() == 0) {
								view.getContainer().clear();
							}
							
							InitialConfigPresenter presenter = eventBus.addHandler(InitialConfigPresenter.class);
							view.getContainer().add(presenter.getView().asWidget());
							presenter.setInitialConfig(applianceConfiguration);
							configNames.put(applianceConfiguration.getId(), applianceConfiguration.getName());
							configPresenters.put(applianceConfiguration.getId(), presenter);
						}
					});
				}
			}
		} else {
			view.displayNameOrPayloadEmptyMessage();
		}
	}
	
	private void loadInitialConfigs() {
		view.getContainer().clear();
		view.showProgressIndicator();
		resetForm();
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
						configNames.put(configuration.getId(), configuration.getName());
						configPresenters.put(configuration.getId(), presenter);
					}
				}
			}
		});
	}

	private void resetForm() {
		view.clearMessages();
		view.getName().setText("");
		view.getPayload().setText("");
		view.setEditLabel(false);
		editedConfigId = null;
	}
}