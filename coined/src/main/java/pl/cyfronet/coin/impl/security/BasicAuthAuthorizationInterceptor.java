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

package pl.cyfronet.coin.impl.security;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * CXF Interceptor that provides HTTP Basic Authentication validation. Based on
 * the concepts outline here:
 * http://chrisdail.com/2008/03/31/apache-cxf-with-http-basic-authentication
 * @author CDail
 */
public class BasicAuthAuthorizationInterceptor extends SoapHeaderInterceptor {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BasicAuthAuthorizationInterceptor.class);

	/**
	 * Map of allowed users to this system with their corresponding passwords.
	 */
	private Map<String, String> users;

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apache.cxfgetClass().binding.soap.interceptor.SoapHeaderInterceptor
	 * #handleMessage (org.apache.cxf.message.Message)
	 */
	@Override
	public void handleMessage(Message message) {
		// This is set by CXF
		AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);

		// If the policy is not set, the user did not specify credentials
		// A 401 is sent to the client to indicate that authentication is
		// required
		if (policy == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("User attempted to log in with no credentials");
			}
			sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
			return;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Logging in use: " + policy.getUserName());
		}

		// Verify the password
		String realPassword = users.get(policy.getUserName());
		if (realPassword == null || !realPassword.equals(policy.getPassword())) {
			LOGGER.warn("Invalid username or password for user: "
					+ policy.getUserName());
			sendErrorResponse(message, HttpURLConnection.HTTP_FORBIDDEN);
		}
	}

	/**
	 * Set error response to the message.
	 * @param message Message.
	 * @param responseCode Response code.
	 */
	@SuppressWarnings("unchecked")
	private void sendErrorResponse(Message message, int responseCode) {
		Message outMessage = getOutMessage(message);
		outMessage.put(Message.RESPONSE_CODE, responseCode);

		// Set the response headers
		Map<String, List<String>> responseHeaders = (Map<String, List<String>>) message
				.get(Message.PROTOCOL_HEADERS);
		if (responseHeaders != null) {
			responseHeaders.put("WWW-Authenticate",
					Arrays.asList(new String[] { "Basic realm=realm" }));
			responseHeaders.put("Content-Length",
					Arrays.asList(new String[] { "0" }));
		}
		message.getInterceptorChain().abort();
		try {
			getConduit(message).prepare(outMessage);
			close(outMessage);
		} catch (IOException e) {
			LOGGER.warn(e.getMessage(), e);
		}
	}

	/**
	 * Get out message correlated with in message.
	 * @param inMessage In message.
	 * @return Out message.
	 */
	private Message getOutMessage(Message inMessage) {
		Exchange exchange = inMessage.getExchange();
		Message outMessage = exchange.getOutMessage();
		if (outMessage == null) {
			Endpoint endpoint = exchange.get(Endpoint.class);
			outMessage = endpoint.getBinding().createMessage();
			exchange.setOutMessage(outMessage);
		}
		outMessage.putAll(inMessage);
		return outMessage;
	}

	/**
	 * Get conduit from message.
	 * @param message Message.
	 * @return Message conduit.
	 * @throws IOException Thrown when error occurs while getting message
	 *             conduit.
	 */
	private Conduit getConduit(Message message) throws IOException {
		Exchange exchange = message.getExchange();
		EndpointReferenceType target = exchange
				.get(EndpointReferenceType.class);
		Conduit conduit = exchange.getDestination().getBackChannel(message,
				null, target);
		exchange.setConduit(conduit);
		return conduit;
	}

	/**
	 * Close message.
	 * @param message Message to be closed.
	 * @throws IOException Thrown when closing finished with error.
	 */
	private void close(Message message) throws IOException {
		OutputStream os = message.getContent(OutputStream.class);
		os.flush();
		os.close();
	}

	/**
	 * Set user names and passwords.
	 * @param users Information about authorized users.
	 */
	@Required
	public void setUsers(Map<String, String> users) {
		this.users = users;
	}
}
