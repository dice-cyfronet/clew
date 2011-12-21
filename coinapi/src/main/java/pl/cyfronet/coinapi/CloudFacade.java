package pl.cyfronet.coinapi;

import java.util.List;

import pl.cyfronet.coinapi.beans.AtomicService;
import pl.cyfronet.coinapi.beans.AtomicServiceInstance;

public interface CloudFacade {
	List<AtomicServiceInstance> getAtomicServiceInstances(String contextId) throws CloudFacadeException;
	List<AtomicService> getAtomicServices();
	String startAtomicService(String atomicServiceId, String contextId);
	AtomicServiceInstance getAtomicServiceStatus(String atomicServiceInstanceId);
	void stopAtomicServiceInstance(String atomicServiceInstance);
	void createAtomicServiceSnapshot(String atomicServiceInstanceId, AtomicService atomicService);
}