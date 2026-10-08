package io.mosip.biometrics.util.nist.wsq.encoder;

import io.mosip.biometrics.util.CommonUtil;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Round-trip and branch-coverage tests for the NIST NBIS WSQ encoder port.
 */
class WsqEncoderTest {

	@Test
	void encodeFingerprintLikeImageIsDecodableByJnbis() {
		byte[] gray = ridge(256, 256);
		byte[] wsq = WsqEncoder.encode(gray, 256, 256, 500, WsqEncoder.DEFAULT_BIT_RATE, null);
		assertEquals((byte) 0xff, wsq[0]);
		assertEquals((byte) 0xa0, wsq[1]);
		BufferedImage decoded = CommonUtil.convertWSQToBufferedImage(wsq);
		assertEquals(256, decoded.getWidth());
		assertEquals(256, decoded.getHeight());
		int contrast = contrast(decoded);
		double mae = mae(gray, decoded, 256, 256);
		assertTrue(contrast > 30, "decoded contrast too low: " + contrast);
		assertTrue(mae < 45.0, "lossy WSQ round-trip MAE too high: " + mae);
		byte[] lossless = WsqEncoder.encodeLossless(gray, 256, 256, 500, "lossless-com");
		double losslessMae = mae(gray, CommonUtil.convertWSQToBufferedImage(lossless), 256, 256);
		assertTrue(losslessMae < mae, "lossless MAE " + losslessMae + " should beat lossy MAE " + mae);
		assertTrue(new String(lossless, java.nio.charset.StandardCharsets.ISO_8859_1).contains("LOSSY 0"));
		int rampN = 128;
		byte[] ramp = new byte[rampN * rampN];
		for (int i = 0; i < ramp.length; i++) {
			ramp[i] = (byte) ((i * 255) / (ramp.length - 1));
		}
		double rampMae = mae(ramp,
				CommonUtil.convertWSQToBufferedImage(WsqEncoder.encodeLossless(ramp, rampN, rampN, 0, null)), rampN,
				rampN);
		assertTrue(rampMae < 3.0, "lossless ramp MAE too high: " + rampMae);
		byte[] def = WsqEncoder.encode(gray, 256, 256, 500, null);
		assertEquals((byte) 0xff, def[0]);
	}

	@Test
	void encodeOddSizesAndDefaultsAreDecodable() {
		for (int[] size : new int[][] { { 255, 241 }, { 128, 97 }, { 64, 64 }, { 33, 48 } }) {
			byte[] gray = ridge(size[0], size[1]);
			byte[] wsq = WsqEncoder.encode(gray, size[0], size[1], 0, 0.0f, "  ");
			BufferedImage decoded = CommonUtil.convertWSQToBufferedImage(wsq);
			assertEquals(size[0], decoded.getWidth());
			assertEquals(size[1], decoded.getHeight());
		}
	}

	@Test
	void encodeCommentAndHighBitrateRemainDecodable() {
		byte[] gray = ridge(128, 128);
		byte[] wsq = WsqEncoder.encode(gray, 128, 128, 250, 1.2f, "mosip-comment");
		BufferedImage decoded = CommonUtil.convertWSQToBufferedImage(wsq);
		assertEquals(128, decoded.getWidth());
		assertEquals(128, decoded.getHeight());
	}

	@Test
	void encodeFlatAndNoisyImages() {
		byte[] flat = new byte[64 * 64];
		java.util.Arrays.fill(flat, (byte) 128);
		BufferedImage flatDecoded = CommonUtil.convertWSQToBufferedImage(WsqEncoder.encode(flat, 64, 64, 500, 0.75f, null));
		assertEquals(64, flatDecoded.getWidth());

		byte[] noise = new byte[256 * 256];
		Random rnd = new Random(7);
		rnd.nextBytes(noise);
		BufferedImage noisy = CommonUtil.convertWSQToBufferedImage(WsqEncoder.encode(noise, 256, 256, 500, 0.75f, null));
		assertEquals(256, noisy.getWidth());
		assertTrue(contrast(noisy) > 10);
	}

	@Test
	void encodeRejectsInvalidInput() {
		assertThrows(IllegalArgumentException.class, () -> WsqEncoder.encode(null, 8, 8, 500, 0.75f, null));
		assertThrows(IllegalArgumentException.class, () -> WsqEncoder.encode(new byte[0], 8, 8, 500, 0.75f, null));
		assertThrows(IllegalArgumentException.class, () -> WsqEncoder.encode(new byte[4], 8, 8, 500, 0.75f, null));
		assertThrows(IllegalArgumentException.class, () -> WsqEncoder.encode(new byte[16], 0, 4, 500, 0.75f, null));
		assertThrows(IllegalArgumentException.class, () -> WsqEncoder.encode(new byte[16], 4, -1, 500, 0.75f, null));
		assertThrows(IllegalArgumentException.class, () -> WsqEncoder.encodeLossless(null, 8, 8, 500, null));
	}

	@Test
	void constantsAndPrivateConstructors() throws Exception {
		assertEquals(1, WsqUtil.sround(0.6));
		assertEquals(0, WsqUtil.sround(0.4));
		assertEquals(-1, WsqUtil.sround(-0.6));
		assertEquals(0, WsqUtil.sround(-0.4));
		assertEquals(3782845551L, WsqUtil.sroundU32(3782845550.6995));
		assertTrue(WsqUtil.sroundU32(3782845550.6995) > Integer.MAX_VALUE);
		assertEquals(0xffffffffL, WsqUtil.sroundU32(-0.6));
		newInstance(WsqConstants.class);
		newInstance(WsqEncoder.class);
		newInstance(WsqHuffman.class);
		newInstance(WsqQuantizer.class);
		newInstance(WsqTrees.class);
		newInstance(WsqUtil.class);
		newInstance(WsqWavelet.class);
	}

	@Test
	void bitOutputStuffingAndFlush() {
		WsqBitOutput out = new WsqBitOutput();
		out.putUShort(0xffa0);
		out.putByte(0x7f);
		out.putUInt(0x01020304L);
		out.putBytes(new byte[] { 9, 8 });
		out.writeBits(0xff, 8);
		out.writeBits(0x01, 3);
		out.flushBits();
		byte[] bytes = out.toByteArray();
		assertEquals((byte) 0xff, bytes[0]);
		assertEquals((byte) 0xa0, bytes[1]);
		assertTrue(indexOf(bytes, (byte) 0xff) >= 0);
		out.flushBits();
		assertArrayEquals(bytes, out.toByteArray());
	}

	@Test
	void waveletEvenFiltersAndTinySignals() {
		WsqWavelet.ShiftScale shift = new WsqWavelet.ShiftScale();
		byte[] gray = { 10, 20, 30, 40 };
		float[] f = WsqWavelet.toFloat(gray, 4, shift);
		assertEquals(4, f.length);
		assertTrue(shift.rScale > 0);

		byte[] flat = { 7, 7, 7, 7 };
		WsqWavelet.toFloat(flat, 4, shift);
		assertEquals(1.0f, shift.rScale);

		float[] src = { 1, 2, 3, 4, 5, 6, 7, 8 };
		float[] dest = new float[8];
		float[] evenLo = { 0.5f, 0.5f };
		float[] evenHi = { 0.5f, -0.5f };
		WsqWavelet.getLets(dest, 0, src, 0, 1, 8, 8, 1, evenHi, evenLo, false);
		WsqWavelet.getLets(dest, 0, src, 0, 1, 7, 8, 1, evenHi, evenLo, true);
		float[] fourLo = { 0.25f, 0.25f, 0.25f, 0.25f };
		float[] fourHi = { 0.25f, 0.25f, -0.25f, -0.25f };
		WsqWavelet.getLets(dest, 0, src, 0, 1, 8, 8, 1, fourHi, fourLo, false);
	}

	@Test
	void treesCoverOddAndEvenSplits() {
		WsqTrees.WTree[] wEven = new WsqTrees.WTree[WsqConstants.W_TREELEN];
		WsqTrees.QTree[] qEven = new WsqTrees.QTree[WsqConstants.Q_TREELEN];
		WsqTrees.build(wEven, qEven, 256, 256);
		assertEquals(256, wEven[0].lenx);
		assertTrue(qEven[0].lenx > 0);

		WsqTrees.WTree[] wOdd = new WsqTrees.WTree[WsqConstants.W_TREELEN];
		WsqTrees.QTree[] qOdd = new WsqTrees.QTree[WsqConstants.Q_TREELEN];
		WsqTrees.build(wOdd, qOdd, 255, 241);
		assertEquals(255, wOdd[0].lenx);
		assertEquals(241, wOdd[0].leny);
	}

	@Test
	void huffmanLargeCoeffsLongRunsAndEmptyBlock() {
		int[] sip = new int[400];
		sip[0] = 80;
		sip[1] = 300;
		sip[2] = -80;
		sip[3] = -300;
		for (int i = 4; i < 160; i++) {
			sip[i] = 0;
		}
		sip[160] = 3;
		for (int i = 161; i < 400; i++) {
			sip[i] = 0;
		}
		WsqHuffman.Table table = WsqHuffman.build(sip, 0, new int[] { 160, 240 });
		assertNotNull(table.codes);
		byte[] packed = WsqHuffman.compress(sip, 0, sip.length, table.codes);
		assertTrue(packed.length > 0);

		int[] tiny = { 1, 2, 3 };
		WsqHuffman.Table t2 = WsqHuffman.build(tiny, 0, new int[] { 3 });
		assertTrue(WsqHuffman.compress(tiny, 0, 3, t2.codes).length > 0);
	}

	@Test
	void huffmanEmptyBlockAndLongZeroRun() throws Exception {
		int[] empty = new int[0];
		WsqHuffman.Table emptyTable = WsqHuffman.build(empty, 0, new int[] { 0 });
		assertNotNull(emptyTable.codes);
		assertEquals(0, WsqHuffman.compress(empty, 0, 0, emptyTable.codes).length);

		int[] zeros = new int[320];
		zeros[0] = 1;
		zeros[319] = 2;
		WsqHuffman.Table table = WsqHuffman.build(zeros, 0, new int[] { 320 });
		assertTrue(WsqHuffman.compress(zeros, 0, 320, table.codes).length > 0);

		WsqHuffman.HuffCode[] ones = new WsqHuffman.HuffCode[2];
		ones[0] = new WsqHuffman.HuffCode();
		ones[0].size = 3;
		ones[0].code = 0b111;
		ones[1] = new WsqHuffman.HuffCode();
		java.lang.reflect.Method hasAll = WsqHuffman.class.getDeclaredMethod("hasAllOnes",
				WsqHuffman.HuffCode[].class, int.class);
		hasAll.setAccessible(true);
		assertEquals(Boolean.TRUE, hasAll.invoke(null, ones, 1));
		assertEquals(Boolean.FALSE, hasAll.invoke(null, ones, 0));
	}

	@Test
	void huffmanAnnexKLengthAdjustAndRunWrap() throws Exception {
		int[] codesize = new int[WsqConstants.MAX_HUFFCOUNTS_WSQ + 1];
		for (int i = 0; i < 8; i++) {
			codesize[i] = 1;
		}
		for (int i = 8; i < 40; i++) {
			codesize[i] = 17;
		}
		int[] adjust = new int[1];
		java.lang.reflect.Method findNum = WsqHuffman.class.getDeclaredMethod("findNumHuffSizes", int[].class,
				int[].class);
		findNum.setAccessible(true);
		byte[] bits = (byte[]) findNum.invoke(null, codesize, adjust);
		assertEquals(1, adjust[0]);
		java.lang.reflect.Method sort = WsqHuffman.class.getDeclaredMethod("sortHuffbits", byte[].class);
		sort.setAccessible(true);
		sort.invoke(null, bits);

		int[] wrap = new int[65537];
		wrap[65536] = 1;
		WsqHuffman.Table table = WsqHuffman.build(wrap, 0, new int[] { wrap.length });
		assertTrue(WsqHuffman.compress(wrap, 0, wrap.length, table.codes).length > 0);

		java.lang.reflect.Method writeRun = WsqHuffman.class.getDeclaredMethod("writeRun", WsqBitOutput.class,
				WsqHuffman.HuffCode[].class, int.class, int.class);
		writeRun.setAccessible(true);
		WsqHuffman.HuffCode[] codes = table.codes;
		try {
			writeRun.invoke(null, new WsqBitOutput(), codes, 0x10000, WsqConstants.MAX_HUFFZRUN);
		} catch (java.lang.reflect.InvocationTargetException e) {
			assertTrue(e.getCause() instanceof IllegalStateException);
		}
		java.lang.reflect.Method bumpRun = WsqHuffman.class.getDeclaredMethod("bumpRun", int[].class, int.class,
				int.class);
		bumpRun.setAccessible(true);
		try {
			bumpRun.invoke(null, new int[WsqConstants.MAX_HUFFCOUNTS_WSQ + 1], 0x10000, WsqConstants.MAX_HUFFZRUN);
		} catch (java.lang.reflect.InvocationTargetException e) {
			assertTrue(e.getCause() instanceof IllegalStateException);
		}
	}

	@Test
	void huffmanManySymbolsTriggersLengthAdjust() {
		int[] sip = new int[512];
		int n = 0;
		for (int v = -74; v <= 74; v++) {
			if (v == 0) {
				continue;
			}
			sip[n++] = (short) v;
		}
		sip[n++] = 80;
		sip[n++] = 256;
		sip[n++] = -80;
		sip[n++] = -256;
		for (int run = 1; run <= 20; run++) {
			for (int z = 0; z < run; z++) {
				sip[n++] = 0;
			}
			sip[n++] = 1;
		}
		WsqHuffman.Table table = WsqHuffman.build(sip, 0, new int[] { n });
		byte[] packed = WsqHuffman.compress(sip, 0, n, table.codes);
		assertTrue(packed.length > 0);
	}

	@Test
	void scaledShortAndFilterZeroTapViaReflection() throws Exception {
		Method scaled = WsqEncoder.class.getDeclaredMethod("scaledShort", float.class);
		scaled.setAccessible(true);
		int[] zero = (int[]) scaled.invoke(null, 0.0f);
		assertArrayEquals(new int[] { 0, 0 }, zero);
		int[] ok = (int[]) scaled.invoke(null, 1.25f);
		assertEquals(2, ok.length);
		assertThrows(Exception.class, () -> {
			try {
				scaled.invoke(null, 70000.0f);
			} catch (java.lang.reflect.InvocationTargetException e) {
				throw (Exception) e.getCause();
			}
		});

		WsqBitOutput out = new WsqBitOutput();
		Method half = WsqEncoder.class.getDeclaredMethod("writeFilterHalf", WsqBitOutput.class, float[].class);
		half.setAccessible(true);
		half.invoke(null, out, new float[] { 0.0f, 0.0f, 0.0f });
		assertTrue(out.toByteArray().length > 0);

		WsqBitOutput loOut = new WsqBitOutput();
		half.invoke(null, loOut, WsqConstants.LOFILT);
		byte[] loBytes = loOut.toByteArray();
		int tapCount = (WsqConstants.LOFILT.length + 1) / 2;
		int last = (tapCount - 1) * 6;
		int scale = loBytes[last + 1] & 0xff;
		long mantissa = ((loBytes[last + 2] & 0xffL) << 24) | ((loBytes[last + 3] & 0xffL) << 16)
				| ((loBytes[last + 4] & 0xffL) << 8) | (loBytes[last + 5] & 0xffL);
		assertTrue(mantissa > Integer.MAX_VALUE, "small LOFILT tap must not saturate signed int");
		double recovered = mantissa;
		for (int i = 0; i < scale; i++) {
			recovered /= 10.0;
		}
		assertEquals(WsqConstants.LOFILT[WsqConstants.LOFILT.length - 1], recovered, 1e-9);
	}

	@Test
	void encodedDttFilterMantissasDoNotSaturate() {
		byte[] gray = ridge(64, 64);
		byte[] wsq = WsqEncoder.encode(gray, 64, 64, 500, WsqEncoder.DEFAULT_BIT_RATE, null);
		int dtt = indexOfMarker(wsq, WsqConstants.DTT_WSQ);
		assertTrue(dtt >= 0, "DTT marker missing");
		int p = dtt + 6;
		int saturated = 0;
		for (int t = 0; t < 9; t++) {
			long man = ((wsq[p + 2] & 0xffL) << 24) | ((wsq[p + 3] & 0xffL) << 16)
					| ((wsq[p + 4] & 0xffL) << 8) | (wsq[p + 5] & 0xffL);
			if (man == 0x7fffffffL) {
				saturated++;
			}
			p += 6;
		}
		assertEquals(0, saturated, "DTT filter mantissa saturated at Integer.MAX_VALUE");
	}

	@Test
	void quantizerVarianceSkipWindowOnHighEnergy() {
		int width = 64;
		int height = 64;
		WsqTrees.WTree[] wTree = new WsqTrees.WTree[WsqConstants.W_TREELEN];
		WsqTrees.QTree[] qTree = new WsqTrees.QTree[WsqConstants.Q_TREELEN];
		WsqTrees.build(wTree, qTree, width, height);
		float[] fip = new float[width * height];
		Random rnd = new Random(3);
		for (int i = 0; i < fip.length; i++) {
			fip[i] = (rnd.nextFloat() - 0.5f) * 4000f;
		}
		WsqQuantizer.QuantVals quant = new WsqQuantizer.QuantVals();
		quant.r = 0.75f;
		WsqQuantizer.variance(quant, qTree, fip, width);
		WsqQuantizer.Quantized q = WsqQuantizer.quantize(quant, qTree, fip, width, height);
		WsqQuantizer.blockSizes(q, quant, wTree, qTree);
		assertEquals(q.size, q.qsize1 + q.qsize2 + q.qsize3);
		java.util.Arrays.fill(fip, 20000.0f);
		WsqQuantizer.QuantVals losslessQ = new WsqQuantizer.QuantVals();
		WsqQuantizer.Quantized packed = WsqQuantizer.quantizeLossless(losslessQ, qTree, fip, width, height);
		WsqQuantizer.blockSizes(packed, losslessQ, wTree, qTree);
		assertEquals(packed.size, packed.qsize1 + packed.qsize2 + packed.qsize3);
		assertTrue(packed.size > 0);
		java.util.Arrays.fill(fip, -20000.0f);
		WsqQuantizer.Quantized packedNeg = WsqQuantizer.quantizeLossless(new WsqQuantizer.QuantVals(), qTree, fip,
				width, height);
		assertTrue(packedNeg.size > 0);
	}

	private static void newInstance(Class<?> type) throws Exception {
		Constructor<?> ctor = type.getDeclaredConstructor();
		ctor.setAccessible(true);
		assertNotNull(ctor.newInstance());
	}

	private static byte[] ridge(int width, int height) {
		byte[] gray = new byte[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int v = (int) (128 + 80 * Math.sin((x + y) / 6.0) + 20 * Math.sin(x / 3.5));
				gray[y * width + x] = (byte) Math.max(0, Math.min(255, v));
			}
		}
		return gray;
	}

	private static int contrast(BufferedImage image) {
		int min = 255;
		int max = 0;
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int g = image.getRGB(x, y) & 0xff;
				min = Math.min(min, g);
				max = Math.max(max, g);
			}
		}
		return max - min;
	}

	private static double mae(byte[] src, BufferedImage decoded, int width, int height) {
		byte[] dec = new byte[width * height];
		decoded.getRaster().getDataElements(0, 0, width, height, dec);
		long sum = 0;
		for (int i = 0; i < src.length; i++) {
			sum += Math.abs((src[i] & 0xff) - (dec[i] & 0xff));
		}
		return sum / (double) src.length;
	}

	private static int indexOf(byte[] data, byte value) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == value) {
				return i;
			}
		}
		return -1;
	}

	private static int indexOfMarker(byte[] data, int marker) {
		int hi = (marker >> 8) & 0xff;
		int lo = marker & 0xff;
		for (int i = 0; i < data.length - 1; i++) {
			if ((data[i] & 0xff) == hi && (data[i + 1] & 0xff) == lo) {
				return i;
			}
		}
		return -1;
	}
}
