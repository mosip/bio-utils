package io.mosip.kernel.biosdk.provider.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.core.cbeffutil.entity.BDBInfo;
import io.mosip.kernel.core.cbeffutil.entity.BIR;
import io.mosip.kernel.core.cbeffutil.entity.BIRInfo;
import io.mosip.kernel.core.cbeffutil.entity.BIRVersion;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.ProcessedLevelType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.PurposeType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.QualityType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.RegistryIDType;
import io.mosip.kernel.core.cbeffutil.jaxbclasses.SingleType;

/**
 * Utility class for converting between different representations of Biometric
 * Information Records (BIR).
 */
@SuppressWarnings("deprecation")
public class BIRConverter {
	private BIRConverter() {
		throw new IllegalStateException("BIRConverter class");
	}

	/**
	 * Converts a {@link BIR} object to a
	 * {@link io.mosip.kernel.biometrics.entities.BIR} object.
	 *
	 * @param bir The input {@link BIR} object to convert.
	 * @return A converted {@link io.mosip.kernel.biometrics.entities.BIR} object.
	 */
	public static io.mosip.kernel.biometrics.entities.BIR convertToBiometricRecordBIR(BIR bir) {
		List<BiometricType> bioTypes = new ArrayList<>();
		for (SingleType type : bir.getBdbInfo().getType()) {
			bioTypes.add(BiometricType.fromValue(type.value()));
		}

		io.mosip.kernel.biometrics.entities.RegistryIDType format = new io.mosip.kernel.biometrics.entities.RegistryIDType(
				bir.getBdbInfo().getFormat().getOrganization(), bir.getBdbInfo().getFormat().getType());

		io.mosip.kernel.biometrics.constant.QualityType qualityType;

		if (Objects.nonNull(bir.getBdbInfo().getQuality())) {
			io.mosip.kernel.biometrics.entities.RegistryIDType birAlgorithm = new io.mosip.kernel.biometrics.entities.RegistryIDType(
					bir.getBdbInfo().getQuality().getAlgorithm().getOrganization(),
					bir.getBdbInfo().getQuality().getAlgorithm().getType());

			qualityType = new io.mosip.kernel.biometrics.constant.QualityType();
			qualityType.setAlgorithm(birAlgorithm);
			qualityType.setQualityCalculationFailed(bir.getBdbInfo().getQuality().getQualityCalculationFailed());
			qualityType.setScore(bir.getBdbInfo().getQuality().getScore());

		} else {
			qualityType = null;
		}

		io.mosip.kernel.biometrics.entities.VersionType version;
		if (Objects.nonNull(bir.getVersion())) {
			version = new io.mosip.kernel.biometrics.entities.VersionType(bir.getVersion().getMajor(),
					bir.getVersion().getMinor());
		} else {
			version = null;
		}

		io.mosip.kernel.biometrics.entities.VersionType cbeffversion;
		if (Objects.nonNull(bir.getCbeffversion())) {
			cbeffversion = new io.mosip.kernel.biometrics.entities.VersionType(bir.getCbeffversion().getMajor(),
					bir.getCbeffversion().getMinor());
		} else {
			cbeffversion = null;
		}

		io.mosip.kernel.biometrics.constant.PurposeType purposeType;
		if (Objects.nonNull(bir.getBdbInfo().getPurpose())) {
			purposeType = io.mosip.kernel.biometrics.constant.PurposeType
					.fromValue(bir.getBdbInfo().getPurpose().name());
		} else {
			purposeType = null;
		}

		io.mosip.kernel.biometrics.constant.ProcessedLevelType processedLevelType;
		if (Objects.nonNull(bir.getBdbInfo().getLevel())) {
			processedLevelType = io.mosip.kernel.biometrics.constant.ProcessedLevelType
					.fromValue(bir.getBdbInfo().getLevel().name());
		} else {
			processedLevelType = null;
		}

		return new io.mosip.kernel.biometrics.entities.BIR.BIRBuilder().withBdb(bir.getBdb()).withVersion(version)
				.withCbeffversion(cbeffversion)
				.withBirInfo(
						new io.mosip.kernel.biometrics.entities.BIRInfo.BIRInfoBuilder().withIntegrity(true).build())
				.withBdbInfo(new io.mosip.kernel.biometrics.entities.BDBInfo.BDBInfoBuilder().withFormat(format)
						.withType(bioTypes).withQuality(qualityType)
						.withCreationDate(bir.getBdbInfo().getCreationDate()).withIndex(bir.getBdbInfo().getIndex())
						.withPurpose(purposeType).withLevel(processedLevelType)
						.withSubtype(bir.getBdbInfo().getSubtype()).build())
				.build();
	}

	/**
	 * Converts a {@link io.mosip.kernel.biometrics.entities.BIR} object to a
	 * {@link BIR} object.
	 *
	 * @param bir The input {@link io.mosip.kernel.biometrics.entities.BIR} object
	 *            to convert.
	 * @return A converted {@link BIR} object.
	 */
	public static BIR convertToBIR(io.mosip.kernel.biometrics.entities.BIR bir) {
		List<SingleType> bioTypes = new ArrayList<>();
		for (BiometricType type : bir.getBdbInfo().getType()) {
			bioTypes.add(SingleType.fromValue(type.value()));
		}

		RegistryIDType format = new RegistryIDType();
		format.setOrganization(bir.getBdbInfo().getFormat().getOrganization());
		format.setType(bir.getBdbInfo().getFormat().getType());

		RegistryIDType birAlgorithm = new RegistryIDType();
		birAlgorithm.setOrganization(bir.getBdbInfo().getQuality().getAlgorithm().getOrganization());
		birAlgorithm.setType(bir.getBdbInfo().getQuality().getAlgorithm().getType());

		QualityType qualityType = new QualityType();
		qualityType.setAlgorithm(birAlgorithm);
		qualityType.setQualityCalculationFailed(bir.getBdbInfo().getQuality().getQualityCalculationFailed());
		qualityType.setScore(bir.getBdbInfo().getQuality().getScore());

		return new BIR.BIRBuilder().withBdb(bir.getBdb())
				.withVersion(new BIRVersion.BIRVersionBuilder().withMinor(bir.getVersion().getMinor())
						.withMajor(bir.getVersion().getMajor()).build())
				.withCbeffversion(new BIRVersion.BIRVersionBuilder().withMinor(bir.getCbeffversion().getMinor())
						.withMajor(bir.getCbeffversion().getMajor()).build())
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(true).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withType(bioTypes).withQuality(qualityType)
						.withCreationDate(bir.getBdbInfo().getCreationDate()).withIndex(bir.getBdbInfo().getIndex())
						.withPurpose(PurposeType.fromValue(bir.getBdbInfo().getPurpose().name()))
						.withLevel(ProcessedLevelType.fromValue(bir.getBdbInfo().getLevel().name()))
						.withSubtype(bir.getBdbInfo().getSubtype()).build())
				.build();
	}
}