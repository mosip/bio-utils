package io.mosip.biometrics.util.nist.wsq.encoder;

import java.nio.charset.StandardCharsets;

/**
 * Public WSQ grayscale encoder ported from public-domain NIST NBIS
 * {@code wsq_encode_mem} ({@code imgtools/src/lib/wsq/encoder.c}).
 * <p>
 * The C sources are U.S. Government works and not subject to copyright
 * (17 U.S.C. § 105). This Java port is licensed with biometrics-util (MPL 2.0).
 * Output is a CJIS WSQ bitstream that {@code org.jnbis.internal.WsqDecoder} can
 * decode.
 * </p>
 */
public final class WsqEncoder {

	/**
	 * FBI-typical fingerprint WSQ bitrate in bits per pixel (0.75, lossy). Passed
	 * to {@link #encode(byte[], int, int, int, float, String)} when the caller
	 * does not specify a rate.
	 */
	public static final float DEFAULT_BIT_RATE = WsqConstants.DEFAULT_BIT_RATE;

	/** Utility holder; not instantiated. */
	private WsqEncoder() {
	}

	/**
	 * Encodes packed 8-bit grayscale pixels to a <strong>lossy</strong> WSQ
	 * bitstream at the FBI fingerprint bitrate ({@link #DEFAULT_BIT_RATE}).
	 *
	 * @param gray   packed 8-bit pixels
	 * @param width  image width in pixels
	 * @param height image height in pixels
	 * @param ppi    scan resolution; default 500 when {@code <= 0}
	 * @param comment optional extra COM segment
	 * @return WSQ bytes
	 */
	public static byte[] encode(byte[] gray, int width, int height, int ppi, String comment) {
		return encode(gray, width, height, ppi, DEFAULT_BIT_RATE, comment);
	}

	/**
	 * Encodes packed 8-bit grayscale pixels to a <strong>lossy</strong> WSQ
	 * bitstream at {@code bitRate} bits per pixel.
	 * <p>
	 * Pipeline: normalize → 5-level wavelet trees → FBI analysis filters →
	 * variance-based quantization → two Huffman tables (block 1, then blocks
	 * 2+3) → stuffed bitstream with SOI/COM/DTT/DQT/SOF/DHT/SOB/EOI markers.
	 * </p>
	 *
	 * @param gray    packed 8-bit pixels, length at least {@code width * height}
	 *                (only the first {@code width * height} samples are read)
	 * @param width   image width in pixels; must be positive
	 * @param height  image height in pixels; must be positive
	 * @param ppi     scan resolution written into the NIST comment; {@link
	 *                WsqConstants#DEFAULT_PPI} is used when {@code <= 0}
	 * @param bitRate target bitrate in bits per pixel (typical fingerprint value
	 *                is {@link #DEFAULT_BIT_RATE}); the default is used when
	 *                {@code <= 0}
	 * @param comment optional extra COM segment; ignored when {@code null} or
	 *                blank
	 * @return WSQ-encoded bytes beginning with SOI ({@code 0xFFA0}) and ending
	 *         with EOI ({@code 0xFFA1})
	 * @throws IllegalArgumentException if {@code gray} is null/empty or shorter
	 *                                  than {@code width * height}, or if width
	 *                                  or height is not positive
	 * @throws IllegalStateException    if quantized block sizes are inconsistent
	 *                                  or a quantization scale overflows
	 */
	public static byte[] encode(byte[] gray, int width, int height, int ppi, float bitRate, String comment) {
		if (bitRate <= 0.0f) {
			bitRate = WsqConstants.DEFAULT_BIT_RATE;
		}
		return encodeInternal(gray, width, height, ppi, bitRate, comment, false);
	}

	/**
	 * Encodes packed 8-bit grayscale pixels to an 8-bit lossless WSQ bitstream.
	 * Every live subband is packed into the Huffman 16-bit extra-bit range so
	 * reconstructed pixels match the source. The container is still WSQ; this
	 * is not a bit-identical wavelet dump.
	 *
	 * @param gray    packed 8-bit pixels
	 * @param width   image width in pixels
	 * @param height  image height in pixels
	 * @param ppi     scan resolution; default 500 when {@code <= 0}
	 * @param comment optional extra COM segment
	 * @return WSQ-encoded bytes
	 */
	public static byte[] encodeLossless(byte[] gray, int width, int height, int ppi, String comment) {
		return encodeInternal(gray, width, height, ppi, 0.0f, comment, true);
	}

	/**
	 * Shared encode pipeline for lossy (FBI bitrate) and 8-bit lossless modes.
	 *
	 * @param gray      packed 8-bit pixels
	 * @param width     image width
	 * @param height    image height
	 * @param ppi       scan resolution
	 * @param bitRate   used when {@code lossless} is false
	 * @param comment   optional COM text
	 * @param lossless  when true, per-subband bins sized for Huffman extras
	 * @return WSQ bytes
	 */
	private static byte[] encodeInternal(byte[] gray, int width, int height, int ppi, float bitRate, String comment,
			boolean lossless) {
		if (gray == null || gray.length == 0 || width <= 0 || height <= 0 || gray.length < width * height) {
			throw new IllegalArgumentException("WSQ encode requires 8-bit grayscale pixels");
		}
		if (ppi <= 0) {
			ppi = WsqConstants.DEFAULT_PPI;
		}

		WsqWavelet.ShiftScale shift = new WsqWavelet.ShiftScale();
		float[] fdata = WsqWavelet.toFloat(gray, width * height, shift);
		WsqTrees.WTree[] wTree = new WsqTrees.WTree[WsqConstants.W_TREELEN];
		WsqTrees.QTree[] qTree = new WsqTrees.QTree[WsqConstants.Q_TREELEN];
		WsqTrees.build(wTree, qTree, width, height);
		WsqWavelet.decompose(fdata, width, height, wTree);

		WsqQuantizer.QuantVals quant = new WsqQuantizer.QuantVals();
		WsqQuantizer.Quantized q;
		if (lossless) {
			q = WsqQuantizer.quantizeLossless(quant, qTree, fdata, width, height);
		} else {
			quant.r = bitRate;
			WsqQuantizer.variance(quant, qTree, fdata, width);
			q = WsqQuantizer.quantize(quant, qTree, fdata, width, height);
		}
		WsqQuantizer.blockSizes(q, quant, wTree, qTree);
		if (q.size != q.qsize1 + q.qsize2 + q.qsize3) {
			throw new IllegalStateException("WSQ quantization block sizes do not match");
		}

		WsqBitOutput out = new WsqBitOutput();
		out.putUShort(WsqConstants.SOI_WSQ);
		writeComment(out, nistComment(width, height, ppi, bitRate, lossless));
		if (comment != null && !comment.isBlank()) {
			writeComment(out, comment);
		}
		writeTransformTable(out);
		writeQuantizationTable(out, quant);
		writeFrameHeader(out, width, height, shift.mShift, shift.rScale);

		WsqHuffman.Table table1 = WsqHuffman.build(q.sip, 0, new int[] { q.qsize1 });
		writeHuffmanTable(out, 0, table1);
		byte[] block1 = WsqHuffman.compress(q.sip, 0, q.qsize1, table1.codes);
		writeBlockHeader(out, 0);
		out.putBytes(block1);

		WsqHuffman.Table table2 = WsqHuffman.build(q.sip, q.qsize1, new int[] { q.qsize2, q.qsize3 });
		writeHuffmanTable(out, 1, table2);
		byte[] block2 = WsqHuffman.compress(q.sip, q.qsize1, q.qsize2, table2.codes);
		writeBlockHeader(out, 1);
		out.putBytes(block2);

		byte[] block3 = WsqHuffman.compress(q.sip, q.qsize1 + q.qsize2, q.qsize3, table2.codes);
		writeBlockHeader(out, 1);
		out.putBytes(block3);

		out.putUShort(WsqConstants.EOI_WSQ);
		return out.toByteArray();
	}

	/**
	 * Builds the required NIST comment listing geometry, PPI, and bitrate.
	 *
	 * @param width    pixel width
	 * @param height   pixel height
	 * @param ppi      scan resolution
	 * @param bitRate  bits per pixel used for quantization (ignored when lossless)
	 * @param lossless {@code LOSSY 0} and bitrate label {@code lossless}
	 * @return ASCII NIST_COM payload
	 */
	private static String nistComment(int width, int height, int ppi, float bitRate, boolean lossless) {
		String rate = lossless ? "lossless" : Float.toString(bitRate);
		int lossy = lossless ? 0 : 1;
		return "NIST_COM 2\nPIX_WIDTH " + width + "\nPIX_HEIGHT " + height + "\nPPI " + ppi + "\nLOSSY " + lossy
				+ "\nWSQ_BITRATE " + rate + "\nIMPLEMENTATION MOSIP-NBIS\n";
	}

	/**
	 * Writes a COM marker and its ASCII payload.
	 *
	 * @param out  bitstream
	 * @param text comment text (US-ASCII)
	 */
	private static void writeComment(WsqBitOutput out, String text) {
		byte[] bytes = text.getBytes(StandardCharsets.US_ASCII);
		out.putUShort(WsqConstants.COM_WSQ);
		out.putUShort(2 + bytes.length);
		out.putBytes(bytes);
	}

	/**
	 * Writes the DTT segment: filter lengths plus the positive half of each FBI
	 * analysis filter in NBIS scaled-integer form. Encoder order is losz then
	 * hisz (jnbis reads hisz then losz — a documented NBIS naming swap).
	 *
	 * @param out bitstream
	 */
	private static void writeTransformTable(WsqBitOutput out) {
		out.putUShort(WsqConstants.DTT_WSQ);
		out.putUShort(58);
		out.putByte(WsqConstants.MAX_LOFILT);
		out.putByte(WsqConstants.MAX_HIFILT);
		writeFilterHalf(out, WsqConstants.LOFILT);
		writeFilterHalf(out, WsqConstants.HIFILT);
	}

	/**
	 * Serializes the right-hand (non-negative index) half of a symmetric filter
	 * as {@code (sign, scale, unsigned 32-bit mantissa)} triples.
	 *
	 * @param out  bitstream
	 * @param filt full odd-length filter taps
	 */
	private static void writeFilterHalf(WsqBitOutput out, float[] filt) {
		for (int coef = filt.length >> 1; coef < filt.length; coef++) {
			float dblTmp = filt[coef];
			int sign = 0;
			if (dblTmp < 0.0f) {
				sign = 1;
				dblTmp *= -1.0f;
			}
			int scaleEx = 0;
			long intDat;
			if (dblTmp == 0.0f) {
				intDat = 0;
			} else {
				double tmp = dblTmp;
				while (tmp < 4294967295.0) {
					scaleEx++;
					tmp *= 10.0;
				}
				scaleEx -= 1;
				intDat = WsqUtil.sroundU32(tmp / 10.0);
			}
			out.putByte(sign);
			out.putByte(scaleEx);
			out.putUInt(intDat);
		}
	}

	/**
	 * Writes the DQT segment: bin widths {@code Q} and zero-bin widths {@code Z}
	 * for 64 slots (60 live subbands plus 4 unused).
	 *
	 * @param out   bitstream
	 * @param quant computed quantization parameters
	 */
	private static void writeQuantizationTable(WsqBitOutput out, WsqQuantizer.QuantVals quant) {
		out.putUShort(WsqConstants.DQT_WSQ);
		out.putUShort(389);
		out.putByte(2);
		out.putUShort(44);
		for (int sub = 0; sub < 64; sub++) {
			int scaleEx = 0;
			int scaleEx2 = 0;
			int shrtDat = 0;
			int shrtDat2 = 0;
			if (sub < 60 && quant.qbss[sub] != 0.0f) {
				int[] q = scaledShort(quant.qbss[sub]);
				int[] z = scaledShort(quant.qzbs[sub]);
				scaleEx = q[0];
				shrtDat = q[1];
				scaleEx2 = z[0];
				shrtDat2 = z[1];
			}
			out.putByte(scaleEx);
			out.putUShort(shrtDat);
			out.putByte(scaleEx2);
			out.putUShort(shrtDat2);
		}
	}

	/**
	 * Encodes a positive float as NBIS {@code (scaleExponent, 16-bit mantissa)}.
	 *
	 * @param value finite value to encode; {@code 0} yields {@code (0, 0)}
	 * @return {@code {scaleEx, mantissa}}
	 * @throws IllegalStateException if {@code value >= 65535}
	 */
	private static int[] scaledShort(float value) {
		int scaleEx = 0;
		float tmp = value;
		if (tmp != 0.0f && tmp < 65535) {
			while (tmp < 65535) {
				scaleEx++;
				tmp *= 10;
			}
			scaleEx -= 1;
			return new int[] { scaleEx, WsqUtil.sround(tmp / 10.0) & 0xffff };
		}
		if (tmp == 0.0f) {
			return new int[] { 0, 0 };
		}
		throw new IllegalStateException("WSQ quantization value too large: " + value);
	}

	/**
	 * Writes the SOF segment: black, white, height, width, mean shift, range
	 * scale, and two reserved bytes.
	 *
	 * @param out    bitstream
	 * @param width  image width
	 * @param height image height
	 * @param mShift pixel-mean shift applied during normalization
	 * @param rScale range scale applied during normalization
	 */
	private static void writeFrameHeader(WsqBitOutput out, int width, int height, float mShift, float rScale) {
		out.putUShort(WsqConstants.SOF_WSQ);
		out.putUShort(17);
		out.putByte(0);
		out.putByte(255);
		out.putUShort(height);
		out.putUShort(width);
		int[] ms = scaledShort(mShift);
		out.putByte(ms[0]);
		out.putUShort(ms[1]);
		int[] rs = scaledShort(rScale);
		out.putByte(rs[0]);
		out.putUShort(rs[1]);
		out.putByte(2);
		out.putUShort(0);
	}

	/**
	 * Writes a DHT segment for Huffman table {@code tableId} (0 = block 1, 1 =
	 * blocks 2 and 3).
	 *
	 * @param out     bitstream
	 * @param tableId table identifier
	 * @param table   bits/values arrays produced by {@link WsqHuffman#build}
	 */
	private static void writeHuffmanTable(WsqBitOutput out, int tableId, WsqHuffman.Table table) {
		int valuesLen = 0;
		for (int i = 0; i < WsqConstants.MAX_HUFFBITS; i++) {
			valuesLen += table.bits[i] & 0xff;
		}
		int tableLen = 3 + WsqConstants.MAX_HUFFBITS + valuesLen;
		out.putUShort(WsqConstants.DHT_WSQ);
		out.putUShort(tableLen);
		out.putByte(tableId);
		for (int i = 0; i < WsqConstants.MAX_HUFFBITS; i++) {
			out.putByte(table.bits[i]);
		}
		for (int i = 0; i < valuesLen; i++) {
			out.putByte(table.values[i]);
		}
	}

	/**
	 * Writes an SOB marker naming the Huffman table that follows.
	 *
	 * @param out   bitstream
	 * @param table Huffman table identifier
	 */
	private static void writeBlockHeader(WsqBitOutput out, int table) {
		out.putUShort(WsqConstants.SOB_WSQ);
		out.putUShort(3);
		out.putByte(table);
	}
}
