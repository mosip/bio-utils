package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents information related to the Standard Biometric (SB) format.
 *
 * This class captures details about the format used for a Standard Biometric
 * (SB) data element. The {@link RegistryIDType} property (likely another class)
 * specifies the owner or identifier associated with the format.
 *
 * @author Ramadurai Pandian
 */
@Data
@NoArgsConstructor
@JsonDeserialize(builder = SBInfo.SBInfoBuilder.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SBInfoType", propOrder = { "format" })
public class SBInfo implements Serializable {
	/**
	 * Reference to the format type or owner associated with the SB data (e.g.,
	 * agency, standard).
	 */
	@XmlElement(name = "Format")
	private RegistryIDType format;

	public SBInfo(SBInfoBuilder sBInfoBuilder) {
		this.format = sBInfoBuilder.format;
	}

	public RegistryIDType getFormat() {
		return format;
	}

	public static class SBInfoBuilder {
		private RegistryIDType format;

		/**
		 * Sets the format owner or identifier (e.g., agency, standard) for the SB data.
		 *
		 * @param format The {@link RegistryIDType} representing the format
		 *               owner/identifier.
		 * @return This builder instance for method chaining.
		 */
		public SBInfoBuilder setFormatOwner(RegistryIDType format) {
			this.format = format;
			return this;
		}

		public SBInfo build() {
			return new SBInfo(this);
		}
	}
}