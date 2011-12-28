package pl.cyfronet.coin.portlet.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;

public class MockCloudFacade implements CloudFacade {
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
			atomicServices.add(atomicService);
		}
		
		for(int i = 0; i < initialNumberOfAtomicServiceInstances; i++) {
			AtomicServiceInstance atomicServiceInstance = new AtomicServiceInstance();
			atomicServiceInstance.setInstanceId(String.valueOf(currentTime++));
			atomicServiceInstance.setName("Mock atomic service instance nr " + (i + 1));
			atomicServiceInstance.setAtomicServiceId(
					atomicServices.get(random.nextInt(initialNumberOfAtomicServices)).getAtomicServiceId());
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
			String contextId) throws AtomicServiceNotFoundException,
			CloudFacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AtomicServiceInstance getAtomicServiceInstance(
			String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopAtomicServiceInstance(String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		// TODO Auto-generated method stub

	}
}