package io.mosip.biometrics.util.nist.wsq.encoder;

/**
 * Marker values, filter-bank taps, and FBI/NBIS limits used by the WSQ encoder.
 * <p>
 * Constants match public-domain NIST NBIS {@code imgtools/include/wsq.h}. The
 * algorithm is specified by the CJIS <em>WSQ Gray-scale Fingerprint Compression
 * Specification</em> (December 1997).
 * </p>
 */
final class WsqConstants {

	/** Start-of-image marker (SOI). */
	static final int SOI_WSQ = 0xffa0;
	/** End-of-image marker (EOI). */
	static final int EOI_WSQ = 0xffa1;
	/** Start-of-frame marker (SOF) carrying width, height, shift, and scale. */
	static final int SOF_WSQ = 0xffa2;
	/** Start-of-block marker (SOB) preceding a Huffman-coded subband block. */
	static final int SOB_WSQ = 0xffa3;
	/** Discrete transform table marker (DTT) storing analysis filter taps. */
	static final int DTT_WSQ = 0xffa4;
	/** Discrete quantization table marker (DQT). */
	static final int DQT_WSQ = 0xffa5;
	/** Define-Huffman-table marker (DHT). */
	static final int DHT_WSQ = 0xffa6;
	/** Comment marker (COM). */
	static final int COM_WSQ = 0xffa8;

	/** Length of the analysis high-pass filter (odd 7-tap FBI bank). */
	static final int MAX_HIFILT = 7;
	/** Length of the analysis low-pass filter (odd 9-tap FBI bank). */
	static final int MAX_LOFILT = 9;
	/** Number of wavelet-tree nodes built by {@link WsqTrees}. */
	static final int W_TREELEN = 20;
	/** Number of quantization-tree nodes (covers 60 live subbands plus padding). */
	static final int Q_TREELEN = 64;
	/** Allocated length of quantization arrays ({@code qbss}, {@code var}, …). */
	static final int MAX_SUBBANDS = 64;
	/** Number of coded wavelet subbands (0–59). */
	static final int NUM_SUBBANDS = 60;
	/** First subband index belonging to Huffman block 2. */
	static final int STRT_SUBBAND_2 = 19;
	/** First subband index belonging to Huffman block 3. */
	static final int STRT_SUBBAND_3 = 52;
	/** Exclusive end of coded subbands (same as {@link #NUM_SUBBANDS}). */
	static final int STRT_SUBBAND_DEL = NUM_SUBBANDS;
	/** First subband using the mid-frequency {@code m2} weight. */
	static final int STRT_SIZE_REGION_2 = 4;
	/** First subband using the high-frequency {@code m3} weight. */
	static final int STRT_SIZE_REGION_3 = 51;
	/** Subbands with variance below this threshold are discarded (qbss = 0). */
	static final float VARIANCE_THRESH = 1.01f;

	/** Huffman coder state: emitting a non-zero coefficient. */
	static final int COEFF_CODE = 0;
	/** Huffman coder state: accumulating a zero run. */
	static final int RUN_CODE = 1;
	/** Maximum Huffman code length after JPEG Annex-K adjustment (bits). */
	static final int MAX_HUFFBITS = 16;
	/** Number of Huffman symbols used by WSQ (indices 0–255 plus EOB slot). */
	static final int MAX_HUFFCOUNTS_WSQ = 256;
	/** Largest coefficient magnitude encoded with a dedicated Huffman symbol. */
	static final int MAX_HUFFCOEFF = 74;
	/** Longest zero-run encoded with a dedicated Huffman symbol. */
	static final int MAX_HUFFZRUN = 100;

	/** FBI-typical fingerprint bitrate in bits per pixel (lossy). */
	static final float DEFAULT_BIT_RATE = 0.75f;
	/**
	 * Target peak {@code |sip|} for lossless quantization. Huffman extra bits
	 * are 16-bit, so this stays well inside {@code 65535} and still gives
	 * sub-gray-level wavelet bins on 8-bit fingerprints.
	 */
	static final float LOSSLESS_SIP_RANGE = 16384.0f;
	/** Default scan resolution written into the NIST comment (pixels per inch). */
	static final int DEFAULT_PPI = 500;

	/**
	 * Analysis high-pass taps of the odd 7-tap FBI filter bank (symmetric around
	 * the centre tap).
	 */
	static final float[] HIFILT = { 0.06453888262893845f, -0.04068941760955844f, -0.41809227322221221f,
			0.78848561640566439f, -0.41809227322221221f, -0.04068941760955844f, 0.06453888262893845f };

	/**
	 * Analysis low-pass taps of the odd 9-tap FBI filter bank (symmetric around
	 * the centre tap).
	 */
	static final float[] LOFILT = { 0.03782845550699546f, -0.02384946501938000f, -0.11062440441842342f,
			0.37740285561265380f, 0.85269867900940344f, 0.37740285561265380f, -0.11062440441842342f,
			-0.02384946501938000f, 0.03782845550699546f };

	/** Utility holder; not instantiated. */
	private WsqConstants() {
	}
}
