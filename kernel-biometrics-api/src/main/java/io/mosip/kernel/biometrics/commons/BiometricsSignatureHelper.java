package io.mosip.kernel.biometrics.commons;

import java.nio.charset.StandardCharsets;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.core.exception.BiometricSignatureValidationException;
import io.mosip.kernel.core.util.CryptoUtil;

/**
 * Utility class for processing Biometric Information Records (BIR) and
 * extracting JWT tokens.
 *
 * This class provides static methods for extracting JWT tokens from BIR objects
 * and performing basic validation checks. It throws exceptions to indicate
 * potential issues during processing.
 *
 */
public class BiometricsSignatureHelper {

	private BiometricsSignatureHelper() {
		throw new IllegalStateException("BiometricsSignatureHelper class");
	}

	/**
	 * Extracts a JWT token from a provided BIR object.
	 *
	 * This method performs the following steps to extract a JWT token: 1. Retrieves
	 * the "Others" map from the BIR object. 2. Throws an exception if the "Others"
	 * map is null or empty. 3. Decodes the BIR's "Sb" value using UTF-8 encoding.
	 * 4. Encodes the BIR's "Bdb" value using URL-safe Base64 encoding. 5. Iterates
	 * through the "Others" map entries. 6. If an entry key matches "PAYLOAD": -
	 * Replaces "<bioValue>" in the entry value with the URL-safe Base64 encoded
	 * "Bdb". - Encodes the modified payload value using URL-safe Base64 encoding. -
	 * Constructs the JWT token by replacing ".." in the "Sb" value with "." +
	 * encodedPayloadValue + ".". - Parses the modified payload value as a JSON
	 * object. - Extracts the "digitalId" value from the JSON object. - Calls
	 * `compareJWTForSameCertificates` to validate certificate consistency
	 * (optional). 7. Returns the constructed JWT token (if found).
	 *
	 * @param bir The BIR object containing the biometric data.
	 * @return The extracted JWT token string, or null if not found.
	 * @throws BiometricSignatureValidationException If any validation error occurs
	 *                                               during processing.
	 * @throws JSONException                         If an error occurs while
	 *                                               parsing the JSON payload.
	 */
	public static String extractJWTToken(BIR bir) throws BiometricSignatureValidationException, JSONException {
		String constructedJWTToken = null;

		Map<String, String> othersInfo = bir.getOthers();
		if (othersInfo == null || othersInfo.isEmpty()) {
			throw new BiometricSignatureValidationException("Others value is null / empty inside BIR");
		}

		String sb = new String(bir.getSb(), StandardCharsets.UTF_8);
		String bdb = CryptoUtil.encodeToURLSafeBase64(bir.getBdb());

		for (Map.Entry<String, String> entry : othersInfo.entrySet()) {
			if ("PAYLOAD".equals(entry.getKey())) {
				String value = entry.getValue().replace("<bioValue>", bdb);
				String encodedPayloadValue = CryptoUtil.encodeToURLSafeBase64(value.getBytes(StandardCharsets.UTF_8));
				constructedJWTToken = constructJWTToken(sb, encodedPayloadValue);

				JSONObject jsonObject = new JSONObject(value);
				String digitalID = jsonObject.optString("digitalId", null);
				if (digitalID != null) {
					compareJWTForSameCertificates(constructedJWTToken, digitalID);
				}
				//break; // Stop iterating once found
			}
		}

		return constructedJWTToken;
	}

	/**
	 * Compares two JWT strings to ensure they share the same certificates in the
	 * header.
	 *
	 * This method is optional and performs additional validation by comparing the
	 * certificate chains (x5c) in the headers of two provided JWT strings. It
	 * throws an exception if the certificates don't match.
	 *
	 * @param jwtString1 The first JWT string for comparison.
	 * @param jwtString2 The second JWT string for comparison.
	 * @throws JSONException                         If an error occurs while
	 *                                               parsing the JSON headers.
	 * @throws BiometricSignatureValidationException If the certificate chains in
	 *                                               the headers don't match.
	 */
	private static void compareJWTForSameCertificates(String jwtString1, String jwtString2)
			throws JSONException, BiometricSignatureValidationException {
		Set<String> jwt1Certs = extractCertificatesFromJWT(jwtString1);
		Set<String> jwt2Certs = extractCertificatesFromJWT(jwtString2);

		if (!jwt1Certs.containsAll(jwt2Certs)) {
			throw new BiometricSignatureValidationException("Header Certificate mismatch");
		}
	}

	/**
	 * Extracts the X.509 certificate chain ("x5c") from the header of a JWT string.
	 * <p>
	 * This method:
	 * <ol>
	 *   <li>Validates that the JWT string is not null and contains the expected '.' delimiters.</li>
	 *   <li>Splits the JWT into its parts (header, payload, signature).</li>
	 *   <li>Decodes the JWT header from Base64 URL-safe encoding into a JSON object.</li>
	 *   <li>Extracts the "x5c" array from the header if present.</li>
	 *   <li>Returns the certificates as a {@link Set} of strings for fast lookup and comparison.</li>
	 * </ol>
	 * If the JWT is malformed or the "x5c" array is missing, an empty set is returned.
	 *
	 * @param jwtString The JWT token string containing the header, payload, and signature, separated by dots.
	 * @return A {@link Set} of certificate strings extracted from the "x5c" array in the JWT header,
	 *         or an empty set if none are found or the token is invalid.
	 * @throws JSONException If the JWT header cannot be parsed as valid JSON or the "x5c" field is malformed.
	 */
	private static Set<String> extractCertificatesFromJWT(String jwtString) throws JSONException {
		if (jwtString == null || !jwtString.contains(".")) {
			return Collections.emptySet();
		}

		String[] parts = jwtString.split("\\.");
		if (parts.length < 1) {
			return Collections.emptySet();
		}

		String headerJson = new String(CryptoUtil.decodeURLSafeBase64(parts[0]), StandardCharsets.UTF_8);
		JSONObject headerObject = new JSONObject(headerJson);
		JSONArray certArray = headerObject.optJSONArray("x5c");

		if (certArray == null) return Collections.emptySet();

		Set<String> certificates = new HashSet<>(certArray.length());
		for (int i = 0; i < certArray.length(); i++) {
			certificates.add(certArray.getString(i));
		}
		return certificates;
	}

	/**
	 * Constructs a JWT token string by combining the Signing Base (Sb), encoded
	 * payload value, and separator.
	 *
	 * This method takes a string containing the Signing Base (Sb) and a URL-safe
	 * Base64 encoded payload value. It replaces the ".." sequence in the Sb with a
	 * "." separator, followed by the encoded payload value, and another "."
	 * separator, effectively creating the three parts (header, payload, signature)
	 * of a JWT token structure.
	 *
	 * @param sb                  The Signing Base string.
	 * @param encodedPayloadValue The URL-safe Base64 encoded payload value.
	 * @return The constructed JWT token string.
	 */
	private static String constructJWTToken(String sb, String encodedPayloadValue) {
		return sb.replace("..", "." + encodedPayloadValue + ".");
	}
}