package pl.cyfronet.coin.clew.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;

import pl.cyfronet.coin.clew.client.controller.cf.AtomicService;
import pl.cyfronet.coin.clew.client.controller.cf.AtomicServiceInstance;
import pl.cyfronet.coin.clew.client.controller.cf.AtomicServiceInstance.Status;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class CloudFacadeController {
	public interface AtomicServiceInstancesCallback {
		void processAtomicServiceInstances(List<AtomicServiceInstance> atomicServiceInstances);
	}
	
	public interface AtomicServicesCallback {
		void processAtomicService(List<AtomicService> atomicServices);
	}
	
	@Deprecated
	void onCors(ClickEvent event) {
		Resource r = new Resource("http://localhost:3000/api/v1/appliance_sets");
		r.get().send(new JsonCallback() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, JSONValue response) {
				Window.alert(response.toString());
			}
		});
	}
	
	public void getAtomicServices(final AtomicServicesCallback atomicServiceCallback) {
		final List<AtomicService> services = new ArrayList<AtomicService>();
		
		for (int i = 0; i < 20; i++) {
			services.add(new AtomicService("Atomic service " + i, "Atomic service " + i + " description"));
		}
		
		if (atomicServiceCallback != null) {
			atomicServiceCallback.processAtomicService(services);
		}
	}

	public void startAtomicServices(List<String> startIds, final Command command) {
		new Timer() {
			@Override
			public void run() {
				command.execute();
			}
		}.schedule(2000);
	}

	public void getAtomicServiceInstances(AtomicServiceInstancesCallback atomicServiceInstancesCallback) {
		List<AtomicServiceInstance> instances = new ArrayList<AtomicServiceInstance>();
		
		for (int i = 0; i < 20; i++) {
			AtomicServiceInstance asi = new AtomicServiceInstance();
			asi.setName("Atomic service instance " + i);
			asi.setIp("192.168.1." + (i + 1));
			asi.setLocation("CYFRONET");
			asi.setSpec("6 cores, 8GB RAM, 300GB disk");
			asi.setStatus(Status.booting);
			instances.add(asi);
		}
		
		if (atomicServiceInstancesCallback != null) {
			atomicServiceInstancesCallback.processAtomicServiceInstances(instances);
		}
	}

	public void shutdownAtomicServiceInstance(Command afterShutdown) {
		//TODO(DH): handle shutdown
		afterShutdown.execute();
	}
}