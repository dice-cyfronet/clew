package pl.cyfronet.coin.clew.client.widgets.dev;

import java.util.List;

import pl.cyfronet.coin.clew.client.common.BasePresenter;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.NewApplianceType;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class DevPresenter extends BasePresenter implements Presenter {
	public interface View extends IsWidget {
		void showNewApplianceModal(boolean show);
		void addApplianceType(String applianceTypeId, String name, String description);
		HasText getNewApplianceTypeName();
		HasText getNewApplianceTypeDescription();
		void addInitialConfiguration(String id, String name, String payload);
		void showInitialConfigurationModel(boolean b);
		void showNoInitialconfigurationsLabel(boolean b);
		HasText getInitialConfigurationName();
		HasText getInitialConfigurationPayload();
		void displayInitialConfigurationNameOrPayloadEmptyMessage();
	}
	
	private CloudFacadeController cloudFacadeController;
	private View view;
	private String currentApplianceTypeId;

	@Inject
	public DevPresenter(View view, CloudFacadeController cloudFacadeController) {
		this.view = view;
		this.cloudFacadeController = cloudFacadeController;
		
	}
	
	public void load() {
		cloudFacadeController.getApplianceTypes(new ApplianceTypesCallback() {
			@Override
			public void processApplianceTypes(List<ApplianceType> applianceTypes) {
				for (ApplianceType applianceType : applianceTypes) {
					view.addApplianceType(applianceType.getId(), applianceType.getName(), applianceType.getDescription());
				}
			}
		});
	}

	@Override
	public IsWidget getWidget() {
		return view;
	}

	@Override
	public void onShowNewApplianceModal() {
		view.showNewApplianceModal(true);
	}

	@Override
	public void onAddApplianceType() {
		NewApplianceType newApplianceType = new NewApplianceType();
		newApplianceType.setName(view.getNewApplianceTypeName().getText());
		newApplianceType.setDescription(view.getNewApplianceTypeDescription().getText());
//		cloudFacadeController.addApplianceType(newApplianceType, new Command() {
//			@Override
//			public void execute() {
//				view.showNewApplianceModal(false);
//			}});
	}

	@Override
	public void onInitialConfigurationModal(String applianceTypeId) {
		currentApplianceTypeId = applianceTypeId;
		cloudFacadeController.getInitialConfigurations(applianceTypeId, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations) {
				if (applianceConfigurations.size() > 0) {
					for (ApplianceConfiguration applianceConfiguration : applianceConfigurations) {
						view.addInitialConfiguration(applianceConfiguration.getId(), applianceConfiguration.getName(), applianceConfiguration.getPayload());
					}
					
					view.showNoInitialconfigurationsLabel(false);
				} else {
					view.showNoInitialconfigurationsLabel(true);
				}
				
				view.showInitialConfigurationModel(true);
			}
		});
	}

	@Override
	public void onAddInitialConfiguration() {
		String name = view.getInitialConfigurationName().getText().trim();
		String payload = view.getInitialConfigurationPayload().getText().trim();
		
		if (name.isEmpty() || payload.isEmpty()) {
			view.displayInitialConfigurationNameOrPayloadEmptyMessage();
		} else {
			cloudFacadeController.addApplianceConfiguration(currentApplianceTypeId, name, payload, new ApplianceConfigurationCallback() {
				@Override
				public void processApplianceConfiguration(ApplianceConfiguration applianceConfiguration) {
					view.addInitialConfiguration(applianceConfiguration.getId(), applianceConfiguration.getName(), applianceConfiguration.getPayload());
					view.getInitialConfigurationName().setText("");
					view.getInitialConfigurationPayload().setText("");
					view.showNoInitialconfigurationsLabel(false);
				}
			});
		}
	}
}