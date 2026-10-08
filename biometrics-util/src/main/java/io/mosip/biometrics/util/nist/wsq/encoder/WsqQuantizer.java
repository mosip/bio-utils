package io.mosip.biometrics.util.nist.wsq.encoder;

/**
 * Subband variance, scalar quantization, and Huffman-block sizing from
 * public-domain NIST NBIS {@code util.c} ({@code variance}, {@code quantize},
 * {@code quant_block_sizes}).
 */
final class WsqQuantizer {

	/**
	 * Per-subband quantization state: bitrate, bin widths, zero-bin widths, and
	 * variances.
	 */
	static final class QuantVals {
		/** Target bitrate in bits per pixel ({@code r} in NBIS). */
		float r;
		/** Quantization bin width {@code Q} for each subband (0 = discarded). */
		float[] qbss = new float[WsqConstants.MAX_SUBBANDS];
		/** Zero-bin width {@code Z} (typically {@code 1.2 * Q}). */
		float[] qzbs = new float[WsqConstants.MAX_SUBBANDS];
		/** Estimated subband variance. */
		float[] var = new float[WsqConstants.MAX_SUBBANDS];
	}

	/**
	 * Packed quantized coefficients and the three Huffman-block lengths.
	 */
	static final class Quantized {
		/** Packed signed coefficients in subband scan order. */
		int[] sip;
		/** Number of valid entries in {@link #sip}. */
		int size;
		/** Coefficient count for Huffman block 1 (low-frequency subbands). */
		int qsize1;
		/** Coefficient count for Huffman block 2. */
		int qsize2;
		/** Coefficient count for Huffman block 3 (high-frequency subbands). */
		int qsize3;
	}

	/** Utility holder; not instantiated. */
	private WsqQuantizer() {
	}

	/**
	 * Fills {@link QuantVals#var}. If the sum of the four LL variances (with
	 * skip window) is below 20 000, every subband is measured without skip;
	 * otherwise remaining subbands use the skip window (NBIS {@code variance}).
	 *
	 * @param quant destination
	 * @param qTree quantization tree
	 * @param fip   wavelet coefficients
	 * @param width image width (pitch)
	 */
	static void variance(QuantVals quant, WsqTrees.QTree[] qTree, float[] fip, int width) {
		float vsum = 0.0f;
		for (int cvr = 0; cvr < 4; cvr++) {
			quant.var[cvr] = varWindow(qTree[cvr], fip, width, true);
			vsum += quant.var[cvr];
		}
		if (vsum < 20000.0f) {
			for (int cvr = 0; cvr < WsqConstants.NUM_SUBBANDS; cvr++) {
				quant.var[cvr] = varWindow(qTree[cvr], fip, width, false);
			}
		} else {
			for (int cvr = 4; cvr < WsqConstants.NUM_SUBBANDS; cvr++) {
				quant.var[cvr] = varWindow(qTree[cvr], fip, width, true);
			}
		}
	}

	/**
	 * Sample variance of one Q-tree rectangle, optionally shrinking the window
	 * (skip x/8 and 9y/32) as NBIS does for high-energy images.
	 *
	 * @param t     subband rectangle
	 * @param fip   wavelet image
	 * @param width pitch
	 * @param skip  when true, use the reduced window
	 * @return unbiased sample variance, or 0 when the window has fewer than two
	 *         samples
	 */
	private static float varWindow(WsqTrees.QTree t, float[] fip, int width, boolean skip) {
		int lenx = t.lenx;
		int leny = t.leny;
		int skipx = 0;
		int skipy = 0;
		if (skip) {
			skipx = t.lenx / 8;
			skipy = (9 * t.leny) / 32;
			lenx = (3 * t.lenx) / 4;
			leny = (7 * t.leny) / 16;
		}
		if (lenx <= 0 || leny <= 0 || (lenx * leny) <= 1) {
			return 0.0f;
		}
		int fp = (t.y * width) + t.x + (skipy * width) + skipx;
		float ssq = 0.0f;
		float sumPix = 0.0f;
		for (int row = 0; row < leny; row++, fp += (width - lenx)) {
			for (int col = 0; col < lenx; col++) {
				float v = fip[fp];
				sumPix += v;
				ssq += v * v;
				fp++;
			}
		}
		float n = (float) (lenx * leny);
		return (ssq - ((sumPix * sumPix) / n)) / (n - 1.0f);
	}

	/**
	 * Computes {@code Q}/{@code Z} from variances and bitrate, then packs
	 * quantized coefficients. Subbands below {@link WsqConstants#VARIANCE_THRESH}
	 * are dropped. Non-finite {@code q} (overflow at very high bitrate) is
	 * replaced with 1 so the image is not flattened.
	 *
	 * @param quant filled variances and bitrate; {@code qbss}/{@code qzbs} are
	 *              overwritten
	 * @param qTree subband geometry
	 * @param fip   wavelet coefficients
	 * @param width image width
	 * @param height image height (used only to size {@code sip})
	 * @return packed coefficients (block sizes filled later by
	 *         {@link #blockSizes})
	 */
	static Quantized quantize(QuantVals quant, WsqTrees.QTree[] qTree, float[] fip, int width, int height) {
		float[] a = new float[WsqConstants.NUM_SUBBANDS];
		for (int cnt = 0; cnt < WsqConstants.STRT_SUBBAND_3; cnt++) {
			a[cnt] = 1.0f;
		}
		a[52] = 1.32f;
		a[53] = 1.08f;
		a[54] = 1.42f;
		a[55] = 1.08f;
		a[56] = 1.32f;
		a[57] = 1.42f;
		a[58] = 1.08f;
		a[59] = 1.08f;

		for (int cnt = 0; cnt < WsqConstants.NUM_SUBBANDS; cnt++) {
			if (quant.var[cnt] < WsqConstants.VARIANCE_THRESH) {
				quant.qbss[cnt] = 0.0f;
			} else if (cnt < WsqConstants.STRT_SIZE_REGION_2) {
				quant.qbss[cnt] = 1.0f;
			} else {
				quant.qbss[cnt] = 10.0f / (a[cnt] * (float) Math.log(quant.var[cnt]));
			}
		}

		float m1 = 1.0f / 1024.0f;
		float m2 = 1.0f / 256.0f;
		float m3 = 1.0f / 16.0f;
		float[] m = new float[WsqConstants.NUM_SUBBANDS];
		for (int cnt = 0; cnt < WsqConstants.STRT_SIZE_REGION_2; cnt++) {
			m[cnt] = m1;
		}
		for (int cnt = WsqConstants.STRT_SIZE_REGION_2; cnt < WsqConstants.STRT_SIZE_REGION_3; cnt++) {
			m[cnt] = m2;
		}
		for (int cnt = WsqConstants.STRT_SIZE_REGION_3; cnt < WsqConstants.NUM_SUBBANDS; cnt++) {
			m[cnt] = m3;
		}

		int[] k0 = new int[WsqConstants.NUM_SUBBANDS];
		int[] k1 = new int[WsqConstants.NUM_SUBBANDS];
		float[] sigma = new float[WsqConstants.NUM_SUBBANDS];
		int k0len = 0;
		for (int cnt = 0; cnt < WsqConstants.NUM_SUBBANDS; cnt++) {
			if (quant.var[cnt] >= WsqConstants.VARIANCE_THRESH) {
				k0[k0len] = cnt;
				k1[k0len++] = cnt;
				sigma[cnt] = (float) Math.sqrt(quant.var[cnt]);
			}
		}
		int[] k = k1;
		int klen = k0len;

		while (true) {
			double s = 0.0;
			for (int i = 0; i < klen; i++) {
				s += m[k[i]];
			}
			if (s == 0.0) {
				break;
			}
			double p = 1.0;
			for (int i = 0; i < klen; i++) {
				p *= Math.pow(sigma[k[i]] / quant.qbss[k[i]], m[k[i]]);
			}
			float q = (float) proportionality(quant.r, s, p);
			boolean[] np = new boolean[WsqConstants.NUM_SUBBANDS];
			int nplen = 0;
			for (int i = 0; i < klen; i++) {
				if ((quant.qbss[k[i]] / q) >= (5.0f * sigma[k[i]])) {
					np[k[i]] = true;
					nplen++;
				}
			}
			if (nplen == 0) {
				break;
			}
			int nKlen = 0;
			int[] nK = k1;
			for (int i = 0; i < klen; i++) {
				if (!np[k[i]]) {
					nK[nKlen++] = k[i];
				}
			}
			k = nK;
			klen = nKlen;
		}

		boolean[] inK0 = new boolean[WsqConstants.NUM_SUBBANDS];
		for (int i = 0; i < k0len; i++) {
			inK0[k0[i]] = true;
		}
		reapplyQ(quant, inK0, sigma, m, k, klen);
		return packSip(quant, qTree, fip, width, height);
	}

	/**
	 * Packs every non-zero subband with a bin width that fills the Huffman
	 * 16-bit extra-bit range. Zero-bin width equals {@code Q} (no extra dead
	 * zone). WSQ is still a quantized wavelet codec; this is 8-bit lossless
	 * round-trip, not a bit-identical wavelet stream.
	 *
	 * @param quant destination bin widths
	 * @param qTree subband geometry
	 * @param fip   wavelet coefficients
	 * @param width image width
	 * @param height image height
	 * @return packed coefficients
	 */
	static Quantized quantizeLossless(QuantVals quant, WsqTrees.QTree[] qTree, float[] fip, int width, int height) {
		for (int cnt = 0; cnt < WsqConstants.NUM_SUBBANDS; cnt++) {
			float maxAbs = 0.0f;
			int fptr = (qTree[cnt].y * width) + qTree[cnt].x;
			for (int row = 0; row < qTree[cnt].leny; row++, fptr += width - qTree[cnt].lenx) {
				for (int col = 0; col < qTree[cnt].lenx; col++) {
					float a = Math.abs(fip[fptr]);
					if (a > maxAbs) {
						maxAbs = a;
					}
					fptr++;
				}
			}
			if (maxAbs == 0.0f) {
				quant.qbss[cnt] = 0.0f;
				quant.qzbs[cnt] = 0.0f;
			} else {
				float q = maxAbs / WsqConstants.LOSSLESS_SIP_RANGE;
				quant.qbss[cnt] = q;
				quant.qzbs[cnt] = q;
			}
		}
		return packSip(quant, qTree, fip, width, height);
	}

	/**
	 * Walks live Q-tree rectangles and packs scalar-quantized coefficients.
	 * Magnitudes are clamped to 16-bit Huffman extra-bit range.
	 *
	 * @param quant  destination bin widths (zero skips a subband)
	 * @param qTree  subband geometry
	 * @param fip    wavelet coefficients
	 * @param width  pitch
	 * @param height used to size {@code sip}
	 * @return packed coefficients
	 */
	private static Quantized packSip(QuantVals quant, WsqTrees.QTree[] qTree, float[] fip, int width, int height) {
		int[] sip = new int[width * height];
		int sptr = 0;
		for (int cnt = 0; cnt < WsqConstants.NUM_SUBBANDS; cnt++) {
			if (quant.qbss[cnt] == 0.0f) {
				continue;
			}
			int fptr = (qTree[cnt].y * width) + qTree[cnt].x;
			float zbin = quant.qzbs[cnt] / 2.0f;
			for (int row = 0; row < qTree[cnt].leny; row++, fptr += width - qTree[cnt].lenx) {
				for (int col = 0; col < qTree[cnt].lenx; col++) {
					float v = fip[fptr];
					int s;
					if (-zbin <= v && v <= zbin) {
						s = 0;
					} else if (v > 0.0f) {
						s = (int) (((v - zbin) / quant.qbss[cnt]) + 1.0f);
					} else {
						s = (int) (((v + zbin) / quant.qbss[cnt]) - 1.0f);
					}
					if (s > 65535) {
						s = 65535;
					} else if (s < -65535) {
						s = -65535;
					}
					sip[sptr] = s;
					sptr++;
					fptr++;
				}
			}
		}
		Quantized out = new Quantized();
		out.sip = sip;
		out.size = sptr;
		return out;
	}

	/**
	 * NBIS applies {@code q} inside the while-loop, then divides {@code qbss} by
	 * that last {@code q}. Recalculate the same last {@code q} here. Non-finite
	 * or zero {@code q} is replaced with 1.
	 *
	 * @param quant destination bin widths
	 * @param inK0  subbands that originally passed the variance threshold
	 * @param sigma per-subband standard deviations
	 * @param m     subband weights
	 * @param k     surviving subband indices after the discard loop
	 * @param klen  length of {@code k}
	 */
	private static void reapplyQ(QuantVals quant, boolean[] inK0, float[] sigma, float[] m, int[] k, int klen) {
		double s = sumM(m, k, klen);
		if (s == 0.0) {
			for (int cnt = 0; cnt < WsqConstants.NUM_SUBBANDS; cnt++) {
				if (!inK0[cnt]) {
					quant.qbss[cnt] = 0.0f;
				}
				quant.qzbs[cnt] = 1.2f * quant.qbss[cnt];
			}
			return;
		}
		double p = productP(sigma, quant.qbss, m, k, klen);
		float q = (float) proportionality(quant.r, s, p);
		for (int cnt = 0; cnt < WsqConstants.NUM_SUBBANDS; cnt++) {
			if (inK0[cnt]) {
				quant.qbss[cnt] /= q;
			} else {
				quant.qbss[cnt] = 0.0f;
			}
			quant.qzbs[cnt] = 1.2f * quant.qbss[cnt];
		}
	}

	/**
	 * FBI proportionality constant {@code q}. The exponent is clamped so a high
	 * lossless bitrate cannot overflow {@code double} and flatten the image.
	 *
	 * @param r target bitrate
	 * @param s weight sum
	 * @param p product term
	 * @return finite {@code q}, or 1 when the set is empty
	 */
	private static double proportionality(double r, double s, double p) {
		if (s == 0.0) {
			return 1.0;
		}
		double exp = (r / s) - 1.0;
		if (exp > 20.0) {
			exp = 20.0;
		} else if (exp < -20.0) {
			exp = -20.0;
		}
		double qVal = (Math.pow(2.0, exp) / 2.5) / Math.pow(p, 1.0 / s);
		if (!Double.isFinite(qVal) || qVal == 0.0) {
			return 1.0;
		}
		return qVal;
	}

	/**
	 * Sums {@code m} weights for the surviving subband set.
	 *
	 * @param m    weights
	 * @param k    subband indices
	 * @param klen number of indices
	 * @return sum of weights
	 */
	private static double sumM(float[] m, int[] k, int klen) {
		double s = 0.0;
		for (int i = 0; i < klen; i++) {
			s += m[k[i]];
		}
		return s;
	}

	/**
	 * Product term {@code Π (σ / Q)^m} used to compute {@code q}.
	 *
	 * @param sigma standard deviations
	 * @param qbss  current bin widths
	 * @param m     weights
	 * @param k     subband indices
	 * @param klen  number of indices
	 * @return product
	 */
	private static double productP(float[] sigma, float[] qbss, float[] m, int[] k, int klen) {
		double p = 1.0;
		for (int i = 0; i < klen; i++) {
			p *= Math.pow(sigma[k[i]] / qbss[k[i]], m[k[i]]);
		}
		return p;
	}

	/**
	 * Computes Huffman block lengths by subtracting discarded subband areas from
	 * the three W-tree regions (NBIS {@code quant_block_sizes}).
	 *
	 * @param q     destination {@code qsize1/2/3}
	 * @param quant bin widths (zero means discarded)
	 * @param wTree wavelet tree
	 * @param qTree quantization tree
	 */
	static void blockSizes(Quantized q, QuantVals quant, WsqTrees.WTree[] wTree, WsqTrees.QTree[] qTree) {
		int qsize1 = wTree[14].lenx * wTree[14].leny;
		int qsize2 = (wTree[5].leny * wTree[1].lenx) + (wTree[4].lenx * wTree[4].leny);
		int qsize3 = (wTree[2].lenx * wTree[2].leny) + (wTree[3].lenx * wTree[3].leny);
		for (int node = 0; node < WsqConstants.STRT_SUBBAND_2; node++) {
			if (quant.qbss[node] == 0.0f) {
				qsize1 -= (qTree[node].lenx * qTree[node].leny);
			}
		}
		for (int node = WsqConstants.STRT_SUBBAND_2; node < WsqConstants.STRT_SUBBAND_3; node++) {
			if (quant.qbss[node] == 0.0f) {
				qsize2 -= (qTree[node].lenx * qTree[node].leny);
			}
		}
		for (int node = WsqConstants.STRT_SUBBAND_3; node < WsqConstants.STRT_SUBBAND_DEL; node++) {
			if (quant.qbss[node] == 0.0f) {
				qsize3 -= (qTree[node].lenx * qTree[node].leny);
			}
		}
		q.qsize1 = qsize1;
		q.qsize2 = qsize2;
		q.qsize3 = qsize3;
	}
}
