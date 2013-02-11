package pl.cyfronet.coin.portlet.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;

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
			atomicServiceInstance.setId(String.valueOf(currentTime++));
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
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		return atomicServices;
	}

	@Override
	public String createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		atomicService.setAtomicServiceId(String.valueOf(System.currentTimeMillis()));
		atomicServices.add(atomicService);
		return "id";
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.api.CloudFacade#getInitialConfigurations(java.lang.String)
	 */
	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId, boolean loadPayload) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.api.CloudFacade#addInitialConfiguration(java.lang.String, pl.cyfronet.coin.api.beans.InitialConfiguration)
	 */
	@Override
	public String addInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration)
			throws AtomicServiceNotFoundException,
			AtomicServiceInstanceNotFoundException, CloudFacadeException,
			InitialConfigurationAlreadyExistException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.api.CloudFacade#getEndpointDescriptor(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getEndpointDescriptor(String atomicServiceId,
			int servicePort, String invocationPath) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.api.CloudFacade#getServicesSet()
	 */
	@Override
	public String getServicesSet() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.coin.api.CloudFacade#getAtomicServiceId(java.lang.String, int, java.lang.String)
	 */
	@Override
	public String getAtomicServiceId(String atomicServiceId, int servicePort,
			String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}