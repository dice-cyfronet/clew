package pl.cyfronet.coin.portlet.mock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance.Status;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;

public class MockCloudFacade implements CloudFacade {
	private static final Logger log = LoggerFactory.getLogger(MockCloudFacade.class);
	
	private List<AtomicServiceInstance> atomicServiceInstances;
	private List<AtomicService> atomicServices;
	
	public MockCloudFacade(int initialNumberOfAtomicServiceInstances,
			int initialNumberOfAtomicServices) {
		atomicServiceInstances = new ArrayList<AtomicServiceInstance>();
		atomicServices = new ArrayList<AtomicService>();
		
		long currentTime = System.currentTimeMillis();
		Random random = new Random(currentTime);
		
		for(int i = 0; i < initialNumberOfAtomicServices; i++) {
			AtomicService atomicService = new AtomicService();
			atomicService.setAtomicServiceId(String.valueOf(currentTime++));
			atomicService.setName("Mock atomic service nr " + (i + 1));
			atomicService.setDescription("Mock atomic service description which is slightly longer than the name");
			log.debug("Created mock atomic service {}", atomicService);
			atomicServices.add(atomicService);
		}
		
		for(int i = 0; i < initialNumberOfAtomicServiceInstances; i++) {
			AtomicServiceInstance atomicServiceInstance = new AtomicServiceInstance();
			atomicServiceInstance.setInstanceId(String.valueOf(currentTime++));
			atomicServiceInstance.setName("Mock atomic service instance nr " + (i + 1));
			atomicServiceInstance.setStatus(Status.values()[random.nextInt(Status.values().length)]);
			
			AtomicService atomicService = atomicServices.get(
					random.nextInt(initialNumberOfAtomicServices));
			atomicServiceInstance.setAtomicServiceId(atomicService.getAtomicServiceId());
			log.debug("Created mock atomic service instance {}", atomicServiceInstance);
			atomicServiceInstances.add(atomicServiceInstance);
		}
	}
	
	@Override
	public List<AtomicServiceInstance> getAtomicServiceInstances(
			String contextId) throws CloudFacadeException {
		return atomicServiceInstances;
	}

	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		return atomicServices;
	}

	@Override
	public String startAtomicServiceInstance(String atomicServiceId,
			String name, String contextId) throws AtomicServiceNotFoundException,
			CloudFacadeException {
		AtomicService atomicService = null;
		
		for(AtomicService as : atomicServices) {
			if(as.getAtomicServiceId().equals(atomicServiceId)) {
				atomicService = as;
				break;
			}
		}
		
		if(atomicService != null) {
			AtomicServiceInstance atomicServiceInstance = new AtomicServiceInstance();
			atomicServiceInstance.setAtomicServiceId(atomicService.getAtomicServiceId());
			atomicServiceInstance.setName(name);
			atomicServiceInstance.setInstanceId(String.valueOf(System.currentTimeMillis()));
			atomicServiceInstance.setStatus(Status.Paused);
			atomicServiceInstances.add(atomicServiceInstance);
			
			return atomicServiceInstance.getInstanceId();
		} else {
			throw new AtomicServiceNotFoundException();
		}
	}

	@Override
	public AtomicServiceInstance getAtomicServiceInstance(
			String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		for(AtomicServiceInstance asi : atomicServiceInstances) {
			if(asi.getInstanceId().equals(atomicServiceInstanceId)) {
				return asi;
			}
		}
		
		return null;
	}

	@Override
	public void stopAtomicServiceInstance(String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		for(Iterator<AtomicServiceInstance> i = atomicServiceInstances.iterator(); i.hasNext();) {
			AtomicServiceInstance asi = i.next();
			
			if(asi.getInstanceId().equals(atomicServiceInstanceId)) {
				i.remove();
				break;
			}
		}
	}

	@Override
	public void createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		atomicService.setAtomicServiceId(String.valueOf(System.currentTimeMillis()));
		atomicServices.add(atomicService);
	}
}