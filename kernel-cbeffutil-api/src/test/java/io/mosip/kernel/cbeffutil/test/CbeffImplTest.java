package io.mosip.kernel.cbeffutil.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import io.mosip.kernel.cbeffutil.container.impl.CbeffContainerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import io.mosip.kernel.biometrics.commons.CbeffValidator;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.constant.OtherKey;
import io.mosip.kernel.biometrics.constant.ProcessedLevelType;
import io.mosip.kernel.biometrics.constant.PurposeType;
import io.mosip.kernel.biometrics.constant.QualityType;
import io.mosip.kernel.biometrics.entities.BDBInfo;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.BIRInfo;
import io.mosip.kernel.biometrics.entities.Entry;
import io.mosip.kernel.biometrics.entities.RegistryIDType;
import io.mosip.kernel.biometrics.entities.VersionType;
import io.mosip.kernel.biometrics.spi.CbeffUtil;
import io.mosip.kernel.cbeffutil.impl.CbeffImpl;
import io.mosip.kernel.core.cbeffutil.common.CbeffISOReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ URL.class, CbeffValidator.class, io.mosip.kernel.core.cbeffutil.common.CbeffXSDValidator.class })
public class CbeffImplTest {

	@InjectMocks
	private CbeffUtil cbeffUtilImpl = new CbeffImpl();

	@Mock
	private InputStream inputStream;

	@Mock
	private URL mockURL;

	private List<BIR> createList;
	private List<BIR> updateList;
	private List<BIR> exceptionList;
	private static final String localpath = "./src/main/resources";

	@SuppressWarnings("unused")
	@Before
	public void setUp() throws Exception {
		PowerMockito.whenNew(URL.class).withArguments(Mockito.anyString()).thenReturn(mockURL);
		when(mockURL.openStream()).thenReturn(inputStream);

		byte[] rindexFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Index.iso",
				"Finger");
		byte[] rmiddleFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Middle.iso",
				"Finger");
		byte[] rringFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Ring.iso",
				"Finger");
		byte[] rlittleFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Little.iso",
				"Finger");
		byte[] rightthumb = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintRight_Thumb.iso",
				"Finger");
		byte[] lindexFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Index.iso",
				"Finger");
		byte[] lmiddleFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Middle.iso",
				"Finger");
		byte[] lringFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Ring.iso", "Finger");
		byte[] llittleFinger = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Little.iso",
				"Finger");
		byte[] leftthumb = CbeffISOReader.readISOImage(localpath + "/images/" + "FingerPrintLeft_Thumb.iso", "Finger");

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
		BIR rIndexFinger = new BIR.BIRBuilder().withBdb(rindexFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right IndexFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(rIndexFinger);

		BIR rMiddleFinger = new BIR.BIRBuilder().withBdb(rmiddleFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right MiddleFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(rMiddleFinger);

		BIR rRingFinger = new BIR.BIRBuilder().withBdb(rringFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right RingFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(rRingFinger);

		BIR rLittleFinger = new BIR.BIRBuilder().withBdb(rlittleFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right LittleFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(rLittleFinger);

		BIR lIndexFinger = new BIR.BIRBuilder().withBdb(lindexFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left IndexFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(lIndexFinger);

		BIR lMiddleFinger = new BIR.BIRBuilder().withBdb(lmiddleFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left MiddleFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(lMiddleFinger);

		BIR lRightFinger = new BIR.BIRBuilder().withBdb(lringFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left RingFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(lRightFinger);

		BIR lLittleFinger = new BIR.BIRBuilder().withBdb(llittleFinger).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left LittleFinger"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(lLittleFinger);

		BIR rightThumb = new BIR.BIRBuilder().withBdb(rightthumb).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Right Thumb"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(rightThumb);

		BIR leftThumb = new BIR.BIRBuilder().withBdb(leftthumb).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left Thumb"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		createList.add(leftThumb);

		exceptionList = new ArrayList<>();
		BIR validThumb = new BIR.BIRBuilder().withBdb(leftthumb).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left Thumb"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.build();

		Entry entryInfo = new Entry(OtherKey.EXCEPTION, "true");
		BIR exceptionThumb = new BIR.BIRBuilder().withBdb(new byte[0]).withVersion(new VersionType(1, 1))
				.withCbeffversion(new VersionType(1, 1))
				.withBirInfo(new BIRInfo.BIRInfoBuilder().withIntegrity(false).build())
				.withBdbInfo(new BDBInfo.BDBInfoBuilder().withFormat(format).withQuality(Qtype)
						.withType(Arrays.asList(BiometricType.FINGER)).withSubtype(Arrays.asList("Left Thumb"))
						.withPurpose(PurposeType.ENROLL).withLevel(ProcessedLevelType.RAW)
						.withCreationDate(LocalDateTime.now(ZoneId.of("UTC"))).build())
				.withOthers(OtherKey.EXCEPTION, "true").build();
		exceptionList.add(validThumb);
		exceptionList.add(exceptionThumb);
	}

//	@Test
	public void testCreateXML() throws Exception {
		byte[] createXml = cbeffUtilImpl.createXML(createList);
		createXMLFile(createXml, "createCbeffLatest");
		assertEquals(new String(createXml), new String(readCreatedXML("createCbeffLatest")));
	}

	@Test
	public void testCreateXMLFromLocal() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		when(CbeffValidator.createXMLBytes(any(), any())).thenReturn(readCreatedXML("createCbeffLatest2"));
		byte[] createXml = cbeffUtilImpl.createXML(createList, readXSD("updatedcbeff"));
		createXMLFile(createXml, "createCbeffLatest2");
		assertEquals(new String(createXml), new String(readCreatedXML("createCbeffLatest2")));
	}

	@Test
	public void testCreateExceptionXMLFromLocal() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		byte[] result = cbeffUtilImpl.createXML(exceptionList, readXSD("cbeff"));
		assertArrayEquals(null, result);
	}

	private byte[] readCreatedXML(String name) throws IOException {
		byte[] fileContent = Files.readAllBytes(Paths.get(localpath + "/schema/" + name + ".xml"));
		return fileContent;
	}

	private byte[] readXSD(String name) throws IOException {
		byte[] fileContent = Files.readAllBytes(Paths.get(localpath + "/schema/" + name + ".xsd"));
		return fileContent;
	}

	private static void createXMLFile(byte[] updatedXmlBytes, String name) throws Exception {
		File tempFile = new File(localpath + "/schema/" + name + ".xml");
		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(updatedXmlBytes);
		fos.close();
	}

	// @Test
	public void testUpdateXML() throws Exception {
		byte[] updateXml = cbeffUtilImpl.updateXML(updateList, readCreatedXML("createCbeff"));
		createXMLFile(updateXml, "updateCbeff");
		assertEquals(new String(updateXml), new String(readCreatedXML("updateCbeff")));
	}

	@SuppressWarnings("unused")
	private byte[] readbytesFromStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		// we need to know how may bytes were read to write them to the byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		// and then we can return your byte array.
		return byteBuffer.toByteArray();

	}

	/**
	 * Test validateXML method with single parameter using internal XSD schema.
	 * Verifies that XML validation returns true when valid XML is provided.
	 */
	@Test
	public void validateXmlWithSingleParameterReturnsTrue() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		PowerMockito.mockStatic(io.mosip.kernel.core.cbeffutil.common.CbeffXSDValidator.class);

		byte[] xmlBytes = "<xml>test</xml>".getBytes();
		when(io.mosip.kernel.core.cbeffutil.common.CbeffXSDValidator.validateXML(any(), any())).thenReturn(true);

		boolean result = cbeffUtilImpl.validateXML(xmlBytes);

		assertTrue(result);
	}

	/**
	 * Test validateXML method with two parameters using provided XSD schema.
	 * Verifies that XML validation returns true when valid XML and XSD are provided.
	 */
	@Test
	public void validateXmlWithTwoParametersReturnsTrue() throws Exception {
		PowerMockito.mockStatic(io.mosip.kernel.core.cbeffutil.common.CbeffXSDValidator.class);

		byte[] xmlBytes = "<xml>test</xml>".getBytes();
		byte[] xsdBytes = "<xsd>schema</xsd>".getBytes();

		when(io.mosip.kernel.core.cbeffutil.common.CbeffXSDValidator.validateXML(xsdBytes, xmlBytes)).thenReturn(true);

		boolean result = cbeffUtilImpl.validateXML(xmlBytes, xsdBytes);

		assertTrue(result);
	}

	/**
	 * Test getBDBBasedOnType method with valid type and subtype.
	 * Verifies that BDB data is retrieved correctly based on biometric type and subtype.
	 */
	@Test
	public void getBdbBasedOnTypeWithValidTypeAndSubtypeReturnsExpectedMap() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		BIR mockBIR = new BIR();
		Map<String, String> expectedMap = new HashMap<>();
		expectedMap.put("bdb", "testData");

		when(CbeffValidator.getBIRFromXML(any())).thenReturn(mockBIR);
		when(CbeffValidator.getBDBBasedOnTypeAndSubType(mockBIR, "FINGER", "Left Thumb")).thenReturn(expectedMap);

		Map<String, String> result = cbeffUtilImpl.getBDBBasedOnType(readCreatedXML("createCbeffLatest2"), "FINGER", "Left Thumb");

		assertEquals(expectedMap, result);
	}

	/**
	 * Test getBIRDataFromXML method with valid XML bytes.
	 * Verifies that BIR list is extracted correctly from XML data.
	 */
	@Test
	public void getBirDataFromXmlWithValidXmlReturnsBirList() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		BIR mockBIR = new BIR();
		mockBIR.setBirs(createList);

		when(CbeffValidator.getBIRFromXML(any())).thenReturn(mockBIR);

		List<BIR> result = cbeffUtilImpl.getBIRDataFromXML(readCreatedXML("createCbeffLatest2"));

		assertEquals(createList, result);
	}

	/**
	 * Test getAllBDBData method with valid type and subtype.
	 * Verifies that all BDB data is retrieved correctly based on biometric type and subtype.
	 */
	@Test
	public void getAllBdbDataWithValidTypeAndSubtypeReturnsAllBdbMap() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		BIR mockBIR = new BIR();
		Map<String, String> expectedMap = new HashMap<>();
		expectedMap.put("allBDB", "allTestData");

		when(CbeffValidator.getBIRFromXML(any())).thenReturn(mockBIR);
		when(CbeffValidator.getAllBDBData(mockBIR, "FINGER", "Left Thumb")).thenReturn(expectedMap);

		Map<String, String> result = cbeffUtilImpl.getAllBDBData(readCreatedXML("createCbeffLatest2"), "FINGER", "Left Thumb");

		assertEquals(expectedMap, result);
	}

	/**
	 * Test getBIRDataFromXMLType method with valid biometric type.
	 * Verifies that BIR list is filtered correctly based on biometric type.
	 */
	@Test
	public void getBirDataFromXmlTypeWithValidTypeReturnsBirList() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		when(CbeffValidator.getBIRDataFromXMLType(any(), eq("FINGER"))).thenReturn(createList);

		List<BIR> result = cbeffUtilImpl.getBIRDataFromXMLType(readCreatedXML("createCbeffLatest2"), "FINGER");

		assertEquals(createList, result);
	}

	/**
	 * Test getBDBBasedOnType method with invalid XML data.
	 * Verifies that exception is thrown when XML parsing fails.
	 */
	@Test(expected = Exception.class)
	public void getBdbBasedOnTypeWithInvalidXmlThrowsException() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		when(CbeffValidator.getBIRFromXML(any())).thenThrow(new Exception("XML parsing error"));

		cbeffUtilImpl.getBDBBasedOnType(new byte[0], "FINGER", "Left Thumb");
	}

	/**
	 * Test getBIRDataFromXML method with invalid XML data.
	 * Verifies that exception is thrown when XML parsing fails.
	 */
	@Test(expected = Exception.class)
	public void getBirDataFromXmlWithInvalidXmlThrowsException() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		when(CbeffValidator.getBIRFromXML(any())).thenThrow(new Exception("XML parsing error"));

		cbeffUtilImpl.getBIRDataFromXML(new byte[0]);
	}

	/**
	 * Test updateBIRType method with valid BIR list and XML data.
	 * Verifies that BIR list is added to existing biometric record successfully.
	 */
	@Test
	public void updateBirTypeWithValidBirListAndXmlReturnsUpdatedBir() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		BIR mockBIR = new BIR();
		mockBIR.setBirs(new ArrayList<>());

		when(CbeffValidator.getBIRFromXML(any())).thenReturn(mockBIR);

		CbeffContainerImpl container = new CbeffContainerImpl();
		BIR result = container.updateBIRType(createList, "<xml>test</xml>".getBytes());

		assertEquals(createList.size(), result.getBirs().size());
	}

	/**
	 * Test updateBIRType method with empty BIR list.
	 * Verifies that no BIRs are added when empty list is provided.
	 */
	@Test
	public void updateBirTypeWithEmptyBirListReturnsUnchangedBir() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		BIR mockBIR = new BIR();
		mockBIR.setBirs(new ArrayList<>());

		when(CbeffValidator.getBIRFromXML(any())).thenReturn(mockBIR);

		CbeffContainerImpl container = new CbeffContainerImpl();
		BIR result = container.updateBIRType(new ArrayList<>(), "<xml>test</xml>".getBytes());

		assertEquals(0, result.getBirs().size());
	}

	/**
	 * Test updateBIRType method with invalid XML data.
	 * Verifies that exception is thrown when XML parsing fails during update.
	 */
	@Test(expected = Exception.class)
	public void updateBirTypeWithInvalidXmlThrowsException() throws Exception {
		PowerMockito.mockStatic(CbeffValidator.class);
		when(CbeffValidator.getBIRFromXML(any())).thenThrow(new Exception("XML error"));

		CbeffContainerImpl container = new CbeffContainerImpl();
		container.updateBIRType(createList, new byte[0]);
	}
}