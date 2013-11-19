package pl.cyfronet.coin.clew.client.widgets.initialconfigembed;

import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.widgets.initialconfigembed.IInitialConfigEmbedView.IInitialConfigEmbedPresenter;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InitialConfigEmbedView.class)
public class InitialConfigEmbedPresenter extends BasePresenter<IInitialConfigEmbedView, MainEventBus> implements IInitialConfigEmbedPresenter {
	private CloudFacadeController cloudFacadeController;

	@Inject
	public InitialConfigEmbedPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onStartApplications(final List<String> initialConfigurationIds) {
		cloudFacadeController.getInitialConfigurations(initialConfigurationIds, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations) {
				boolean parametersPresent = false;
				
				for (ApplianceConfiguration config : applianceConfigurations) {
					if (config.getParameters().size() > 0) {
						parametersPresent = true;
						
						break;
					}
				}
				
				if (parametersPresent) {
					view.showModal(true);
				} else {
					cloudFacadeController.startApplianceTypes(initialConfigurationIds, new Command() {
						@Override
						public void execute() {
							eventBus.refreshInstanceList();
						}
					});
				}
			}});
	}
}