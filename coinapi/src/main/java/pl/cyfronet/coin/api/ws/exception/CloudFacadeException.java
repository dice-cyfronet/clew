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
package pl.cyfronet.coin.api.ws.exception;

import javax.xml.ws.WebFault;

/**
 * The most generic cloud facade exception. It should be thrown when e.g.
 * connection with Air, Atmosphere, etc. fails.
 * @author <a href="d.harezlak@cyfronet.pl>Daniel Harezlak</a>
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@WebFault
public class CloudFacadeException extends Exception {
	private static final long serialVersionUID = -1200644885931600000L;

	private ErrorCode errorCode;

	public enum ErrorCode {
		UNKNOWN;
	}

	public CloudFacadeException() {
		errorCode = ErrorCode.UNKNOWN;
	}

	public CloudFacadeException(String message) {
		super(message);
		errorCode = ErrorCode.UNKNOWN;
	}

	public CloudFacadeException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public CloudFacadeException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}