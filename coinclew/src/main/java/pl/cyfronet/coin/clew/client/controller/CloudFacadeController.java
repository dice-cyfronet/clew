package pl.cyfronet.coin.clew.client.controller;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.clew.client.controller.beans.cf.AtomicService;
import pl.cyfronet.coin.clew.client.controller.beans.cf.AtomicServiceInstance;
import pl.cyfronet.coin.clew.client.controller.beans.cf.AtomicServiceInstance.Status;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;

public class CloudFacadeController {
	public interface AtomicServiceInstancesCallback {
		void processAtomicServiceInstances(List<AtomicServiceInstance> atomicServiceInstances);
	}
	
	public List<AtomicService> getAtomicServices() {
		List<AtomicService> services = new ArrayList<AtomicService>();
		
		for (int i = 0; i < 20; i++) {
			services.add(new AtomicService("Atomic service " + i, "Atomic service " + i + " description"));
		}
		
		return services;
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
		
		atomicServiceInstancesCallback.processAtomicServiceInstances(instances);
	}

	public void shutdownAtomicServiceInstance(Command afterShutdown) {
		//TODO(DH): handle shutdown
		afterShutdown.execute();
	}
}