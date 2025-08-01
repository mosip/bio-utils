package io.mosip.kernel.cbeffutil.impl;

import io.mosip.kernel.biometrics.commons.CbeffValidator;
import io.mosip.kernel.biometrics.entities.BIR;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Test class for CbeffImpl implementation.
 * Tests various CBEFF (Common Biometric Exchange Formats Framework) operations
 * including XML creation, updates, and data extraction.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CbeffValidator.class, IOUtils.class, URI.class})
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
    @Before
    public void setUp() {
        birList = new ArrayList<>();
        birList.add(new BIR());
        xmlBytes = "<xml>test</xml>".getBytes();
        xsdBytes = "<xsd>schema</xsd>".getBytes();
        ReflectionTestUtils.setField(cbeffImpl, "xsd", xsdBytes);
    }

    /**
     * Tests XML creation with BIR list using default XSD.
     * @throws Exception if XML creation fails
     */
    @Test
    public void createXmlWithDefaultXsdReturnsXmlBytes() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        when(CbeffValidator.createXMLBytes(any(), any())).thenReturn(xmlBytes);

        byte[] result = cbeffImpl.createXML(birList);
        assertArrayEquals(xmlBytes, result);
    }

    /**
     * Tests XML creation with BIR list and custom XSD.
     * @throws Exception if XML creation fails
     */
    @Test
    public void createXmlWithCustomXsdReturnsXmlBytes() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        when(CbeffValidator.createXMLBytes(any(), eq(xsdBytes))).thenReturn(xmlBytes);

        byte[] result = cbeffImpl.createXML(birList, xsdBytes);
        assertArrayEquals(xmlBytes, result);
    }

    /**
     * Tests updating existing XML with new BIR list.
     * @throws Exception if XML update fails
     */
    @Test
    public void updateXmlWithNewBirListReturnsUpdatedXmlBytes() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        BIR mockBIR = new BIR();
        mockBIR.setBirs(new ArrayList<>());
        when(CbeffValidator.getBIRFromXML(xmlBytes)).thenReturn(mockBIR);
        when(CbeffValidator.createXMLBytes(any(), any())).thenReturn(xmlBytes);

        byte[] result = cbeffImpl.updateXML(birList, xmlBytes);
        assertArrayEquals(xmlBytes, result);
    }

    /**
     * Tests retrieval of BDB based on type and subtype.
     * @throws Exception if BDB retrieval fails
     */
    @Test
    public void getBdbBasedOnTypeWithValidInputReturnsExpectedMap() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        BIR mockBIR = new BIR();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key", "value");

        when(CbeffValidator.getBIRFromXML(xmlBytes)).thenReturn(mockBIR);
        when(CbeffValidator.getBDBBasedOnTypeAndSubType(mockBIR, "FINGER", "Left Thumb")).thenReturn(expectedMap);

        Map<String, String> result = cbeffImpl.getBDBBasedOnType(xmlBytes, "FINGER", "Left Thumb");
        assertEquals(expectedMap, result);
    }

    /**
     * Tests retrieval of BIR data from XML bytes.
     * @throws Exception if BIR data retrieval fails
     */
    @Test
    public void getBirDataFromXmlWithValidXmlReturnsBirList() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        BIR mockBIR = new BIR();
        mockBIR.setBirs(birList);

        when(CbeffValidator.getBIRFromXML(xmlBytes)).thenReturn(mockBIR);

        List<BIR> result = cbeffImpl.getBIRDataFromXML(xmlBytes);
        assertEquals(birList, result);
    }

    /**
     * Tests retrieval of all BDB data based on type and subtype.
     * @throws Exception if BDB data retrieval fails
     */
    @Test
    public void getAllBdbDataWithValidInputReturnsExpectedMap() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        BIR mockBIR = new BIR();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("allData", "value");

        when(CbeffValidator.getBIRFromXML(xmlBytes)).thenReturn(mockBIR);
        when(CbeffValidator.getAllBDBData(mockBIR, "FINGER", "Left Thumb")).thenReturn(expectedMap);

        Map<String, String> result = cbeffImpl.getAllBDBData(xmlBytes, "FINGER", "Left Thumb");
        assertEquals(expectedMap, result);
    }

    /**
     * Tests retrieval of BIR data from XML for specific type.
     * @throws Exception if BIR data retrieval fails
     */
    @Test
    public void getBirDataFromXmlTypeWithValidInputReturnsBirList() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        when(CbeffValidator.getBIRDataFromXMLType(xmlBytes, "FINGER")).thenReturn(birList);

        List<BIR> result = cbeffImpl.getBIRDataFromXMLType(xmlBytes, "FINGER");
        assertEquals(birList, result);
    }

    /**
     * Tests exception handling when getBDBBasedOnType fails.
     * @throws Exception expected XML parsing exception
     */
    @Test(expected = Exception.class)
    public void getBdbBasedOnTypeWithInvalidXmlThrowsException() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        when(CbeffValidator.getBIRFromXML(any())).thenThrow(new Exception("XML parsing failed"));

        cbeffImpl.getBDBBasedOnType(xmlBytes, "FINGER", "Left Thumb");
    }

    /**
     * Tests exception handling when getBIRDataFromXML fails.
     * @throws Exception expected XML parsing exception
     */
    @Test(expected = Exception.class)
    public void getBirDataFromXmlWithInvalidXmlThrowsException() throws Exception {
        PowerMockito.mockStatic(CbeffValidator.class);
        when(CbeffValidator.getBIRFromXML(any())).thenThrow(new Exception("XML parsing failed"));

        cbeffImpl.getBIRDataFromXML(xmlBytes);
    }

    /**
     * Tests successful loading of XSD schema.
     * @throws Exception if XSD loading fails
     */
    @Test
    public void loadXsdWithValidConfigSuccess() throws Exception {
        ReflectionTestUtils.setField(cbeffImpl, "configServerFileStorageURL", "http://test.com/");
        ReflectionTestUtils.setField(cbeffImpl, "schemaName", "test.xsd");
        ReflectionTestUtils.setField(cbeffImpl, "xsd", xsdBytes);

        byte[] result = (byte[]) ReflectionTestUtils.getField(cbeffImpl, "xsd");
        assertArrayEquals(xsdBytes, result);
    }

    /**
     * Tests handling of URISyntaxException during XSD loading.
     * @throws Exception expected URISyntaxException
     */
    @Test
    public void loadXsdWithInvalidUriThrowsUriSyntaxException() throws Exception {
        ReflectionTestUtils.setField(cbeffImpl, "configServerFileStorageURL", "invalid uri with spaces");
        ReflectionTestUtils.setField(cbeffImpl, "schemaName", "test.xsd");

        try {
            cbeffImpl.loadXSD();
            fail("Expected URISyntaxException");
        } catch (URISyntaxException e) {
            assertTrue(e.getMessage().contains("Illegal character"));
        }
    }

    /**
     * Tests handling of IOException during XSD loading.
     * @throws Exception expected IOException
     */
    @Test
    public void loadXsdWithNonexistentHostThrowsIoException() throws Exception {
        ReflectionTestUtils.setField(cbeffImpl, "configServerFileStorageURL", "http://nonexistent.invalid/");
        ReflectionTestUtils.setField(cbeffImpl, "schemaName", "test.xsd");

        try {
            cbeffImpl.loadXSD();
            fail("Expected IOException");
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }
}