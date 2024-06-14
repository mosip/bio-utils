package io.mosip.kernel.biometrics.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

/**
 * Represents a Biometric Record (BIR) containing version information, CBEFF
 * version, BIR information, biometric segments, and additional key-value pairs.
 * This class supports serialization and deserialization.
 * <p>
 * This class utilizes Lombok's {@code @Data} annotation for automatic
 * generation of getters, setters, toString, equals, and hashCode methods.
 * </p>
 * 
 * <p>
 * Each biometric segment in the record can represent any modality, with each
 * subtype specified as an element in the segments list, along with its type and
 * subtype information.
 * </p>
 * 
 * <p>
 * This class does not use a builder pattern, but provides constructors for
 * setting initial values of version, CBEFF version, and BIR information.
 * </p>
 * 
 * <p>
 * Serialization and deserialization of this class can be achieved through
 * various mechanisms such as XML or JSON, depending on the application
 * requirements.
 * </p>
 * 
 * <p>
 * For custom key-value pairs, the 'others' field can be used, which is
 * represented as a HashMap where each entry contains a key-value pair.
 * </p>
 * 
 * <p>
 * Note: This class assumes that the BIRInfo and VersionType classes are
 * appropriately defined and imported.
 * </p>
 * 
 * @author Ramadurai Pandian
 * @version 1.0.0
 */

@Data
public class BiometricRecord implements Serializable {
	/**
	 * SerialVersionUID for serialization.
	 */
	private static final long serialVersionUID = 3688026570163725740L;

	/**
	 * The version of the Biometric Record.
	 */
	protected VersionType version;

	/**
	 * The CBEFF version of the Biometric Record.
	 */
	protected VersionType cbeffversion;

	/**
	 * The BIR information associated with the Biometric Record.
	 */
	protected BIRInfo birInfo;

	/**
	 * List of biometric segments (BIRs) within the record. Each segment represents
	 * a modality with type and subtype information.
	 */
	@SuppressWarnings({ "java:S1948" })
	protected List<BIR> segments;

	/**
	 * Additional key-value pairs for customization. These pairs are stored in a
	 * HashMap.
	 */
	protected HashMap<String, String> others;

	/**
	 * Default constructor initializes an empty list of segments and an empty
	 * HashMap for 'others'.
	 */
	public BiometricRecord() {
		this.segments = new ArrayList<>();
		this.others = new HashMap<>();
	}

	/**
	 * Constructor initializes the Biometric Record with specified version, CBEFF
	 * version, and BIR information. It also initializes an empty list of segments
	 * and an empty HashMap for 'others'.
	 * 
	 * @param version      The version of the Biometric Record.
	 * @param cbeffversion The CBEFF version of the Biometric Record.
	 * @param birInfo      The BIR information associated with the Biometric Record.
	 */
	public BiometricRecord(VersionType version, VersionType cbeffversion, BIRInfo birInfo) {
		this.version = version;
		this.cbeffversion = cbeffversion;
		this.birInfo = birInfo;
		this.segments = new ArrayList<>();
		this.others = new HashMap<>();
	}
}