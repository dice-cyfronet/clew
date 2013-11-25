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

import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplianceTypeView.class, multiple = true)
public class ApplianceTypePresenter extends BasePresenter<IApplianceTypeView, MainEventBus> implements IApplianceTypePresenter {
	private static final Logger log = LoggerFactory.getLogger(ApplianceTypePresenter.class);
	
	private ApplianceType applianceType;
	private CloudFacadeController cloudFacadeController;
	private Map<String, HasValue<Boolean>> initialConfigs;
	private boolean developmentMode;
	
	@Inject
	public ApplianceTypePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		initialConfigs = new HashMap<String, HasValue<Boolean>>();
	}

	public void setApplianceType(ApplianceType applianceType, boolean developmentMode) {
		this.applianceType = applianceType;
		this.developmentMode = developmentMode;
		view.getName().setText(applianceType.getName());
		
		if (applianceType.getDescription().trim().isEmpty()) {
			view.setEmptyDescription();
		} else {
			view.getDescription().setText(applianceType.getDescription());
		}
		
		view.clearInitialConfigsContainer();
		view.addInitialConfigsProgressIndicator();
		cloudFacadeController.getInitialConfigurations(applianceType.getId(), new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations) {
				view.clearInitialConfigsContainer();
				
				if (applianceConfigurations.size() == 0) {
					view.addNoInitialConfigsLabel();
				} else {
					boolean firstChecked = false;
					
					for (ApplianceConfiguration config : applianceConfigurations) {
						initialConfigs.put(config.getId(), view.addInitialConfigRadioBox(ApplianceTypePresenter.this.applianceType.getId(), config.getName()));
						
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
		
		eventBus.hideStartInstanceModal();
		eventBus.startApplications(Arrays.asList(new String[] {initialConfigurationId}), developmentMode);
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

	public boolean matchesFilter(String filterText) {
		return applianceType.getName() != null && applianceType.getName().contains(filterText) ||
				applianceType.getDescription() != null && applianceType.getDescription().contains(filterText);
	}
}