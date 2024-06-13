package io.mosip.kernel.biometrics.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

/**
 * 
 * BIR class with Builder to create data
 * 
 * @author Ramadurai Pandian
 *
 */

@Data
public class BiometricRecord implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3688026570163725740L;
	
	protected VersionType version;
	protected VersionType cbeffversion;
	protected BIRInfo birInfo;
	/**
	 * This can be of any modality, each subtype is an element in this list. it has
	 * type and subtype info in it
	 */
	@SuppressWarnings({ "java:S1948" })
	protected List<BIR> segments;
	protected HashMap<String, String> others;

	public BiometricRecord() {
		this.segments = new ArrayList<>();
		this.others = new HashMap<>();
	}

	public BiometricRecord(VersionType version, VersionType cbeffversion, BIRInfo birInfo) {
		this.version = version;
		this.cbeffversion = cbeffversion;
		this.birInfo = birInfo;
		this.segments = new ArrayList<>();
		this.others = new HashMap<>();
	}
}