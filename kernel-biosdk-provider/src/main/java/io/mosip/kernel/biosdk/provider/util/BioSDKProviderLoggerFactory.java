package io.mosip.kernel.biosdk.provider.util;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.logger.logback.factory.Logfactory;

/**
 * Factory class for obtaining logger instances specific to the BioSDKProvider.
 */
public class BioSDKProviderLoggerFactory {
	/**
	 * Private constructor to prevent instantiation of the factory class.
	 */
	private BioSDKProviderLoggerFactory() {
	}

	/**
	 * Retrieves a logger instance for the specified class.
	 *
	 * @param clazz the Class for which the logger instance is requested
	 * @return a logger instance configured for the BioSDKProvider
	 */
	public static Logger getLogger(Class<?> clazz) {
		return Logfactory.getSlf4jLogger(clazz);
	}
}