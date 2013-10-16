package pl.cyfronet.coin.clew.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeInstance;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypesResponse;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeService;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.NewApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceTypeInstance.Status;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class CloudFacadeController {
	public interface ApplianceTypeInstancesCallback {
		void processApplianceTypeInstances(List<ApplianceTypeInstance> applianceTypeInstances);
	}
	
	public interface ApplianceTypesCallback {
		void processApplianceTypes(List<ApplianceType> applianceTypes);
	}
	
	private ApplianceTypeService applianceTypesService;
	
	@Inject
	public CloudFacadeController(ApplianceTypeService applianceTypesService) {
		this.applianceTypesService = applianceTypesService;
		
	}
	
	public void getApplianceTypes(final ApplianceTypesCallback applianceTypesCallback) {
		applianceTypesService.getApplianceTypes(new MethodCallback<ApplianceTypesResponse>() {
			@Override
			public void onSuccess(Method method, ApplianceTypesResponse response) {
				if (applianceTypesCallback != null) {
					applianceTypesCallback.processApplianceTypes(response.getApplianceTypes());
				}
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
			}
		});
	}

	public void startApplianceTypes(List<String> startIds, final Command command) {
		new Timer() {
			@Override
			public void run() {
				command.execute();
			}
		}.schedule(2000);
	}

	public void getApplianceTypeInstances(ApplianceTypeInstancesCallback applianceTypeInstancesCallback) {
		List<ApplianceTypeInstance> instances = new ArrayList<ApplianceTypeInstance>();
		
		for (int i = 0; i < 20; i++) {
			ApplianceTypeInstance asi = new ApplianceTypeInstance();
			asi.setName("Atomic service instance " + i);
			asi.setIp("192.168.1." + (i + 1));
			asi.setLocation("CYFRONET");
			asi.setSpec("6 cores, 8GB RAM, 300GB disk");
			asi.setStatus(Status.booting);
			instances.add(asi);
		}
		
		if (applianceTypeInstancesCallback != null) {
			applianceTypeInstancesCallback.processApplianceTypeInstances(instances);
		}
	}

	public void shutdownApplianceTypeInstance(Command afterShutdown) {
		//TODO(DH): handle shutdown
		afterShutdown.execute();
	}

	public void addApplianceType(NewApplianceType newApplianceType, final Command after) {
		
	}
}