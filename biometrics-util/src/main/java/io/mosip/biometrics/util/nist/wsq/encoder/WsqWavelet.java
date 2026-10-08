package io.mosip.biometrics.util.nist.wsq.encoder;

/**
 * Pixel normalization and 2-D wavelet analysis from public-domain NIST NBIS
 * {@code util.c} ({@code conv_img_2_flt_ret}, {@code wsq_decompose},
 * {@code get_lets}).
 */
final class WsqWavelet {

	/**
	 * Mean shift and range scale applied when converting 8-bit pixels to the
	 * floating-point domain expected by the analysis filters.
	 */
	static final class ShiftScale {
		/** Pixel mean written into the SOF header as {@code m_shift}. */
		float mShift;
		/** Range scale written into the SOF header as {@code r_scale}. */
		float rScale;
	}

	/** Utility holder; not instantiated. */
	private WsqWavelet() {
	}

	/**
	 * Converts packed 8-bit pixels to zero-mean floats:
	 * {@code (pixel - mean) / rScale}. {@code rScale} is the larger of
	 * {@code (mean - min)} and {@code (max - mean)} divided by 128, or 1 when
	 * the image is flat.
	 *
	 * @param gray   source pixels
	 * @param numPix number of pixels to convert (normally {@code width * height})
	 * @param out    filled with {@code mShift} and {@code rScale}
	 * @return newly allocated float image of length {@code numPix}
	 */
	static float[] toFloat(byte[] gray, int numPix, ShiftScale out) {
		long sum = 0;
		int low = 255;
		int high = 0;
		for (int i = 0; i < numPix; i++) {
			int v = gray[i] & 0xff;
			if (v > high) {
				high = v;
			}
			if (v < low) {
				low = v;
			}
			sum += v;
		}
		float mean = (float) sum / (float) numPix;
		out.mShift = mean;
		float lowDiff = mean - low;
		float highDiff = high - mean;
		float rScale = (lowDiff >= highDiff ? lowDiff : highDiff) / 128.0f;
		if (rScale == 0.0f) {
			rScale = 1.0f;
		}
		out.rScale = rScale;
		float[] fip = new float[numPix];
		for (int i = 0; i < numPix; i++) {
			fip[i] = ((gray[i] & 0xff) - mean) / rScale;
		}
		return fip;
	}

	/**
	 * Applies the 5-level FBI analysis filter bank in place, walking every
	 * W-tree node (row then column).
	 *
	 * @param fdata  image buffer of length {@code width * height}; replaced with
	 *               wavelet coefficients
	 * @param width  image width (pitch of {@code fdata})
	 * @param height image height
	 * @param wTree  wavelet tree from {@link WsqTrees#build}
	 */
	static void decompose(float[] fdata, int width, int height, WsqTrees.WTree[] wTree) {
		float[] tmp = new float[width * height];
		for (WsqTrees.WTree node : wTree) {
			int base = (node.y * width) + node.x;
			getLets(tmp, 0, fdata, base, node.leny, node.lenx, width, 1, WsqConstants.HIFILT, WsqConstants.LOFILT,
					node.invRw != 0);
			getLets(fdata, base, tmp, 0, node.lenx, node.leny, 1, width, WsqConstants.HIFILT, WsqConstants.LOFILT,
					node.invCl != 0);
		}
	}

	/**
	 * Filters one pass of the 2-D wavelet. C {@code get_lets} receives pointers
	 * already offset to the subband origin; {@code destBase}/{@code srcBase}
	 * model those pointers as linear indices. Even-length filters negate the
	 * high-pass taps (NBIS); the FBI bank used in production is odd-length.
	 *
	 * @param dest     destination coefficients
	 * @param destBase linear origin in {@code dest}
	 * @param old      source samples
	 * @param srcBase  linear origin in {@code old}
	 * @param len1     number of rows (or columns) to filter
	 * @param len2     length of each 1-D signal
	 * @param pitch    spacing between successive {@code len1} lines
	 * @param stride   spacing between successive samples along {@code len2}
	 * @param hiIn     high-pass analysis taps (cloned; may be negated)
	 * @param lo       low-pass analysis taps
	 * @param inv      when true, store high-pass coefficients before low-pass
	 */
	static void getLets(float[] dest, int destBase, float[] old, int srcBase, int len1, int len2, int pitch, int stride,
			float[] hiIn, float[] lo, boolean inv) {
		float[] hi = hiIn.clone();
		int hsz = hi.length;
		int lsz = lo.length;
		int daEv = len2 % 2;
		int fiEv = lsz % 2;
		int loc;
		int hoc;
		int olle;
		int ohle;
		int olre;
		int ohre;
		if (fiEv != 0) {
			loc = (lsz - 1) / 2;
			hoc = (hsz - 1) / 2 - 1;
			olle = 0;
			ohle = 0;
			olre = 0;
			ohre = 0;
		} else {
			loc = lsz / 2 - 2;
			hoc = hsz / 2 - 2;
			olle = 1;
			ohle = 1;
			olre = 1;
			ohre = 1;
			if (loc == -1) {
				loc = 0;
				olle = 0;
			}
			if (hoc == -1) {
				hoc = 0;
				ohle = 0;
			}
			for (int i = 0; i < hsz; i++) {
				hi[i] *= -1.0f;
			}
		}

		int pstr = stride;
		int nstr = -pstr;
		int llen;
		int hlen;
		if (daEv != 0) {
			llen = (len2 + 1) / 2;
			hlen = llen - 1;
		} else {
			llen = len2 / 2;
			hlen = llen;
		}

		for (int rwCl = 0; rwCl < len1; rwCl++) {
			int hipass;
			int lopass;
			if (inv) {
				hipass = destBase + rwCl * pitch;
				lopass = hipass + hlen * stride;
			} else {
				lopass = destBase + rwCl * pitch;
				hipass = lopass + llen * stride;
			}
			int p0 = srcBase + rwCl * pitch;
			int p1 = p0 + (len2 - 1) * stride;
			int lspx = p0 + (loc * stride);
			int lspxstr = nstr;
			int lle2 = olle;
			int lre2 = olre;
			int hspx = p0 + (hoc * stride);
			int hspxstr = nstr;
			int hle2 = ohle;
			int hre2 = ohre;
			for (int pix = 0; pix < hlen; pix++) {
				int lpxstr = lspxstr;
				int lpx = lspx;
				int lle = lle2;
				int lre = lre2;
				dest[lopass] = old[lpx] * lo[0];
				for (int i = 1; i < lsz; i++) {
					if (lpx == p0) {
						if (lle != 0) {
							lpxstr = 0;
							lle = 0;
						} else {
							lpxstr = pstr;
						}
					}
					if (lpx == p1) {
						if (lre != 0) {
							lpxstr = 0;
							lre = 0;
						} else {
							lpxstr = nstr;
						}
					}
					lpx += lpxstr;
					dest[lopass] += old[lpx] * lo[i];
				}
				lopass += stride;

				int hpxstr = hspxstr;
				int hpx = hspx;
				int hle = hle2;
				int hre = hre2;
				dest[hipass] = old[hpx] * hi[0];
				for (int i = 1; i < hsz; i++) {
					if (hpx == p0) {
						if (hle != 0) {
							hpxstr = 0;
							hle = 0;
						} else {
							hpxstr = pstr;
						}
					}
					if (hpx == p1) {
						if (hre != 0) {
							hpxstr = 0;
							hre = 0;
						} else {
							hpxstr = nstr;
						}
					}
					hpx += hpxstr;
					dest[hipass] += old[hpx] * hi[i];
				}
				hipass += stride;

				for (int i = 0; i < 2; i++) {
					if (lspx == p0) {
						if (lle2 != 0) {
							lspxstr = 0;
							lle2 = 0;
						} else {
							lspxstr = pstr;
						}
					}
					lspx += lspxstr;
					if (hspx == p0) {
						if (hle2 != 0) {
							hspxstr = 0;
							hle2 = 0;
						} else {
							hspxstr = pstr;
						}
					}
					hspx += hspxstr;
				}
			}
			if (daEv != 0) {
				int lpxstr = lspxstr;
				int lpx = lspx;
				int lle = lle2;
				int lre = lre2;
				dest[lopass] = old[lpx] * lo[0];
				for (int i = 1; i < lsz; i++) {
					if (lpx == p0) {
						if (lle != 0) {
							lpxstr = 0;
							lle = 0;
						} else {
							lpxstr = pstr;
						}
					}
					if (lpx == p1) {
						if (lre != 0) {
							lpxstr = 0;
							lre = 0;
						} else {
							lpxstr = nstr;
						}
					}
					lpx += lpxstr;
					dest[lopass] += old[lpx] * lo[i];
				}
			}
		}
	}
}
