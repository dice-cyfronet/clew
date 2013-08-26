package pl.cyfronet.coin.clew.client.controller;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.clew.client.controller.beans.cf.AtomicService;

public class CloudFacadeController {
	public List<AtomicService> getAtomicServices() {
		List<AtomicService> services = new ArrayList<AtomicService>();
		
		for (int i = 0; i < 20; i++) {
			services.add(new AtomicService("Atomic service " + i, "Atomic service " + i + " description"));
		}
		
		return services;
	}
}