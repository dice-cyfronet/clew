package pl.cyfronet.coin.impl.utils;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.CloudFacadeException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class Validator {

	private static final Logger logger = LoggerFactory
			.getLogger(Validator.class);

	/**
	 * MongoDB object id regex.
	 */
	private static final String MONGODB_OBJECT_ID_REGEXP = "[a-f0-9]{24}";

	private static final String REDIRECTION_REGEXP = "[a-zA-Z0-9_-]+";

	/**
	 * Check if given string fulfill given regexp.
	 * @param id Id to be checked
	 * @param regexp Valid format regexp.
	 * @return True if id is valid.
	 */
	private static boolean isValid(String value, String regexp) {
		return value != null && value.matches(regexp);
	}

	/**
	 * Checks if id is valid. If not than CloudFacadeException with return code
	 * 400 is thrown.
	 * @param id Id to be checked.
	 */
	public static void validateId(String... ids) throws CloudFacadeException {
		for (String id : ids) {
			if (!isValid(id, MONGODB_OBJECT_ID_REGEXP)) {
				logger.info("Id {} is not valid", id);
				throw new CloudFacadeException(String.format(
						"%s is not a valid id", id),
						Response.Status.BAD_REQUEST);
			}
		}
	}

	/**
	 * Checks if redirection name is valid. If not than CloudFacadeException
	 * with return code 400 is thrown.
	 * @param id Redirection name to be checked.
	 */
	public static void validateRedirectionName(String redirectionName) {
		if (!isValid(redirectionName, REDIRECTION_REGEXP)) {
			logger.debug("Id {} is not valid redirection name", redirectionName);
			throw new CloudFacadeException(String.format(
					"%s is not a valid redirection name", redirectionName),
					Response.Status.BAD_REQUEST);
		}
	}
}
