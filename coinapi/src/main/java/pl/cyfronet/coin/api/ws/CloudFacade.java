/*
 * Copyright 2011 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pl.cyfronet.coin.api.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.ws.exception.CloudFacadeException;

@WebService(targetNamespace = "http://cyfronet.pl/coin")
public interface CloudFacade {

	@WebMethod(operationName = "getAtomicServiceInstances")
	List<AtomicServiceInstance> getAtomicServiceInstances(String contextId)
			throws CloudFacadeException;

	@WebMethod(operationName = "getAtomicServices")
	@WebResult(name = "atomicServices")
	List<AtomicService> getAtomicServices() throws CloudFacadeException;

	@WebMethod(operationName = "startAtomicService")
	@WebResult(name = "atomicServiceInstanceId")
	String startAtomicService(
			@WebParam(name = "atomicServiceId") String atomicServiceId,
			@WebParam(name = "contextId") String contextId)
			throws CloudFacadeException;

	@WebMethod(operationName = "getAtomicServiceStatus")
	@WebResult(name = "atomicServiceInstanceStatus")
	AtomicServiceInstance getAtomicServiceStatus(
			@WebParam(name = "atomicServiceInstanceId") String atomicServiceInstanceId)
			throws CloudFacadeException;

	@WebMethod(operationName = "stopAtomicServiceInstance")
	void stopAtomicServiceInstance(
			@WebParam(name = "atomicServiceInstance") String atomicServiceInstance)
			throws CloudFacadeException;

	@WebMethod(operationName = "createAtomicService")
	void createAtomicService(
			@WebParam(name = "atomicServiceInstanceId") String atomicServiceInstanceId,
			@WebParam(name = "atomicService") AtomicService atomicService)
			throws CloudFacadeException;
}