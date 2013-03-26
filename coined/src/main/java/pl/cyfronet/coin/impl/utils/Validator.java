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

	/**
	 * Check if given string is a valid MongoDB id.
	 * @param id Id to be checked
	 * @return True if id is valid.
	 */
	private static boolean isObjectId(String id) {
		return id != null && id.matches(MONGODB_OBJECT_ID_REGEXP);
	}

	/**
	 * Checks if id is valid. If not than CloudFacadeException with return code
	 * 400 is thrown.
	 * @param id Id to be checked.
	 */
	public static void validateId(String... ids) throws CloudFacadeException {
		for (String id : ids) {
			if (!isObjectId(id)) {
				logger.info("Id {} is not valid", id);
				throw new CloudFacadeException(String.format(
						"%s is not a valid id", id),
						Response.Status.BAD_REQUEST);
			}
		}
	}
}
