package io.mosip.kernel.biometrics.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import io.mosip.kernel.biometrics.commons.CbeffValidator;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.constant.OtherKey;
import io.mosip.kernel.biometrics.constant.ProcessedLevelType;
import io.mosip.kernel.biometrics.constant.PurposeType;
import io.mosip.kernel.biometrics.constant.QualityType;
import io.mosip.kernel.biometrics.entities.BDBInfo;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.BIRInfo;
import io.mosip.kernel.biometrics.entities.RegistryIDType;
import io.mosip.kernel.biometrics.entities.SingleAnySubtypeType;
import io.mosip.kernel.biometrics.entities.VersionType;
import io.mosip.kernel.core.cbeffutil.common.CbeffISOReader;
import io.mosip.kernel.core.cbeffutil.exception.CbeffException;

public class CbeffValidatorTest {

	private List<BIR> createList;
	private List<BIR> birList;
	private List<BIR> updateList;
	private List<BIR> exceptionList;
	private String localpath = "./src/test/resources";
	byte[] rindexFinger = null;
	byte[] rmiddleFinger = null;
	byte[] rringFinger = null;
	byte[] rlittleFinger = null;
	byte[] rightthumb = null;
	byte[] lindexFinger = null;
	byte[] lmiddleFinger = null;
	byte[] lringFinger = null;
	byte[] llittleFinger = null;
	byte[] leftthumb = null;
	byte[] iris = null;
	byte[] face = null;
	byte[] handGeo = null;

	@Before
	public void setUp() throws Exception {

		rindexFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Index.iso", "Finger");
		rmiddleFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Middle.iso",
				"Finger");
		rringFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Ring.iso",
				"Finger");
		rlittleFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Little.iso",
				"Finger");
		rightthumb = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Thumb.iso",
				"Finger");
		lindexFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Index.iso",
				"Finger");
		lmiddleFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Middle.iso",
				"Finger");
		lringFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Ring.iso", "Finger");
		llittleFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Little.iso",
				"Finger");
		leftthumb = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Thumb.iso", "Finger");
		iris = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Thumb.iso", "Finger");
		face = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Thumb.iso", "Finger");
		handGeo = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Thumb.iso", "Finger");

		RegistryIDType format = new RegistryIDType();
		format.setOrganization("257");
		format.setType("7");
		QualityType Qtype = new QualityType();
		Qtype.setScore(Long.valueOf(100));
		RegistryIDType algorithm = new RegistryIDType();
		algorithm.setOrganization("HMAC");
		algorithm.setType("SHA-256");
		Qtype.setAlgorithm(algorithm);
		createList = new ArrayList<>();
		birList = new ArrayList<>();
		BIR rIndexFinger = new BIR.BIRBuilder().withBdb(rindexFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right IndexFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(rIndexFinger);

		BIR rMiddleFinger = new BIR.BIRBuilder().withBdb(rmiddleFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("MiddleFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(rMiddleFinger);

		BIR rRingFinger = new BIR.BIRBuilder().withBdb(rringFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right RingFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(rRingFinger);

		BIR rLittleFinger = new BIR.BIRBuilder().withBdb(rlittleFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right LittleFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(rLittleFinger);

		BIR lIndexFinger = new BIR.BIRBuilder().withBdb(lindexFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left IndexFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(lIndexFinger);

		BIR lMiddleFinger = new BIR.BIRBuilder().withBdb(lmiddleFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left MiddleFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(lMiddleFinger);

		BIR lRightFinger = new BIR.BIRBuilder().withBdb(lringFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left RingFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(lRightFinger);

		BIR lLittleFinger = new BIR.BIRBuilder().withBdb(llittleFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left LittleFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(lLittleFinger);

		BIR rightThumb = new BIR.BIRBuilder().withBdb(rightthumb).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right Thumb"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(rightThumb);

		BIR leftThumb = new BIR.BIRBuilder().withBdb(leftthumb).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left Thumb"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		createList.add(leftThumb);
		
		RegistryIDType irisFormat = new RegistryIDType();
		irisFormat.setOrganization("257");
		irisFormat.setType("9");
		BIR iIris = new BIR.BIRBuilder().withBdb(iris).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(irisFormat).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.IRIS)).withSubtype(Arrays.asList("Iris"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		birList.add(iIris);
		
		RegistryIDType faceFormat = new RegistryIDType();
		faceFormat.setOrganization("257");
		faceFormat.setType("8");
		BIR iFace = new BIR.BIRBuilder().withBdb(face).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(faceFormat).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FACE)).withSubtype(Arrays.asList("Face"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		birList.add(iFace);
		
		BIR iHandGeo = new BIR.BIRBuilder().withBdb(handGeo).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(faceFormat).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.HAND_GEOMETRY)).withSubtype(Arrays.asList("Hand Geo"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers("test", "test")
				.withOthers("test2", "teste")
				.build();

		birList.add(iHandGeo);
		
		
		
	}

	@Test
	public void validateXMLTest() throws CbeffException {
		BIR bir = new BIR();
		bir.setBirs(createList);
		MatcherAssert.assertThat(CbeffValidator.validateXML(bir), is(true));
	}
	
	@Test
	public void validateXMLOtherThanFingerTest() throws CbeffException {
		BIR bir = new BIR();
		bir.setBirs(birList);
		MatcherAssert.assertThat(CbeffValidator.validateXML(bir), is(true));
	}
	
	@Test(expected = CbeffException.class)
	public void validateXMLInvalidBioTypeTest() throws CbeffException {
		BIR bir = new BIR();
		List<BIR> invalidBio =new ArrayList<>();
		RegistryIDType format = new RegistryIDType();
		format.setOrganization("257");
		format.setType("7");
		QualityType Qtype = new QualityType();
		Qtype.setScore(Long.valueOf(100));
		RegistryIDType algorithm = new RegistryIDType();
		algorithm.setOrganization("HMAC");
		algorithm.setType("SHA-256");
		Qtype.setAlgorithm(algorithm);
		BIR invalidBiometricType = new BIR.BIRBuilder().withBdb(handGeo).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.DNA)).withSubtype(Arrays.asList("Hand Geo"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();
		invalidBio.add(invalidBiometricType);
		bir.setBirs(invalidBio);
		CbeffValidator.validateXML(bir);
	}

	
	@Test(expected = CbeffException.class)
	public void validateXMLOtherExceptionTest() throws CbeffException {
		BIR bir = new BIR();
		List<BIR> invalidBio =new ArrayList<>();
		RegistryIDType format = new RegistryIDType();
		format.setOrganization("257");
		format.setType("7");
		QualityType Qtype = new QualityType();
		Qtype.setScore(Long.valueOf(100));
		RegistryIDType algorithm = new RegistryIDType();
		algorithm.setOrganization("HMAC");
		algorithm.setType("SHA-256");
		Qtype.setAlgorithm(algorithm);

		BIR invalidBiometricType = new BIR.BIRBuilder().withBdb(handGeo).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1)).withOthers(OtherKey.EXCEPTION, "true")
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.DNA)).withSubtype(Arrays.asList("Hand Geo"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();
		invalidBio.add(invalidBiometricType);
		bir.setBirs(invalidBio);
		CbeffValidator.validateXML(bir);
	}

	@Test(expected = CbeffException.class)
	public void validateXMLBIRNullTest() throws CbeffException {
		CbeffValidator.validateXML(null);
	}

	@Test(expected = CbeffException.class)
	public void validateXMLBDBNullTest() throws CbeffException {
		List<BIR> birs = new ArrayList<>(); 
		birs.add(new BIR.BIRBuilder().build());
		BIR bir = new BIR();
		bir.setBirs(birs);
		CbeffValidator.validateXML(bir);
	}

	@Test(expected = CbeffException.class)
	public void validateXMLBDBInfoNullTest() throws CbeffException {
		List<BIR> birs = new ArrayList<>(); 
		BIR nullBDBInfoBIR=new BIR.BIRBuilder().withBdb(lindexFinger).build();
		birs.add(nullBDBInfoBIR);
		BIR bir = new BIR();
		bir.setBirs(birs);
		CbeffValidator.validateXML(bir);
	}
	
	@Test(expected = CbeffException.class)
	public void validateXMLBDBInfoBiometricTypeNULLTest() throws CbeffException {
		List<BIR> birs = new ArrayList<>(); 
		BIR nullBDBInfoBIR=new BIR.BIRBuilder().withBdb(lindexFinger).withBdbInfo(new BDBInfo.BDBInfoBuilder().withType(null)
	            .withSubtype(Arrays.asList("Right MiddleFinger"))
				.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
				.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build()).build();
		birs.add(nullBDBInfoBIR);
		BIR bir = new BIR();
		bir.setBirs(birs);
		CbeffValidator.validateXML(bir);
	}
	
	@Test(expected = CbeffException.class)
	public void validateXMLBDBInfoBiometricTypeEmptyTest() throws CbeffException {
		List<BiometricType> bdbTypes = new ArrayList<>(); 
		List<BIR> birs = new ArrayList<>(); 
		BIR emptyBDBInfoBIR=new BIR.BIRBuilder().withBdb(lindexFinger).withBdbInfo(new BDBInfo.BDBInfoBuilder().withType(bdbTypes)
	            .withSubtype(Arrays.asList("Right MiddleFinger"))
				.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
				.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build()).build();
		birs.add(emptyBDBInfoBIR);
		BIR bir = new BIR();
		bir.setBirs(birs);
		CbeffValidator.validateXML(bir);
	}

	@Test(expected = CbeffException.class)
	public void validateXMLBDBInfoInvalidFormatTest() throws CbeffException {
		RegistryIDType format = new RegistryIDType();
		format.setOrganization("257");
		format.setType("1");
		List<BIR> birs = new ArrayList<>(); 
		BIR invalidFormatBDBInfoBIR=new BIR.BIRBuilder().withBdb(rindexFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right IndexFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();
		birs.add(invalidFormatBDBInfoBIR);
		BIR bir = new BIR();
		bir.setBirs(birs);
		CbeffValidator.validateXML(bir);
	}
	@Test
	public void createXMLBytesTest() throws Exception {
		BIR bir = new BIR();
		VersionType type = new VersionType(1, 1);
		BIRInfo birInfo = new BIRInfo();
		birInfo.setIntegrity(false);
		bir.setBirInfo(birInfo);
		bir.setCbeffversion(type);
		bir.setBirs(createList);
		byte[] xmlbytes = CbeffValidator.createXMLBytes(bir, readXSD("updatedcbeff"));
		MatcherAssert.assertThat(xmlbytes, isA(byte[].class));
	}
	
	@Test(expected = CbeffException.class)
	public void createXMLSAXExceptionBytesTest() throws Exception {
		BIR bir = new BIR();
		VersionType type = new VersionType(1, 1);
		BIRInfo birInfo = new BIRInfo();
		birInfo.setIntegrity(false);
		bir.setBirInfo(birInfo);
		bir.setCbeffversion(type);
		bir.setBirs(createList);
		MatcherAssert.assertThat(CbeffValidator.createXMLBytes(bir, readXSD("cbeff")), isA(byte[].class));
	}
	@Test
	public void getBIRFromXMLTest() throws Exception {
		BIR bir = CbeffValidator.getBIRFromXML(readCreatedXML("createCbeffLatest"));
		MatcherAssert.assertThat(bir.getVersion().getMajor(), is(1));
		MatcherAssert.assertThat(bir.getVersion().getMinor(), is(1));
	}

	@Test
	@SuppressWarnings({ "java:S5976" })
	public void getBDBBasedOnTypeAndSubTypeSubTypeNULLTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(createList);
		Map<String, String> bdbMap = CbeffValidator.getBDBBasedOnTypeAndSubType(bir, "Finger", null);
		MatcherAssert.assertThat(bdbMap.size(), is(10));
	}
	
	@Test
	@SuppressWarnings({ "java:S5976" })
	public void getBDBBasedOnTypeAndSubTypeSubTypeNULLWithFaceBioTypeTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(birList);
		Map<String, String> bdbMap = CbeffValidator.getBDBBasedOnTypeAndSubType(bir, "Face", null);
		MatcherAssert.assertThat(bdbMap.size(), is(1));
	}
	
	@Test
	@SuppressWarnings({ "java:S5976" })
	public void getBDBBasedOnTypeAndSubTypeSubTypeNULLWithIrisBioTypeTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(birList);
		Map<String, String> bdbMap = CbeffValidator.getBDBBasedOnTypeAndSubType(bir, "Iris", null);
		MatcherAssert.assertThat(bdbMap.size(), is(1));
	}
	
	@Test
	@SuppressWarnings({ "java:S5976" })
	public void getBDBBasedOnTypeAndSubTypeSubTypeNULLWithHandGeoBioTypeTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(birList);
		Map<String, String> bdbMap = CbeffValidator.getBDBBasedOnTypeAndSubType(bir, "Handgeometry", null);
		MatcherAssert.assertThat(bdbMap.size(), is(1));
	}
		
	@Test
	public void getBDBBasedOnTypeAndSubTypeAllNULLTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(createList);
		Map<String, String> bdbMap = CbeffValidator.getBDBBasedOnTypeAndSubType(bir, null, null);
		MatcherAssert.assertThat(bdbMap.size(), is(10));
	}
	
	@Test
	public void getBDBBasedOnTypeAndSubTypeTypeNULLTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(createList);
		Map<String, String> bdbMap = CbeffValidator.getBDBBasedOnTypeAndSubType(bir, null, "MiddleFinger");
		MatcherAssert.assertThat(bdbMap.size(), is(1));
	}
	
	@Test
	public void getBDBBasedOnTypeAndSubTypeTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(createList);
		Map<String, String> bdbMap = CbeffValidator.getBDBBasedOnTypeAndSubType(bir, "Finger", "MiddleFinger");
		MatcherAssert.assertThat(bdbMap.size(), is(1));
	}

	@Test
	public void isInEnumTest()  {
		MatcherAssert.assertThat(CbeffValidator.isInEnum("LEFT", SingleAnySubtypeType.class), is(true));
	}

	@Test
	@SuppressWarnings({ "java:S5976" })
	public void getAllBDBDataSubTypeNULLTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(createList);
		Map<String, String> bdbMap = CbeffValidator.getAllBDBData(bir, "Finger", null);
		MatcherAssert.assertThat(bdbMap.size(), is(10));
	}
	
	@Test
	@SuppressWarnings({ "java:S5976" })
	public void getAllBDBDataTypeNullTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(createList);
		Map<String, String> bdbMap = CbeffValidator.getAllBDBData(bir, null, "MiddleFinger");
		MatcherAssert.assertThat(bdbMap.size(), is(1));
	}
	
	@Test
	@SuppressWarnings({ "java:S5976" })
	public void getAllBDBDataTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(createList);
		Map<String, String> bdbMap = CbeffValidator.getAllBDBData(bir, "Finger", "MiddleFinger");
		MatcherAssert.assertThat(bdbMap.size(), is(1));
	}

	@Test
	@SuppressWarnings({ "java:S5976" })
	public void getBIRDataFromXMLTypeTest() throws Exception {
		BIR bir = new BIR();
		bir.setBirs(createList);
		List<BIR> birs = CbeffValidator.getBIRDataFromXMLType(readCreatedXML("createCbeffLatest"), "Finger");
		MatcherAssert.assertThat(birs.size(), is(3));
	}

	private byte[] readXSD(String name) throws IOException {
		byte[] fileContent = Files.readAllBytes(Paths.get(localpath + "/schema/" + name + ".xsd"));
		return fileContent;
	}

	private byte[] readCreatedXML(String name) throws IOException {
		byte[] fileContent = Files.readAllBytes(Paths.get(localpath + "/schema/" + name + ".xml"));
		return fileContent;
	}
}