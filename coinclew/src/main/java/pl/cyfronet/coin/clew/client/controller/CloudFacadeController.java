package pl.cyfronet.coin.clew.client.controller;

import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.clew.client.controller.beans.cf.AtomicService;

public class CloudFacadeController {
	public List<AtomicService> getAtomicServices() {
		AtomicService as1 = new AtomicService("Atomic service 1", "Atomic service 1 description");
		AtomicService as2 = new AtomicService("Atomic service 2", "Atomic service 2 description");
		
		return Arrays.asList(as1, as2);
	}
}