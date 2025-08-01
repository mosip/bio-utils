package io.mosip.kernel.biometrics.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.deser.std.MapEntryDeserializer;

import io.mosip.kernel.core.cbeffutil.common.Base64Adapter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Biometric Information Record (BIR) containing various attributes
 * such as version, CBEFF version, BIR information, biometric data block (BDB)
 * info, structured biometric data (SB), and additional key-value pairs. This
 * class supports XML and JSON serialization/deserialization.
 * <p>
 * This class uses a builder pattern ({@link BIR.BIRBuilder}) for object
 * creation.
 * </p>
 * 
 * @author Ramadurai Pandian
 * @version 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BIRType", propOrder = { "version", "cbeffversion", "birInfo", "bdbInfo", "sbInfo", "birs", "bdb", "sb",
		"others" })
@XmlRootElement(name = "BIR")
@Data
@NoArgsConstructor
@JsonDeserialize(builder = BIR.BIRBuilder.class)
public class BIR implements Serializable {
	@XmlElement(name = "Version")
	private VersionType version;

	@XmlElement(name = "CBEFFVersion")
	private VersionType cbeffversion;

	@XmlElement(name = "BIRInfo", required = true)
	private BIRInfo birInfo;

	@XmlElement(name = "BDBInfo")
	private BDBInfo bdbInfo;

	@XmlElement(name = "BDB")
	@XmlJavaTypeAdapter(Base64Adapter.class)
	@JsonFormat(shape = JsonFormat.Shape.ARRAY)
	private byte[] bdb;

	@XmlElement(name = "SB")
	@XmlJavaTypeAdapter(Base64Adapter.class)
	@JsonFormat(shape = JsonFormat.Shape.ARRAY)
	private byte[] sb;

	@SuppressWarnings({ "java:S1948" })
	@XmlElement(name = "BIR")
	protected List<BIR> birs;

	@XmlElement(name = "SBInfo")
	private SBInfo sbInfo;

	@XmlJavaTypeAdapter(AdapterOthersListToHashMap.class)
	@JsonDeserialize(using = MapEntryDeserializer.class)
	private HashMap<String, String> others;

	/**
	 * Constructs a new instance of BIR using a builder pattern.
	 * 
	 * @param birBuilder the builder used for constructing this BIR object
	 */
	public BIR(BIRBuilder birBuilder) {
		this.version = birBuilder.version;
		this.cbeffversion = birBuilder.cbeffversion;
		this.birInfo = birBuilder.birInfo;
		this.bdbInfo = birBuilder.bdbInfo;
		this.bdb = birBuilder.bdb;
		this.sb = birBuilder.sb;
		this.sbInfo = birBuilder.sbInfo;
		this.others = birBuilder.others;
	}

	/**
	 * Builder pattern class for constructing instances of BIR.
	 */
	@JsonPOJOBuilder(withPrefix = "with")
	public static class BIRBuilder {
		private VersionType version;
		private VersionType cbeffversion;
		private BIRInfo birInfo;
		private BDBInfo bdbInfo;
		private byte[] bdb;
		private byte[] sb;
		private SBInfo sbInfo;
		private HashMap<String, String> others = new HashMap<>();

		public BIRBuilder(){
		}
		/**
		 * Sets the additional key-value pairs for the BIR.
		 * 
		 * @param others the additional key-value pairs
		 * @return this builder instance
		 */
		@JsonProperty("others")
		public BIRBuilder withOthers(HashMap<String, String> others) {
			this.others = others;
			return this;
		}

		/**
		 * Adds a key-value pair to the additional key-value pairs for the BIR.
		 * 
		 * @param key   the key of the additional pair
		 * @param value the value of the additional pair
		 * @return this builder instance
		 */
		public BIRBuilder withOthers(String key, String value) {
			if (Objects.isNull(this.others))
				this.others = new HashMap<>();
			
			this.others.put(key, value);
			return this;
		}

		/**
		 * Sets the version of the BIR.
		 * 
		 * @param version the version of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("version")
		public BIRBuilder withVersion(VersionType version) {
			this.version = version;
			return this;
		}

		/**
		 * Sets the CBEFF version of the BIR.
		 * 
		 * @param cbeffversion the CBEFF version of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("cbeffversion")
		public BIRBuilder withCbeffversion(VersionType cbeffversion) {
			this.cbeffversion = cbeffversion;
			return this;
		}

		/**
		 * Sets the BIR information.
		 * 
		 * @param birInfo the BIR information
		 * @return this builder instance
		 */
		@JsonProperty("birInfo")
		public BIRBuilder withBirInfo(BIRInfo birInfo) {
			this.birInfo = birInfo;
			return this;
		}

		/**
		 * Sets the BDBInfo (Biometric Data Block information).
		 * 
		 * @param bdbInfo the BDBInfo
		 * @return this builder instance
		 */
		@JsonProperty("bdbInfo")
		public BIRBuilder withBdbInfo(BDBInfo bdbInfo) {
			this.bdbInfo = bdbInfo;
			return this;
		}

		/**
		 * Sets the BDB (Biometric Data Block).
		 * 
		 * @param bdb the BDB
		 * @return this builder instance
		 */
		@JsonProperty("bdb")
		@JsonFormat(shape = JsonFormat.Shape.ARRAY)
		public BIRBuilder withBdb(byte[] bdb) {
			this.bdb = bdb;
			return this;
		}

		/**
		 * Sets the SB (Structured Biometric Data).
		 * 
		 * @param sb the SB
		 * @return this builder instance
		 */
		@JsonProperty("sb")
		@JsonFormat(shape = JsonFormat.Shape.ARRAY)
		public BIRBuilder withSb(byte[] sb) {
			this.sb = sb == null ? new byte[0] : sb;
			return this;
		}

		/**
		 * Sets the SBInfo (Structured Biometric Data information).
		 * 
		 * @param sbInfo the SBInfo
		 * @return this builder instance
		 */
		@JsonProperty("sbInfo")
		public BIRBuilder withSbInfo(SBInfo sbInfo) {
			this.sbInfo = sbInfo;
			return this;
		}

		/**
		 * Builds and returns a new instance of BIR using this builder.
		 * 
		 * @return a new instance of BIR
		 */
		public BIR build() {
			return new BIR(this);
		}
	}
}