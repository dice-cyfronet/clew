package pl.cyfronet.coin.clew.client.widgets.applications;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.AggregateApplianceCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceConfigurationsCallback;
import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.AggregateAppliance;
import pl.cyfronet.coin.clew.client.controller.cf.applianceconf.ApplianceConfiguration;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet.Type;
import pl.cyfronet.coin.clew.client.widgets.applications.IApplicationsView.IApplicationsPresenter;
import pl.cyfronet.coin.clew.client.widgets.instance.InstancePresenter;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ApplicationsView.class)
public class ApplicationsPresenter extends BasePresenter<IApplicationsView, MainEventBus> implements IApplicationsPresenter {
	private final static Logger log = LoggerFactory.getLogger(ApplicationsPresenter.class);
	
	private static final int REFRESH_MILIS = 5000;
	
	private CloudFacadeController cloudFacadeController;
	private Map<String, InstancePresenter> instancePresenters;
	private Timer timer;

	@Inject
	public ApplicationsPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		instancePresenters = new HashMap<String, InstancePresenter>();
	}
	
	@Override
	public void onStartInstance() {
		eventBus.showStartInstanceDialog(false);
	}

	public void onSwitchToApplicationsView() {
		eventBus.setBody(view);
		loadApplianceInstances(false);
	}
	
	public void onRemoveInstance(String applianceInstanceId) {
		InstancePresenter instancePresenter = instancePresenters.get(applianceInstanceId);
		
		if (instancePresenter != null) {
			eventBus.removeHandler(instancePresenter);
			view.getInstanceContainer().remove(instancePresenter.getView().asWidget());
			instancePresenters.remove(applianceInstanceId);
			
			if (instancePresenters.size() == 0) {
				view.showHeaderRow(false);
				view.showNoInstancesLabel(true);
				onDeactivateApplicationsRefresh();
			}
		}
	}
	
	public void onRefreshInstanceList() {
		loadApplianceInstances(false);
	}
	
	public void onDeactivateApplicationsRefresh() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	public void onStartInstance(String applianceTypeId) {
		eventBus.switchToApplicationsView();
		cloudFacadeController.getInitialConfigurations(applianceTypeId, new ApplianceConfigurationsCallback() {
			@Override
			public void processApplianceConfigurations(List<ApplianceConfiguration> applianceConfigurations) {
				if(applianceConfigurations.size() == 0) {
					view.showNoInitialConfigurationsMessage();
				} else if(applianceConfigurations.size() == 1) {
					eventBus.startApplications(
							Arrays.asList(new String[] {applianceConfigurations.get(0).getId()}), null, false);
				} else {
					eventBus.showInitialConfigPicker(applianceConfigurations, false);
				}
			}
		});
	}
	
	private void loadApplianceInstances(final boolean update) {
		view.showNoInstancesLabel(false);
		
		if(!update) {
			view.showLoadingIndicator(true);
		}
		
		cloudFacadeController.aggregatedInstances(Type.portal, new AggregateApplianceCallback() {
			@Override
			public void processAppliances(List<AggregateAppliance> appliances) {
				if(!update) {
					view.showLoadingIndicator(false);
				}
				
				if(appliances.size() == 0) {
					view.showNoInstancesLabel(true);
				} else {
					view.showHeaderRow(true);
					view.showNoInstancesLabel(false);
					
					for(AggregateAppliance applianceInstance : appliances) {
						InstancePresenter presenter = instancePresenters.get(applianceInstance.getId());

						if(presenter == null) {
							presenter = eventBus.addHandler(InstancePresenter.class);
							instancePresenters.put(applianceInstance.getId(), presenter);
							view.getInstanceContainer().add(presenter.getView().asWidget());
						}
						
						presenter.setInstance(applianceInstance, true, false);
					}
					
					if(timer == null) {
						timer = new Timer() {
							@Override
							public void run() {
								loadApplianceInstances(true);
							}
						};
					}

					timer.schedule(REFRESH_MILIS);
				}
			}

			@Override
			public void onError(CloudFacadeError error) {
				eventBus.displayError(error);
				
				if(timer == null) {
					timer = new Timer() {
						@Override
						public void run() {
							loadApplianceInstances(true);
						}
					};
				}

				timer.schedule(REFRESH_MILIS);
			}
		});
	}
}