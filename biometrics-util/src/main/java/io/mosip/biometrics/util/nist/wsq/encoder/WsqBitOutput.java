package io.mosip.biometrics.util.nist.wsq.encoder;

import java.io.ByteArrayOutputStream;

/**
 * Big-endian marker writer and packed-bit Huffman sink, ported from public-domain
 * NIST NBIS {@code dataio.c} ({@code putc_*} / {@code write_bits} /
 * {@code flush_bits}).
 * <p>
 * Any output byte equal to {@code 0xFF} is followed by a stuffed zero so that
 * marker codes cannot appear inside Huffman data (jnbis rejects missing stuffing
 * with “No stuffed zeros”).
 * </p>
 */
final class WsqBitOutput {

	/** Destination buffer for markers and stuffed Huffman bytes. */
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	/** Next bit position to fill inside {@link #bits}, 7 = MSB of the current byte. */
	private int outbit = 7;
	/** Partially assembled output byte. */
	private int bits = 0;

	/**
	 * Writes an unsigned 16-bit value in network byte order.
	 *
	 * @param value value to write; only the low 16 bits are used
	 */
	void putUShort(int value) {
		out.write((value >> 8) & 0xff);
		out.write(value & 0xff);
	}

	/**
	 * Writes a single unsigned byte.
	 *
	 * @param value value to write; only the low 8 bits are used
	 */
	void putByte(int value) {
		out.write(value & 0xff);
	}

	/**
	 * Writes an unsigned 32-bit value in network byte order.
	 *
	 * @param value value to write; only the low 32 bits are used
	 */
	void putUInt(long value) {
		out.write((int) ((value >> 24) & 0xff));
		out.write((int) ((value >> 16) & 0xff));
		out.write((int) ((value >> 8) & 0xff));
		out.write((int) (value & 0xff));
	}

	/**
	 * Appends raw bytes (already stuffed or marker payload).
	 *
	 * @param data bytes to copy
	 */
	void putBytes(byte[] data) {
		out.write(data, 0, data.length);
	}

	/**
	 * Packs {@code size} bits of {@code code} (MSB first) into the stream, stuffing
	 * a zero after any {@code 0xFF} byte.
	 *
	 * @param code Huffman code or extra-bits payload
	 * @param size number of bits to emit, from bit {@code size - 1} down to 0
	 */
	void writeBits(int code, int size) {
		for (int num = size - 1; num >= 0; num--) {
			bits <<= 1;
			bits |= (code >> num) & 0x0001;
			if (--outbit < 0) {
				out.write(bits);
				if ((bits & 0xff) == 0xff) {
					out.write(0);
				}
				outbit = 7;
				bits = 0;
			}
		}
	}

	/**
	 * Pads any remaining bits in the current byte with ones (NBIS
	 * {@code flush_bits}) and applies {@code 0xFF} stuffing if needed.
	 */
	void flushBits() {
		if (outbit != 7) {
			for (int cnt = outbit; cnt >= 0; cnt--) {
				bits <<= 1;
				bits |= 0x01;
			}
			out.write(bits);
			if ((bits & 0xff) == 0xff) {
				out.write(0);
			}
			outbit = 7;
			bits = 0;
		}
	}

	/**
	 * Returns the bytes written so far.
	 *
	 * @return a newly allocated copy of the stream contents
	 */
	byte[] toByteArray() {
		return out.toByteArray();
	}
}
