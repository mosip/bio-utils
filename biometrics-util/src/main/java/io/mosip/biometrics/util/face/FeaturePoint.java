package io.mosip.biometrics.util.face;

/**
 * Feature points as described in Section 5.6.3 of ISO/IEC FCD 19794-5.
 * 
 */
public class FeaturePoint {

	private int type;
	private int majorCode;
	private int minorCode;
	private int x;
	private int y;

	/**
	 * Constructs a new feature point.
	 * 
	 * @param type feature point type
	 * @param majorCode major code
	 * @param minorCode minor code
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	public FeaturePoint(int type, int majorCode, int minorCode,
			int x, int y) {
		this.type = type;
		this.majorCode = majorCode;
		this.minorCode = minorCode;
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a new feature point.
	 * 
	 * @param type feature point type
	 * @param code combined major and minor code
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	FeaturePoint(int type, byte code, int x, int y) {
		this(type, (int)((code & 0xF0) >> 4), (int)(code & 0x0F), x ,y);
	}

	/**
	 * Gets the major code of this point.
	 * 
	 * @return major code
	 */
	public int getMajorCode() {
		return majorCode;
	}

	/**
	 * Gets the minor code of this point.
	 * 
	 * @return minor code
	 */
	public int getMinorCode() {
		return minorCode;
	}

	/**
	 * Gets the type of this point.
	 * 
	 * @return type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the X-coordinate of this point.
	 * 
	 * @return X-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the Y-coordinate of this point.
	 * 
	 * @return Y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Generates a textual representation of this point.
	 * 
	 * @return a textual representation of this point
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append("( point: "); out.append(getMajorCode()); out.append("."); out.append(getMinorCode());
		out.append(", ");
		out.append("type: "); out.append(Integer.toHexString(type)); out.append(", ");
		out.append("("); out.append(x); out.append(", ");
		out.append(y); out.append(")");
		out.append(")");
		return out.toString();
	}
}
