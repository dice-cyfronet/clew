package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.appliancetype.IApplianceTypeView.IApplianceTypePresenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplianceTypeView.class, multiple = true)
public class ApplianceTypePresenter extends BasePresenter<IApplianceTypeView, MainEventBus> implements IApplianceTypePresenter {
	private static final Logger log = LoggerFactory.getLogger(ApplianceTypePresenter.class);
	
	private String applianceTypeId;
	private CloudFacadeController cloudFacadeController;
	private Map<String, HasValue<Boolean>> initialConfigs;
	
	@Inject
	public ApplianceTypePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		initialConfigs = new HashMap<String, HasValue<Boolean>>();
	}

	public void setApplianceType(ApplianceType applianceType) {
		applianceTypeId = applianceType.getId();
		view.getName().setText(applianceType.getName());
		
		if (applianceType.getDescription().trim().isEmpty()) {
			view.setEmptyDescription();
		} else {
			view.getDescription().setText(applianceType.getDescription());
		}
		
		view.clearInitialConfigsContainer();
		view.addInitialConfigsProgressIndicator();
		cloudFacadeController.getInitialConfigurations(applianceTypeId, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations) {
				view.clearInitialConfigsContainer();
				
				if (applianceConfigurations.size() == 0) {
					view.addNoInitialConfigsLabel();
				} else {
					boolean firstChecked = false;
					
					for (ApplianceConfiguration config : applianceConfigurations) {
						initialConfigs.put(config.getId(), view.addInitialConfigRadioBox(applianceTypeId, config.getName()));
						
						if (!firstChecked) {
							initialConfigs.get(config.getId()).setValue(true);
							firstChecked = true;
						}
					}
					
					view.enableControls(true);
				}
			}
		});
	}

	@Override
	public void onStartApplianceType() {
		String initialConfigurationId = null;
		
		for (String initialConfigId : initialConfigs.keySet()) {
			if (initialConfigs.get(initialConfigId).getValue()) {
				initialConfigurationId = initialConfigId;
				
				break;
			}
		}
		
		log.info("Starting appliance type with id {} and initial configuration id {}", applianceTypeId, initialConfigurationId);
		view.setStartButtonBusyState(true);
		cloudFacadeController.startApplianceTypes(Arrays.asList(new String[] {initialConfigurationId}), new Command() {
			@Override
			public void execute() {
				view.setStartButtonBusyState(false);
				eventBus.hideStartInstanceModal();
				eventBus.refreshInstanceList();
			}
		});
	}

	public String getSelectedInitialConfigId() {
		if (view.getChecked().getValue()) {
			for (String initialConfigId : initialConfigs.keySet()) {
				if (initialConfigs.get(initialConfigId).getValue()) {
					return initialConfigId;
				}
			}
		}
		
		return null;
	}
}