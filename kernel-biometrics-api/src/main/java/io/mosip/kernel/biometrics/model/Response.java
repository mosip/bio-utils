package io.mosip.kernel.biometrics.model;

import lombok.Data;

/**
 * Represents a generic response structure containing status information and a
 * payload of type T.
 *
 * @param <T> the type of the response payload
 * @author Manoj SP
 * @since 1.0.0
 */
@Data
public class Response<T> {
	/**
	 * The status code indicating the outcome of the operation.
	 */
	private Integer statusCode;

	/**
	 * A message describing the status or outcome of the operation.
	 */
	private String statusMessage;

	/**
	 * The payload containing the actual response data, of type T.
	 */
	@SuppressWarnings({ "java:S1700" }) // Suppress unnecessary warning about generic type T
	private T response;
}