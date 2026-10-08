package io.mosip.biometrics.util.nist.wsq.encoder;

/**
 * Numeric helpers for the WSQ encoder, ported from public-domain NIST NBIS
 * {@code sround} in {@code util.c} / {@code dataio.c}.
 */
final class WsqUtil {

	/** Utility holder; not instantiated. */
	private WsqUtil() {
	}

	/**
	 * Rounds {@code value} to the nearest integer, matching NBIS {@code sround}
	 * (half away from zero). Used for 16-bit scaled shorts (SOF/DQT).
	 *
	 * @param value floating-point value to round
	 * @return nearest {@code int}
	 */
	static int sround(double value) {
		return value >= 0.0 ? (int) (value + 0.5) : (int) (value - 0.5);
	}

	/**
	 * Rounds {@code value} to the nearest unsigned 32-bit integer, matching NBIS
	 * {@code (unsigned int)sround} used for DTT filter mantissas. The small FBI
	 * taps (for example {@code 0.0378}) yield mantissas above
	 * {@link Integer#MAX_VALUE}; casting through {@code int} saturates in Java
	 * and writes a wrong filter, which reconstructs as a washed-out mesh.
	 *
	 * @param value floating-point value to round
	 * @return rounded value in {@code 0 .. 2^32-1}
	 */
	static long sroundU32(double value) {
		long rounded = value >= 0.0 ? (long) (value + 0.5) : (long) (value - 0.5);
		return rounded & 0xffffffffL;
	}
}
