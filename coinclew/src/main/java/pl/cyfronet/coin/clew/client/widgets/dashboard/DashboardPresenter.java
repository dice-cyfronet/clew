package pl.cyfronet.coin.clew.client.widgets.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.common.BasePresenter;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceInstancesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance.Status;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class DashboardPresenter extends BasePresenter implements Presenter {
	public interface View extends IsWidget {
		void showStartAppPopup();
		void clearAppsTable();
		void setAppsSpinnerVisible(boolean visible);
		void addStartButton(int i);
		HasValue<Boolean> addCheckButton(int i);
		void setAppVisibility(int i, boolean b);
		void setStartSelectedWidgetBusyState(boolean b);
		void hideStartAppPopup();
		void setStartAppWidgetBusyState(int i, boolean b);
		void addAppDescription(int i, String description);
		void addAppName(int i, String name);
		void setInstanceName(int i, String name);
		void setInstanceIp(int i, String ip);
		void setInstanceActionsAndDetails(int i);
		void setInstanceStatus(int i, Status status);
		void setInstanceSpec(int i, String spec);
		void setInstanceLocation(int i, String location);
		void confirmShutdown(Command command);
		void toggleInstanceDetails(int i);
	}
	
	private final static Logger log = LoggerFactory.getLogger(DashboardPresenter.class);
	
	private View view;
	private CloudFacadeController cloudFacadeController;
	private List<ApplianceType> applianceTypes;
	private List<HasValue<Boolean>> appChecks;
	private List<ApplianceInstance> instances;
	
	@Inject
	public DashboardPresenter(View view, CloudFacadeController cloudFacadeController) {
		this.view = view;
		this.cloudFacadeController = cloudFacadeController;
		appChecks = new ArrayList<HasValue<Boolean>>();
	}
	
	public void load() {
		cloudFacadeController.getApplianceInstances(new ApplianceInstancesCallback() {
			@Override
			public void processApplianceInstances(List<ApplianceInstance> applianceInstances) {
				instances = applianceInstances;
				int i = 0;
				
				for (ApplianceInstance asi : instances) {
					view.setInstanceName(i, asi.getId());
//					view.setInstanceName(i, asi.getName());
//					view.setInstanceIp(i, asi.getIp());
//					view.setInstanceLocation(i, asi.getLocation());
//					view.setInstanceSpec(i, asi.getSpec());
//					view.setInstanceStatus(i, asi.getStatus());
					view.setInstanceActionsAndDetails(i);
					i++;
				}
			}
		});
	}
	
	@Override
	public IsWidget getWidget() {
		return view;
	}

	@Override
	public void onShowStartAppModal() {
		view.clearAppsTable();
		view.setAppsSpinnerVisible(true);
		view.showStartAppPopup();
		cloudFacadeController.getApplianceTypes(new ApplianceTypesCallback() {
			@Override
			public void processApplianceTypes(List<ApplianceType> applianceTypes) {
				DashboardPresenter.this.applianceTypes = applianceTypes;
				view.setAppsSpinnerVisible(false);
				
				int i = 0;
				appChecks.clear();
				
				for(ApplianceType atomicService : applianceTypes) {
					view.addStartButton(i);
					appChecks.add(view.addCheckButton(i));
					view.addAppName(i, atomicService.getName());
					view.addAppDescription(i, atomicService.getDescription());
					i++;
				}
			}
		});
	}

	@Override
	public void onFilter(String text) {
		log.debug("Filtering apps for {}", text);
		int i = 0;
		
		for (ApplianceType applianceType : applianceTypes) {
			if (applianceType.getName() != null && applianceType.getName().contains(text) ||
					applianceType.getDescription() != null && applianceType.getDescription().contains(text)) {
				view.setAppVisibility(i, true);
			} else {
				view.setAppVisibility(i, false);
			}
			
			i++;
		}
	}

	@Override
	public void onStartSelected() {
		List<String> startIds = new ArrayList<String>();
		int i = 0;
		
		for (HasValue<Boolean> selected : appChecks) {
			if (selected.getValue()) {
				startIds.add(applianceTypes.get(i).getId());
			}
			
			i++;
		}
		
		log.info("Starting appliance types with ids {}", startIds);
		view.setStartSelectedWidgetBusyState(true);
		cloudFacadeController.startApplianceTypes(startIds, new Command() {
			@Override
			public void execute() {
				view.setStartSelectedWidgetBusyState(false);
				view.hideStartAppPopup();
			}
		});
	}

	@Override
	public void onStartSingle(final int i) {
		log.info("Starting single appliance type {}", applianceTypes.get(i).getId());
		view.setStartAppWidgetBusyState(i, true);
		cloudFacadeController.startApplianceTypes(
				Arrays.asList(applianceTypes.get(i).getId()), new Command() {
					@Override
					public void execute() {
						view.setStartAppWidgetBusyState(i, false);
						view.hideStartAppPopup();
					}
				});
	}

	@Override
	public void onInstanceDetailsShow(int i) {
		view.toggleInstanceDetails(i);
	}

	@Override
	public void onInstanceShutdown(int i) {
		view.confirmShutdown(new Command() {
			@Override
			public void execute() {
				cloudFacadeController.shutdownApplianceInstance(new Command() {
					@Override
					public void execute() {
						Window.alert("Handle post-shutdown action");
					}
				});
			}});
	}
}