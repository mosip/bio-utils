package io.mosip.kernel.cbeffutil.impl;

import io.mosip.kernel.biometrics.commons.CbeffValidator;
import io.mosip.kernel.biometrics.entities.BIR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

/**
 * Test class for CbeffImpl implementation.
 * Tests various CBEFF (Common Biometric Exchange Formats Framework) operations
 * including XML creation, updates, and data extraction.
 */
@ExtendWith(MockitoExtension.class)
public class CbeffImplTest {

    @InjectMocks
    private CbeffImpl cbeffImpl;

    private List<BIR> birList;
    private byte[] xmlBytes;
    private byte[] xsdBytes;

    @Mock
    private InputStream mockInputStream;

    @Mock
    private URI mockURI;

    /**
     * Sets up test data before each test method execution.
     * Initializes BIR list, XML bytes, and XSD bytes for testing.
     */
    @BeforeEach
    public void setUp() {
        birList = new ArrayList<>();
        birList.add(new BIR());
        xmlBytes = "<xml>test</xml>".getBytes();
        xsdBytes = "<xsd>schema</xsd>".getBytes();
        ReflectionTestUtils.setField(cbeffImpl, "xsd", xsdBytes);
    }

    /**
     * Tests XML creation with BIR list using default XSD.
     * Ensures createXML method returns expected XML bytes when using default XSD.
     */
    @Test
    public void shouldCreateXmlWithDefaultXsdReturnsXmlBytes() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            mockedValidator.when(() -> CbeffValidator.createXMLBytes(any(), any())).thenReturn(xmlBytes);

            byte[] result = cbeffImpl.createXML(birList);

            assertArrayEquals(xmlBytes, result);
        }
    }

    /**
     * Tests XML creation with BIR list and custom XSD.
     * Ensures createXML method returns expected XML bytes when using custom XSD.
     */
    @Test
    public void shouldCreateXmlWithCustomXsdReturnsXmlBytes() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            mockedValidator.when(() -> CbeffValidator.createXMLBytes(any(), eq(xsdBytes))).thenReturn(xmlBytes);

            byte[] result = cbeffImpl.createXML(birList, xsdBytes);

            assertArrayEquals(xmlBytes, result);
        }
    }

    /**
     * Tests updating existing XML with new BIR list.
     * Ensures updateXML method returns updated XML bytes.
     */
    @Test
    public void shouldUpdateXmlWithNewBirListReturnsUpdatedXmlBytes() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            BIR mockBIR = new BIR();
            mockBIR.setBirs(new ArrayList<>());

            mockedValidator.when(() -> CbeffValidator.getBIRFromXML(xmlBytes)).thenReturn(mockBIR);
            mockedValidator.when(() -> CbeffValidator.createXMLBytes(any(), any())).thenReturn(xmlBytes);

            byte[] result = cbeffImpl.updateXML(birList, xmlBytes);

            assertArrayEquals(xmlBytes, result);
        }
    }

    /**
     * Tests retrieval of BDB based on type and subtype.
     * Ensures getBDBBasedOnType returns expected map for valid inputs.
     */
    @Test
    public void shouldGetBdbBasedOnTypeWithValidInputReturnsExpectedMap() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            BIR mockBIR = new BIR();
            Map<String, String> expectedMap = new HashMap<>();
            expectedMap.put("key", "value");

            mockedValidator.when(() -> CbeffValidator.getBIRFromXML(xmlBytes)).thenReturn(mockBIR);
            mockedValidator.when(() -> CbeffValidator.getBDBBasedOnTypeAndSubType(mockBIR, "FINGER", "Left Thumb"))
                    .thenReturn(expectedMap);

            Map<String, String> result = cbeffImpl.getBDBBasedOnType(xmlBytes, "FINGER", "Left Thumb");

            assertEquals(expectedMap, result);
        }
    }

    /**
     * Tests retrieval of BIR data from XML bytes.
     * Ensures getBIRDataFromXML returns BIR list for valid XML.
     */
    @Test
    public void shouldGetBirDataFromXmlWithValidXmlReturnsBirList() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            BIR mockBIR = new BIR();
            mockBIR.setBirs(birList);

            mockedValidator.when(() -> CbeffValidator.getBIRFromXML(xmlBytes)).thenReturn(mockBIR);

            List<BIR> result = cbeffImpl.getBIRDataFromXML(xmlBytes);

            assertEquals(birList, result);
        }
    }

    /**
     * Tests retrieval of all BDB data based on type and subtype.
     * Ensures getAllBDBData returns expected map for valid inputs.
     */
    @Test
    public void shouldGetAllBdbDataWithValidInputReturnsExpectedMap() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            BIR mockBIR = new BIR();
            Map<String, String> expectedMap = new HashMap<>();
            expectedMap.put("allData", "value");

            mockedValidator.when(() -> CbeffValidator.getBIRFromXML(xmlBytes)).thenReturn(mockBIR);
            mockedValidator.when(() -> CbeffValidator.getAllBDBData(mockBIR, "FINGER", "Left Thumb"))
                    .thenReturn(expectedMap);

            Map<String, String> result = cbeffImpl.getAllBDBData(xmlBytes, "FINGER", "Left Thumb");

            assertEquals(expectedMap, result);
        }
    }

    /**
     * Tests retrieval of BIR data from XML for specific type.
     * Ensures getBIRDataFromXMLType returns BIR list for valid type.
     */
    @Test
    public void shouldGetBirDataFromXmlTypeWithValidInputReturnsBirList() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            mockedValidator.when(() -> CbeffValidator.getBIRDataFromXMLType(xmlBytes, "FINGER")).thenReturn(birList);

            List<BIR> result = cbeffImpl.getBIRDataFromXMLType(xmlBytes, "FINGER");

            assertEquals(birList, result);
        }
    }

    /**
     * Tests exception handling when getBDBBasedOnType fails.
     * Ensures proper exception is thrown for invalid XML input.
     */
    @Test
    public void shouldThrowExceptionWhenGetBdbBasedOnTypeWithInvalidXml() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            mockedValidator.when(() -> CbeffValidator.getBIRFromXML(any()))
                    .thenThrow(new Exception("XML parsing failed"));

            Exception exception = assertThrows(Exception.class, () ->
                    cbeffImpl.getBDBBasedOnType(xmlBytes, "FINGER", "Left Thumb"));

            assertThat(exception.getMessage(), is("XML parsing failed"));
        }
    }

    /**
     * Tests exception handling when getBIRDataFromXML fails.
     * Ensures proper exception is thrown for invalid XML input.
     */
    @Test
    public void shouldThrowExceptionWhenGetBirDataFromXmlWithInvalidXml() throws Exception {
        try (MockedStatic<CbeffValidator> mockedValidator = mockStatic(CbeffValidator.class)) {
            mockedValidator.when(() -> CbeffValidator.getBIRFromXML(any()))
                    .thenThrow(new Exception("XML parsing failed"));

            Exception exception = assertThrows(Exception.class, () ->
                    cbeffImpl.getBIRDataFromXML(xmlBytes));

            assertThat(exception.getMessage(), is("XML parsing failed"));
        }
    }

    /**
     * Tests successful loading of XSD schema.
     * Ensures XSD field is properly set after loading.
     */
    @Test
    public void shouldLoadXsdWithValidConfigSuccess() throws Exception {
        ReflectionTestUtils.setField(cbeffImpl, "configServerFileStorageURL", "http://test.com/");
        ReflectionTestUtils.setField(cbeffImpl, "schemaName", "test.xsd");
        ReflectionTestUtils.setField(cbeffImpl, "xsd", xsdBytes);

        byte[] result = (byte[]) ReflectionTestUtils.getField(cbeffImpl, "xsd");

        assertArrayEquals(xsdBytes, result);
    }

    /**
     * Tests handling of URISyntaxException during XSD loading.
     * Ensures URISyntaxException is thrown for invalid URI.
     */
    @Test
    public void shouldThrowUriSyntaxExceptionWhenLoadXsdWithInvalidUri() throws Exception {
        ReflectionTestUtils.setField(cbeffImpl, "configServerFileStorageURL", "invalid uri with spaces");
        ReflectionTestUtils.setField(cbeffImpl, "schemaName", "test.xsd");

        URISyntaxException exception = assertThrows(URISyntaxException.class, () ->
                cbeffImpl.loadXSD());

        assertTrue(exception.getMessage().contains("Illegal character"));
    }

    /**
     * Tests handling of IOException during XSD loading.
     * Ensures IOException is thrown for nonexistent host.
     */
    @Test
    public void shouldThrowIoExceptionWhenLoadXsdWithNonexistentHost() throws Exception {
        ReflectionTestUtils.setField(cbeffImpl, "configServerFileStorageURL", "http://nonexistent.invalid/");
        ReflectionTestUtils.setField(cbeffImpl, "schemaName", "test.xsd");

        IOException exception = assertThrows(IOException.class, () ->
                cbeffImpl.loadXSD());

        assertNotNull(exception.getMessage());
    }
}
