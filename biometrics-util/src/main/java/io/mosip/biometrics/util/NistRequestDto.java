package io.mosip.biometrics.util;

import lombok.Data;

/**
 * Data Transfer Object (DTO) to hold XML file data in byte array format.
 */
@Data
public class NistRequestDto {
	 /**
     * Byte array representing the XML file data.
     */
	private byte[] inputBytes;
}