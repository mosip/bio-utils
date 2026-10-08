package io.mosip.biometrics.util.nist.wsq.encoder;

/**
 * Huffman table construction and block compression from public-domain NIST NBIS
 * {@code encoder.c} / {@code jpegl/huff.c} ({@code count_block},
 * {@code gen_hufftable_wsq}, {@code compress_block}, JPEG Annex-K code-size
 * assignment).
 * <p>
 * Symbols: zero-runs 1–100, extension codes 101–106, and coefficients mapped
 * to {@code pix + 180} for {@code |pix| ≤ 74}.
 * </p>
 */
final class WsqHuffman {

	/**
	 * One Huffman code: bit length and the codeword itself (MSB-aligned in the
	 * low {@code size} bits).
	 */
	static final class HuffCode {
		/** Code length in bits; 0 means unused. */
		int size;
		/** Codeword bits. */
		int code;
	}

	/**
	 * A complete WSQ Huffman table: JPEG-style {@code bits}/{@code values} plus
	 * a symbol-indexed {@link HuffCode} array used by the compressor.
	 */
	static final class Table {
		/** Number of codes of each length 1–16 (JPEG DHT {@code Li}). */
		byte[] bits;
		/** Symbols in increasing code-length order (JPEG DHT {@code Vij}). */
		byte[] values;
		/** Lookup by WSQ symbol index (0–256). */
		HuffCode[] codes;
	}

	/** Utility holder; not instantiated. */
	private WsqHuffman() {
	}

	/**
	 * Builds a Huffman table from one or more consecutive coefficient blocks.
	 *
	 * @param sip        packed quantized coefficients (16-bit magnitudes fit Huffman extra bits)
	 * @param offset     start index in {@code sip}
	 * @param blockSizes lengths of the blocks to combine (block 1 is one
	 *                   length; blocks 2+3 pass two lengths)
	 * @return table ready for {@link #compress} and DHT serialization
	 * @throws IllegalStateException if an all-1s codeword is produced
	 */
	static Table build(int[] sip, int offset, int[] blockSizes) {
		int[] counts = countBlock(sip, offset, blockSizes[0]);
		int pos = offset + blockSizes[0];
		for (int i = 1; i < blockSizes.length; i++) {
			int[] extra = countBlock(sip, pos, blockSizes[i]);
			for (int j = 0; j < WsqConstants.MAX_HUFFCOUNTS_WSQ; j++) {
				counts[j] += extra[j];
			}
			pos += blockSizes[i];
		}
		int[] codesize = findHuffSizes(counts);
		int[] adjust = new int[1];
		byte[] bits = findNumHuffSizes(codesize, adjust);
		if (adjust[0] != 0) {
			sortHuffbits(bits);
		}
		byte[] values = sortCodeSizes(codesize);
		int[] lastSize = new int[1];
		HuffCode[] table1 = buildHuffsizes(bits, lastSize);
		buildHuffcodes(table1);
		if (hasAllOnes(table1, lastSize[0])) {
			throw new IllegalStateException("WSQ huffman table contains an all-1s code");
		}
		HuffCode[] table2 = buildHuffcodeTable(table1, lastSize[0], values);
		Table table = new Table();
		table.bits = bits;
		table.values = values;
		table.codes = table2;
		return table;
	}

	/**
	 * Huffman-encodes {@code sipSiz} coefficients starting at {@code offset},
	 * stuffing {@code 0xFF} bytes and padding leftover bits with ones.
	 *
	 * @param sip    packed coefficients
	 * @param offset start index
	 * @param sipSiz number of coefficients
	 * @param codes  symbol-indexed table from {@link #build}
	 * @return stuffed Huffman bytes
	 */
	static byte[] compress(int[] sip, int offset, int sipSiz, HuffCode[] codes) {
		WsqBitOutput bits = new WsqBitOutput();
		int maxCoeff = WsqConstants.MAX_HUFFCOEFF;
		int loMaxCoeff = 1 - maxCoeff;
		int maxZRun = WsqConstants.MAX_HUFFZRUN;
		int state = WsqConstants.COEFF_CODE;
		int rcnt = 0;
		for (int cnt = 0; cnt < sipSiz; cnt++) {
			int pix = sip[offset + cnt];
			if (state == WsqConstants.COEFF_CODE) {
				if (pix == 0) {
					state = WsqConstants.RUN_CODE;
					rcnt = 1;
					continue;
				}
				writeCoeff(bits, codes, pix, maxCoeff, loMaxCoeff);
			} else {
				if (pix == 0 && rcnt < 0xffff) {
					rcnt++;
					continue;
				}
				writeRun(bits, codes, rcnt, maxZRun);
				if (pix != 0) {
					writeCoeff(bits, codes, pix, maxCoeff, loMaxCoeff);
					state = WsqConstants.COEFF_CODE;
				} else {
					rcnt = 1;
					state = WsqConstants.RUN_CODE;
				}
			}
		}
		if (state == WsqConstants.RUN_CODE) {
			writeRun(bits, codes, rcnt, maxZRun);
		}
		bits.flushBits();
		return bits.toByteArray();
	}

	/**
	 * Emits a coefficient: dedicated symbol for {@code |pix| ≤ 74}, else 8- or
	 * 16-bit extension codes 101–104.
	 *
	 * @param bits       bitstream
	 * @param codes      Huffman table
	 * @param pix        quantized coefficient
	 * @param maxCoeff   74
	 * @param loMaxCoeff -73
	 */
	private static void writeCoeff(WsqBitOutput bits, HuffCode[] codes, int pix, int maxCoeff, int loMaxCoeff) {
		if (pix > maxCoeff) {
			if (pix > 255) {
				bits.writeBits(codes[103].code, codes[103].size);
				bits.writeBits(pix, 16);
			} else {
				bits.writeBits(codes[101].code, codes[101].size);
				bits.writeBits(pix, 8);
			}
		} else if (pix < loMaxCoeff) {
			if (pix < -255) {
				bits.writeBits(codes[104].code, codes[104].size);
				bits.writeBits(-pix, 16);
			} else {
				bits.writeBits(codes[102].code, codes[102].size);
				bits.writeBits(-pix, 8);
			}
		} else {
			bits.writeBits(codes[pix + 180].code, codes[pix + 180].size);
		}
	}

	/**
	 * Emits a zero-run: dedicated symbol for length ≤ 100, else 8- or 16-bit
	 * extension codes 105–106.
	 *
	 * @param bits     bitstream
	 * @param codes    Huffman table
	 * @param rcnt     run length
	 * @param maxZRun  100
	 */
	private static void writeRun(WsqBitOutput bits, HuffCode[] codes, int rcnt, int maxZRun) {
		if (rcnt <= maxZRun) {
			bits.writeBits(codes[rcnt].code, codes[rcnt].size);
		} else if (rcnt <= 0xff) {
			bits.writeBits(codes[105].code, codes[105].size);
			bits.writeBits(rcnt, 8);
		} else if (rcnt <= 0xffff) {
			bits.writeBits(codes[106].code, codes[106].size);
			bits.writeBits(rcnt, 16);
		} else {
			throw new IllegalStateException("WSQ zero-run too large");
		}
	}

	/**
	 * Histogram of Huffman symbols for one coefficient block (NBIS
	 * {@code count_block}).
	 *
	 * @param sip    coefficients
	 * @param offset start
	 * @param sipSiz length
	 * @return counts of length {@link WsqConstants#MAX_HUFFCOUNTS_WSQ}+1 (last
	 *         slot forced to 1 as in NBIS)
	 */
	private static int[] countBlock(int[] sip, int offset, int sipSiz) {
		int[] counts = new int[WsqConstants.MAX_HUFFCOUNTS_WSQ + 1];
		counts[WsqConstants.MAX_HUFFCOUNTS_WSQ] = 1;
		int maxCoeff = WsqConstants.MAX_HUFFCOEFF;
		int loMaxCoeff = 1 - maxCoeff;
		int maxZRun = WsqConstants.MAX_HUFFZRUN;
		int state = WsqConstants.COEFF_CODE;
		int rcnt = 0;
		for (int cnt = 0; cnt < sipSiz; cnt++) {
			int pix = sip[offset + cnt];
			if (state == WsqConstants.COEFF_CODE) {
				if (pix == 0) {
					state = WsqConstants.RUN_CODE;
					rcnt = 1;
					continue;
				}
				bumpCoeff(counts, pix, maxCoeff, loMaxCoeff);
			} else {
				if (pix == 0 && rcnt < 0xffff) {
					rcnt++;
					continue;
				}
				bumpRun(counts, rcnt, maxZRun);
				if (pix != 0) {
					bumpCoeff(counts, pix, maxCoeff, loMaxCoeff);
					state = WsqConstants.COEFF_CODE;
				} else {
					rcnt = 1;
					state = WsqConstants.RUN_CODE;
				}
			}
		}
		if (state == WsqConstants.RUN_CODE) {
			bumpRun(counts, rcnt, maxZRun);
		}
		return counts;
	}

	/**
	 * Increments the histogram bucket for one coefficient.
	 *
	 * @param counts     histogram
	 * @param pix        coefficient
	 * @param maxCoeff   74
	 * @param loMaxCoeff -73
	 */
	private static void bumpCoeff(int[] counts, int pix, int maxCoeff, int loMaxCoeff) {
		if (pix > maxCoeff) {
			counts[pix > 255 ? 103 : 101]++;
		} else if (pix < loMaxCoeff) {
			counts[pix < -255 ? 104 : 102]++;
		} else {
			counts[pix + 180]++;
		}
	}

	/**
	 * Increments the histogram bucket for one zero-run.
	 *
	 * @param counts  histogram
	 * @param rcnt    run length
	 * @param maxZRun 100
	 */
	private static void bumpRun(int[] counts, int rcnt, int maxZRun) {
		if (rcnt <= maxZRun) {
			counts[rcnt]++;
		} else if (rcnt <= 0xff) {
			counts[105]++;
		} else if (rcnt <= 0xffff) {
			counts[106]++;
		} else {
			throw new IllegalStateException("WSQ zero-run too large");
		}
	}

	/**
	 * JPEG Annex-K procedure to assign code lengths from frequencies.
	 *
	 * @param freqIn symbol frequencies (cloned)
	 * @return code length per symbol
	 */
	private static int[] findHuffSizes(int[] freqIn) {
		int max = WsqConstants.MAX_HUFFCOUNTS_WSQ;
		int[] freq = freqIn.clone();
		int[] codesize = new int[max + 1];
		int[] others = new int[max + 1];
		for (int i = 0; i <= max; i++) {
			others[i] = -1;
		}
		while (true) {
			int[] pair = findLeastFreq(freq, max);
			int value1 = pair[0];
			int value2 = pair[1];
			if (value2 == -1) {
				break;
			}
			freq[value1] += freq[value2];
			freq[value2] = 0;
			codesize[value1]++;
			while (others[value1] != -1) {
				value1 = others[value1];
				codesize[value1]++;
			}
			others[value1] = value2;
			codesize[value2]++;
			while (others[value2] != -1) {
				value2 = others[value2];
				codesize[value2]++;
			}
		}
		return codesize;
	}

	/**
	 * Finds the two smallest remaining frequencies (JPEG Annex-K).
	 *
	 * @param freq frequencies
	 * @param max  last symbol index
	 * @return {@code {value1, value2}}; {@code value2 == -1} when only one
	 *         symbol remains
	 */
	private static int[] findLeastFreq(int[] freq, int max) {
		int value1 = -1;
		int value2 = -1;
		int code1 = 0;
		int code2 = 0;
		int set = 1;
		for (int i = 0; i <= max; i++) {
			if (freq[i] == 0) {
				continue;
			}
			if (set == 1) {
				code1 = freq[i];
				value1 = i;
				set++;
				continue;
			}
			if (set == 2) {
				code2 = freq[i];
				value2 = i;
				set++;
			}
			int codeTemp = freq[i];
			int valueTemp = i;
			if (code1 < codeTemp && code2 < codeTemp) {
				continue;
			}
			if (codeTemp < code1 || (codeTemp == code1 && valueTemp > value1)) {
				code2 = code1;
				value2 = value1;
				code1 = codeTemp;
				value1 = valueTemp;
				continue;
			}
			if (codeTemp < code2 || (codeTemp == code2 && valueTemp > value2)) {
				code2 = codeTemp;
				value2 = valueTemp;
			}
		}
		return new int[] { value1, value2 };
	}

	/**
	 * Counts how many symbols have each code length; sets {@code adjust[0]} when
	 * any length exceeds 16.
	 *
	 * @param codesize per-symbol lengths
	 * @param adjust   output flag
	 * @return {@code bits} array of length 32
	 */
	private static byte[] findNumHuffSizes(int[] codesize, int[] adjust) {
		byte[] bits = new byte[WsqConstants.MAX_HUFFBITS << 1];
		adjust[0] = 0;
		for (int i = 0; i < WsqConstants.MAX_HUFFCOUNTS_WSQ; i++) {
			if (codesize[i] != 0) {
				bits[codesize[i] - 1]++;
				if (codesize[i] > WsqConstants.MAX_HUFFBITS) {
					adjust[0] = 1;
				}
			}
		}
		return bits;
	}

	/**
	 * JPEG Annex-K adjustment that pulls over-long codes back to 16 bits.
	 *
	 * @param bits in/out length histogram
	 */
	private static void sortHuffbits(byte[] bits) {
		int l3 = WsqConstants.MAX_HUFFBITS << 1;
		int l1 = l3 - 1;
		int l2 = WsqConstants.MAX_HUFFBITS - 1;
		short[] tbits = new short[l3];
		for (int i = 0; i < l3; i++) {
			tbits[i] = (short) (bits[i] & 0xff);
		}
		int i = l1;
		for (; i > l2; i--) {
			while (tbits[i] > 0) {
				int j = i - 2;
				while (tbits[j] == 0) {
					j--;
				}
				tbits[i] -= 2;
				tbits[i - 1] += 1;
				tbits[j + 1] += 2;
				tbits[j] -= 1;
			}
			tbits[i] = 0;
		}
		while (tbits[i] == 0) {
			i--;
		}
		tbits[i] -= 1;
		for (int n = 0; n < l3; n++) {
			bits[n] = (byte) tbits[n];
		}
	}

	/**
	 * Lists symbols in increasing code-length order.
	 *
	 * @param codesize per-symbol lengths
	 * @return JPEG DHT {@code Vij} array
	 */
	private static byte[] sortCodeSizes(int[] codesize) {
		byte[] values = new byte[WsqConstants.MAX_HUFFCOUNTS_WSQ + 1];
		int i2 = 0;
		for (int i = 1; i <= (WsqConstants.MAX_HUFFBITS << 1); i++) {
			for (int i3 = 0; i3 < WsqConstants.MAX_HUFFCOUNTS_WSQ; i3++) {
				if (codesize[i3] == i) {
					values[i2++] = (byte) i3;
				}
			}
		}
		return values;
	}

	/**
	 * Assigns a bit length to each successive code in length order.
	 *
	 * @param huffbits length histogram
	 * @param tempSize output last occupied index
	 * @return table sized {@code MAX_HUFFCOUNTS_WSQ + 1}
	 */
	private static HuffCode[] buildHuffsizes(byte[] huffbits, int[] tempSize) {
		HuffCode[] table = new HuffCode[WsqConstants.MAX_HUFFCOUNTS_WSQ + 1];
		for (int n = 0; n < table.length; n++) {
			table[n] = new HuffCode();
		}
		tempSize[0] = 0;
		for (int codeSize = 1; codeSize <= WsqConstants.MAX_HUFFBITS; codeSize++) {
			int numberOfCodes = 1;
			while (numberOfCodes <= (huffbits[codeSize - 1] & 0xff)) {
				table[tempSize[0]].size = codeSize;
				tempSize[0]++;
				numberOfCodes++;
			}
		}
		table[tempSize[0]].size = 0;
		return table;
	}

	/**
	 * Assigns canonical codewords from the length table (JPEG Annex-C).
	 *
	 * @param table in/out; {@code size} must already be filled
	 */
	private static void buildHuffcodes(HuffCode[] table) {
		int pointer = 0;
		int tempCode = 0;
		int tempSize = table[0].size;
		if (table[0].size == 0) {
			return;
		}
		do {
			do {
				table[pointer].code = tempCode;
				tempCode++;
				pointer++;
			} while (table[pointer].size == tempSize);
			if (table[pointer].size == 0) {
				return;
			}
			do {
				tempCode <<= 1;
				tempSize++;
			} while (table[pointer].size != tempSize);
		} while (table[pointer].size == tempSize);
	}

	/**
	 * Maps length-ordered codes onto WSQ symbol indices.
	 *
	 * @param in       length-ordered table
	 * @param lastSize number of used entries
	 * @param values   symbol for each length-ordered slot
	 * @return symbol-indexed table
	 */
	private static HuffCode[] buildHuffcodeTable(HuffCode[] in, int lastSize, byte[] values) {
		HuffCode[] out = new HuffCode[WsqConstants.MAX_HUFFCOUNTS_WSQ + 1];
		for (int n = 0; n < out.length; n++) {
			out[n] = new HuffCode();
		}
		for (int size = 0; size < lastSize; size++) {
			int idx = values[size] & 0xff;
			out[idx].code = in[size].code;
			out[idx].size = in[size].size;
		}
		return out;
	}

	/**
	 * Returns {@code true} if any used codeword is all ones (forbidden by JPEG /
	 * WSQ so markers cannot appear in the entropy stream).
	 *
	 * @param table    length-ordered codes
	 * @param lastSize number of used entries
	 * @return whether an all-1s code exists
	 */
	private static boolean hasAllOnes(HuffCode[] table, int lastSize) {
		for (int i = 0; i < lastSize; i++) {
			boolean allOnes = true;
			for (int k = 0; k < table[i].size && allOnes; k++) {
				allOnes = ((table[i].code >> k) & 0x0001) != 0;
			}
			if (allOnes && table[i].size > 0) {
				return true;
			}
		}
		return false;
	}
}
