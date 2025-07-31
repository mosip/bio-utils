/**
 * 
 */
package io.mosip.kernel.biometrics.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.mosip.kernel.core.cbeffutil.common.DateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents Biometric Information Record (BIR) information.
 * <p>
 * This class encapsulates details about a biometric record including its
 * creator, index, payload, integrity status, creation date, validity period
 * (not valid before and not valid after).
 * </p>
 * <p>
 * It supports XML and JSON serialization, using JAXB and Jackson annotations.
 * </p>
 * <p>
 * This class also provides a builder pattern {@link BIRInfo.BIRInfoBuilder} for
 * convenient object construction.
 * </p>
 *
 * @author Ramadurai Pandian
 * @since 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BIRInfoType", propOrder = { "creator", "index", "payload", "integrity", "creationDate",
		"notValidBefore", "notValidAfter" })
@Data
@NoArgsConstructor
@JsonDeserialize(builder = BIRInfo.BIRInfoBuilder.class)
public class BIRInfo implements Serializable {
	private static final long serialVersionUID = -2466414332099574792L;
	@XmlElement(name = "Creator")
	private String creator;
	@XmlElement(name = "Index")
	private String index;
	@XmlElement(name = "Payload")
	private byte[] payload;
	@XmlElement(name = "Integrity")
	private Boolean integrity;
	@XmlElement(name = "CreationDate")
	@XmlSchemaType(name = "dateTime")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDateTime creationDate;
	@XmlElement(name = "NotValidBefore")
	@XmlSchemaType(name = "dateTime")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDateTime notValidBefore;
	@XmlElement(name = "NotValidAfter")
	@XmlSchemaType(name = "dateTime")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDateTime notValidAfter;

	/**
	 * Constructs a new instance of BIRInfo using a builder pattern.
	 * 
	 * @param bIRInfoBuilder the builder used for constructing this BIRInfo object
	 */
	public BIRInfo(BIRInfoBuilder bIRInfoBuilder) {
		this.creator = bIRInfoBuilder.creator;
		this.index = bIRInfoBuilder.index;
		this.payload = bIRInfoBuilder.payload;
		this.integrity = bIRInfoBuilder.integrity;
		this.creationDate = bIRInfoBuilder.creationDate;
		this.notValidBefore = bIRInfoBuilder.notValidBefore;
		this.notValidAfter = bIRInfoBuilder.notValidAfter;
	}

	/**
	 * Builder pattern class for constructing instances of BIRInfo.
	 */
	@JsonPOJOBuilder(withPrefix = "with")
	public static class BIRInfoBuilder {
		private String creator;
		private String index;
		private byte[] payload;
		private Boolean integrity;
		private LocalDateTime creationDate;
		private LocalDateTime notValidBefore;
		private LocalDateTime notValidAfter;

		public BIRInfoBuilder(){
		}
		/**
		 * Sets the creator of the BIR.
		 * 
		 * @param creator the creator of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("creator")
		public BIRInfoBuilder withCreator(String creator) {
			this.creator = creator;
			return this;
		}

		/**
		 * Sets the index of the BIR.
		 * 
		 * @param index the index of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("index")
		public BIRInfoBuilder withIndex(String index) {
			this.index = index;
			return this;
		}

		/**
		 * Sets the payload (data) of the BIR.
		 * 
		 * @param payload the payload (data) of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("payload")
		@JsonDeserialize(using = IntArrayToByteArrayDeserializer.class)
		@JsonSerialize(using = ByteArrayToIntArraySerializer.class)
		public BIRInfoBuilder withPayload(byte[] payload) {
			this.payload = payload;
			return this;
		}

		/**
		 * Sets the integrity status of the BIR.
		 * 
		 * @param integrity the integrity status of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("integrity")
		public BIRInfoBuilder withIntegrity(Boolean integrity) {
			this.integrity = integrity;
			return this;
		}

		/**
		 * Sets the creation date of the BIR.
		 * 
		 * @param creationDate the creation date of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("creationDate")
		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		public BIRInfoBuilder withCreationDate(LocalDateTime creationDate) {
			this.creationDate = creationDate;
			return this;
		}

		/**
		 * Sets the start date of validity of the BIR.
		 * 
		 * @param notValidBefore the start date of validity of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("notValidBefore")
		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		public BIRInfoBuilder withNotValidBefore(LocalDateTime notValidBefore) {
			this.notValidBefore = notValidBefore;
			return this;
		}

		/**
		 * Sets the end date of validity of the BIR.
		 * 
		 * @param notValidAfter the end date of validity of the BIR
		 * @return this builder instance
		 */
		@JsonProperty("notValidAfter")
		@JsonDeserialize(using = DateTimeObjectToLocalDateTimeDeserializer.class)
		@JsonSerialize(using = LocalDateTimeToDateTimeObjectSerializer.class)
		public BIRInfoBuilder withNotValidAfter(LocalDateTime notValidAfter) {
			this.notValidAfter = notValidAfter;
			return this;
		}

		/**
		 * Builds and returns a new instance of BIRInfo using this builder.
		 * 
		 * @return a new instance of BIRInfo
		 */
		public BIRInfo build() {
			return new BIRInfo(this);
		}
	}
}