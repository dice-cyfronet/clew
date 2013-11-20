package pl.cyfronet.coin.clew.client.widgets.initialconfig;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.widgets.initialconfig.IInitialConfigView.IInitialConfigPresenter;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InitialConfigView.class, multiple = true)
public class InitialConfigPresenter extends BasePresenter<IInitialConfigView, MainEventBus> implements IInitialConfigPresenter {
	private CloudFacadeController cloudFacadeController;
	private ApplianceConfiguration configuration;
	
	@Inject
	public InitialConfigPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}

	public void setInitialConfig(ApplianceConfiguration configuration) {
		this.configuration = configuration;
		view.getName().setText(configuration.getName());
		view.clearParameterContainer();
		
		if (configuration.getParameters().size() > 0) {
			for (String parameter : configuration.getParameters()) {
				view.addParameter(parameter);
			}
		} else {
			view.addNoParametersLabel();
		}
	}

	@Override
	public void onRemove() {
		cloudFacadeController.removeInitialConfiguration(configuration.getId(), new Command() {
			@Override
			public void execute() {
				eventBus.removeInitialConfiguration(configuration.getId());
			}
		});
	}

	@Override
	public void onEdit() {
		eventBus.editInitialConfiguration(configuration.getId());
	}

	public String getConfigPayload() {
		return configuration.getPayload();
	}

	public String getConfigName() {
		return configuration.getName();
	}
}