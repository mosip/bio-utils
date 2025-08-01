/**
 *
 */
package io.mosip.kernel.biometrics.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.constant.ProcessedLevelType;
import io.mosip.kernel.biometrics.constant.PurposeType;
import io.mosip.kernel.biometrics.constant.QualityType;
import io.mosip.kernel.core.cbeffutil.common.DateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents Biometric Data Block (BDB) information associated with biometric
 * records. This class includes details such as challenge response, index,
 * format, encryption status, creation date, validity dates, biometric type,
 * subtype, processed level, product information, capture device, feature
 * extraction algorithm, comparison algorithm, compression algorithm, purpose,
 * and quality.
 * <p>
 * This class supports XML and JSON serialization/deserialization.
 * </p>
 *
 * <p>
 * Serialization of dates (creationDate, notValidBefore, notValidAfter) is
 * handled using {@link XmlSchemaType} and {@link XmlJavaTypeAdapter}
 * annotations with {@link DateAdapter}.
 * </p>
 *
 * <p>
 * Biometric type and subtype are represented as lists of {@link BiometricType}
 * and {@link String} respectively. Processed level, purpose, and quality are
 * enums represented by {@link ProcessedLevelType}, {@link PurposeType}, and
 * {@link QualityType}.
 * </p>
 *
 * <p>
 * This class uses Lombok's {@code @Data} annotation for automatic generation of
 * getters, setters, toString, equals, and hashCode methods.
 * </p>
 *
 * <p>
 * The builder pattern ({@link BDBInfo.BDBInfoBuilder}) is used for constructing
 * instances of {@code BDBInfo}.
 * </p>
 *
 * <p>
 * Note: This class assumes that {@link RegistryIDType}, {@link DateAdapter},
 * {@link BiometricType}, {@link ProcessedLevelType}, {@link PurposeType}, and
 * {@link QualityType} are properly defined and imported.
 * </p>
 *
 * @author Ramadurai Pandian
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BDBInfoType", propOrder = { "challengeResponse", "index", "format", "encryption", "creationDate",
		"notValidBefore", "notValidAfter", "type", "subtype", "level", "product", "captureDevice",
		"featureExtractionAlgorithm", "comparisonAlgorithm", "compressionAlgorithm", "purpose", "quality" })
@Data
@NoArgsConstructor
@JsonDeserialize(builder = BDBInfo.BDBInfoBuilder.class)
public class BDBInfo implements Serializable {
	/**
	 * Serialized version UID for compatibility.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Challenge response associated with the BDB.
	 */
	@XmlElement(name = "ChallengeResponse")
	@JsonFormat(shape = JsonFormat.Shape.ARRAY)
	private byte[] challengeResponse;

	/**
	 * Index value of the BDB.
	 */
	@XmlElement(name = "Index")
	private String index;

	/**
	 * Format of the BDB represented by {@link RegistryIDType}.
	 */
	@XmlElement(name = "Format")
	private RegistryIDType format;

	/**
	 * Encryption status of the BDB.
	 */
	@XmlElement(name = "Encryption")
	private Boolean encryption;

	/**
	 * Date and time when the BDB was created.
	 */
	@XmlElement(name = "CreationDate")
	@XmlSchemaType(name = "dateTime")
	@XmlJavaTypeAdapter(DateAdapter.class)
	@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
	private LocalDateTime creationDate;

	/**
	 * Date and time when the BDB is valid from.
	 */
	@XmlElement(name = "NotValidBefore")
	@XmlSchemaType(name = "dateTime")
	@XmlJavaTypeAdapter(DateAdapter.class)
	@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
	private LocalDateTime notValidBefore;

	/**
	 * Date and time when the BDB is valid until.
	 */
	@XmlElement(name = "NotValidAfter")
	@XmlSchemaType(name = "dateTime")
	@XmlJavaTypeAdapter(DateAdapter.class)
	@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
	private LocalDateTime notValidAfter;

	/**
	 * List of biometric types associated with the BDB.
	 */
	@XmlList
	@XmlElement(name = "Type")
	private List<BiometricType> type;

	/**
	 * List of biometric subtypes associated with the BDB.
	 */
	@XmlList
	@XmlElement(name = "Subtype")
	private List<String> subtype;

	/**
	 * Processed level of the BDB represented by {@link ProcessedLevelType}.
	 */
	@XmlElement(name = "Level")
	@XmlSchemaType(name = "string")
	private ProcessedLevelType level;

	/**
	 * Product information associated with the BDB.
	 */
	@XmlElement(name = "Product")
	private RegistryIDType product;

	/**
	 * Capture device used for capturing biometric data.
	 */
	@XmlElement(name = "CaptureDevice")
	private RegistryIDType captureDevice;

	/**
	 * Feature extraction algorithm used to extract biometric features.
	 */
	@XmlElement(name = "FeatureExtractionAlgorithm")
	private RegistryIDType featureExtractionAlgorithm;

	/**
	 * Comparison algorithm used for comparing biometric data.
	 */
	@XmlElement(name = "ComparisonAlgorithm")
	private RegistryIDType comparisonAlgorithm;

	/**
	 * Compression algorithm used for compressing biometric data.
	 */
	@XmlElement(name = "CompressionAlgorithm")
	private RegistryIDType compressionAlgorithm;

	/**
	 * Purpose of using the BDB represented by {@link PurposeType}.
	 */
	@XmlElement(name = "Purpose")
	@XmlSchemaType(name = "string")
	private PurposeType purpose;

	/**
	 * Quality of the biometric data represented by {@link QualityType}.
	 */
	@XmlElement(name = "Quality")
	private QualityType quality;

	/**
	 * Constructs a new BDBInfo instance using the provided builder.
	 *
	 * @param bDBInfoBuilder the builder used to construct this BDBInfo object
	 */
	public BDBInfo(BDBInfoBuilder bDBInfoBuilder) {
		this.challengeResponse = bDBInfoBuilder.challengeResponse;
		this.index = bDBInfoBuilder.index;
		this.format = bDBInfoBuilder.format;
		this.encryption = bDBInfoBuilder.encryption;
		this.creationDate = bDBInfoBuilder.creationDate;
		this.notValidBefore = bDBInfoBuilder.notValidBefore;
		this.notValidAfter = bDBInfoBuilder.notValidAfter;
		this.type = bDBInfoBuilder.type;
		this.subtype = bDBInfoBuilder.subtype;
		this.level = bDBInfoBuilder.level;
		this.product = bDBInfoBuilder.product;
		this.purpose = bDBInfoBuilder.purpose;
		this.quality = bDBInfoBuilder.quality;
		this.captureDevice = bDBInfoBuilder.captureDevice;
		this.featureExtractionAlgorithm = bDBInfoBuilder.featureExtractionAlgorithm;
		this.comparisonAlgorithm = bDBInfoBuilder.comparisonAlgorithm;
		this.compressionAlgorithm = bDBInfoBuilder.compressionAlgorithm;
	}

	/**
	 * Builder class for constructing instances of {@code BDBInfo}.
	 */
	@JsonPOJOBuilder(withPrefix = "with")
	public static class BDBInfoBuilder {
		private byte[] challengeResponse;
		private String index;
		private RegistryIDType format;
		private Boolean encryption;

		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		private LocalDateTime creationDate;
		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		private LocalDateTime notValidBefore;
		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		private LocalDateTime notValidAfter;
		private List<BiometricType> type;
		private List<String> subtype;
		private ProcessedLevelType level;
		private RegistryIDType product;
		private PurposeType purpose;
		private QualityType quality;
		private RegistryIDType captureDevice;
		private RegistryIDType featureExtractionAlgorithm;
		private RegistryIDType comparisonAlgorithm;
		private RegistryIDType compressionAlgorithm;

		public BDBInfoBuilder(){
		}

		/**
		 * Sets the challenge response associated with the BDB.
		 *
		 * @param challengeResponse the challenge response
		 * @return this builder instance
		 */
		@JsonProperty("challengeResponse")
		@JsonFormat(shape = JsonFormat.Shape.ARRAY)
		public BDBInfoBuilder withChallengeResponse(byte[] challengeResponse) {
			this.challengeResponse = challengeResponse;
			return this;
		}

		/**
		 * Sets the index identifier of the BDB.
		 *
		 * @param index the index identifier
		 * @return this builder instance
		 */
		@JsonProperty("index")
		public BDBInfoBuilder withIndex(String index) {
			this.index = index;
			return this;
		}

		/**
		 * Sets the format of the BDB represented by a registry ID type.
		 *
		 * @param format the format of the BDB
		 * @return this builder instance
		 */
		@JsonProperty("format")
		public BDBInfoBuilder withFormat(RegistryIDType format) {
			this.format = format;
			return this;
		}

		/**
		 * Sets whether the BDB is encrypted.
		 *
		 * @param encryption true if encrypted, false otherwise
		 * @return this builder instance
		 */
		@JsonProperty("encryption")
		public BDBInfoBuilder withEncryption(Boolean encryption) {
			this.encryption = encryption;
			return this;
		}

		/**
		 * Sets the creation date and time of the BDB.
		 *
		 * @param creationDate the creation date and time
		 * @return this builder instance
		 */
		@JsonProperty("creationDate")
		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		public BDBInfoBuilder withCreationDate(LocalDateTime creationDate) {
			this.creationDate = creationDate;
			return this;
		}

		/**
		 * Sets the not valid before date and time of the BDB.
		 *
		 * @param notValidBefore the not valid before date and time
		 * @return this builder instance
		 */
		@JsonProperty("notValidBefore")
		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		public BDBInfoBuilder withNotValidBefore(LocalDateTime notValidBefore) {
			this.notValidBefore = notValidBefore;
			return this;
		}

		/**
		 * Sets the not valid after date and time of the BDB.
		 *
		 * @param notValidAfter the not valid after date and time
		 * @return this builder instance
		 */
		@JsonProperty("notValidAfter")
		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		public BDBInfoBuilder withNotValidAfter(LocalDateTime notValidAfter) {
			this.notValidAfter = notValidAfter;
			return this;
		}

		/**
		 * Sets the list of biometric types associated with the BDB.
		 *
		 * @param type the list of biometric types
		 * @return this builder instance
		 */
		@JsonProperty("type")
		public BDBInfoBuilder withType(List<BiometricType> type) {
			this.type = type;
			return this;
		}

		/**
		 * Sets the list of biometric subtypes associated with the BDB.
		 *
		 * @param subtype the list of biometric subtypes
		 * @return this builder instance
		 */
		@JsonProperty("subtype")
		public BDBInfoBuilder withSubtype(List<String> subtype) {
			this.subtype = subtype;
			return this;
		}

		/**
		 * Sets the processed level type of the BDB.
		 *
		 * @param level the processed level type
		 * @return this builder instance
		 */
		@JsonProperty("level")
		public BDBInfoBuilder withLevel(ProcessedLevelType level) {
			this.level = level;
			return this;
		}

		/**
		 * Sets the product information associated with the BDB.
		 *
		 * @param product the product information
		 * @return this builder instance
		 */
		@JsonProperty("product")
		public BDBInfoBuilder withProduct(RegistryIDType product) {
			this.product = product;
			return this;
		}

		/**
		 * Sets the purpose type of the BDB.
		 *
		 * @param purpose the purpose type
		 * @return this builder instance
		 */
		@JsonProperty("purpose")
		public BDBInfoBuilder withPurpose(PurposeType purpose) {
			this.purpose = purpose;
			return this;
		}

		/**
		 * Sets the quality type associated with the BDB.
		 *
		 * @param quality the quality type
		 * @return this builder instance
		 */
		@JsonProperty("quality")
		public BDBInfoBuilder withQuality(QualityType quality) {
			this.quality = quality;
			return this;
		}

		/**
		 * Sets the capture device information associated with the BDB.
		 *
		 * @param captureDevice the capture device information
		 * @return this builder instance
		 */
		@JsonProperty("captureDevice")
		public BDBInfoBuilder withCaptureDevice(RegistryIDType captureDevice) {
			this.captureDevice = captureDevice;
			return this;
		}

		/**
		 * Sets the feature extraction algorithm associated with the BDB.
		 *
		 * @param featureExtractionAlgorithm the feature extraction algorithm
		 * @return this builder instance
		 */
		@JsonProperty("featureExtractionAlgorithm")
		public BDBInfoBuilder withFeatureExtractionAlgorithm(RegistryIDType featureExtractionAlgorithm) {
			this.featureExtractionAlgorithm = featureExtractionAlgorithm;
			return this;
		}

		/**
		 * Sets the comparison algorithm associated with the BDB.
		 *
		 * @param comparisonAlgorithm the comparison algorithm
		 * @return this builder instance
		 */
		@JsonProperty("comparisonAlgorithm")
		public BDBInfoBuilder withComparisonAlgorithm(RegistryIDType comparisonAlgorithm) {
			this.comparisonAlgorithm = comparisonAlgorithm;
			return this;
		}

		/**
		 * Sets the compression algorithm associated with the BDB.
		 *
		 * @param compressionAlgorithm the compression algorithm
		 * @return this builder instance
		 */
		@JsonProperty("compressionAlgorithm")
		public BDBInfoBuilder withCompressionAlgorithm(RegistryIDType compressionAlgorithm) {
			this.compressionAlgorithm = compressionAlgorithm;
			return this;
		}

		/**
		 * Constructs a new {@link BDBInfo} instance based on the builder's current
		 * state.
		 *
		 * @return a new {@link BDBInfo} instance
		 */
		public BDBInfo build() {
			return new BDBInfo(this);
		}
	}
}