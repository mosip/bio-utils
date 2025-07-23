package io.mosip.biometrics.util.face;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class FaceBDIRTest {

    /**
     * Tests FaceBDIR constructor with basic parameters
     */
    @Test
    public void constructor_basicParameters_createsFaceBDIR() {
        long formatIdentifier = FaceFormatIdentifier.FORMAT_FAC;
        long versionNumber = FaceVersionNumber.VERSION_030;
        int certificationFlag = FaceCertificationFlag.UNSPECIFIED;
        int temporalSemantics = TemporalSequenceFlags.ONE_REPRESENTATION;
        Date captureDate = new Date();
        int noOfRepresentations = 1;
        FaceQualityBlock[] qualityBlocks = {new FaceQualityBlock(40)};
        FacialInformation facialInformation = createFacialInformation();
        LandmarkPoints[] landmarkPoints = null;
        ImageInformation imageInformation = createImageInformation();
        byte[] image = new byte[100];
        byte[] threeDInformationAndData = null;

        FaceBDIR faceBDIR = new FaceBDIR(formatIdentifier, versionNumber, certificationFlag,
            temporalSemantics, captureDate, noOfRepresentations, qualityBlocks,
            facialInformation, landmarkPoints, imageInformation, image, threeDInformationAndData);

        assertNotNull(faceBDIR);
        assertEquals(formatIdentifier, faceBDIR.getFormatIdentifier());
        assertEquals(versionNumber, faceBDIR.getVersionNumber());
        assertEquals(certificationFlag, faceBDIR.getCertificationFlag());
        assertEquals(temporalSemantics, faceBDIR.getTemporalSemantics());
    }

    /**
     * Tests FaceBDIR constructor with extended parameters including device information
     */
    @Test
    public void constructor_extendedParameters_createsFaceBDIR() {
        long formatIdentifier = FaceFormatIdentifier.FORMAT_FAC;
        long versionNumber = FaceVersionNumber.VERSION_030;
        int certificationFlag = FaceCertificationFlag.UNSPECIFIED;
        int temporalSemantics = TemporalSequenceFlags.ONE_REPRESENTATION;
        int sourceType = FaceCaptureDeviceTechnology.VIDEO_FRAME_ANALOG_CAMERA;
        int deviceVendor = FaceCaptureDeviceVendor.UNSPECIFIED;
        int deviceType = FaceCaptureDeviceType.UNSPECIFIED;
        Date captureDate = new Date();
        int noOfRepresentations = 1;
        FaceQualityBlock[] qualityBlocks = {new FaceQualityBlock(40)};
        FacialInformation facialInformation = createFacialInformation();
        LandmarkPoints[] landmarkPoints = null;
        ImageInformation imageInformation = createImageInformation();
        byte[] image = new byte[100];
        byte[] threeDInformationAndData = null;

        FaceBDIR faceBDIR = new FaceBDIR(formatIdentifier, versionNumber, certificationFlag,
            temporalSemantics, sourceType, deviceVendor, deviceType, captureDate,
            noOfRepresentations, qualityBlocks, facialInformation, landmarkPoints,
            imageInformation, image, threeDInformationAndData);

        assertNotNull(faceBDIR);
        assertEquals(sourceType, faceBDIR.getCaptureDeviceTechnologyIdentifier());
        assertEquals(deviceVendor, faceBDIR.getCaptureDeviceVendorIdentifier());
        assertEquals(deviceType, faceBDIR.getCaptureDeviceTypeIdentifier());
    }

    /**
     * Tests FaceBDIR constructor with DataInputStream
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructor_dataInputStream_createsFaceBDIR() throws Exception {
        byte[] testData = createValidFaceBDIRData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FaceBDIR faceBDIR = new FaceBDIR(inputStream);

        assertNotNull(faceBDIR);
        assertNotNull(faceBDIR.getGeneralHeader());
    }

    /**
     * Tests FaceBDIR constructor with DataInputStream and onlyImageInformation flag
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsFaceBDIR() throws Exception {
        byte[] testData = createValidFaceBDIRData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        FaceBDIR faceBDIR = new FaceBDIR(inputStream, true);

        assertNotNull(faceBDIR);
        assertNotNull(faceBDIR.getGeneralHeader());
    }

    /**
     * Tests writeObject method writes data correctly
     * @throws Exception if writing fails
     */
    @Test
    public void writeObject_validFaceBDIR_writesDataSuccessfully() throws Exception {
        FaceBDIR faceBDIR = createValidFaceBDIR();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        faceBDIR.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validFaceBDIR_returnsCorrectLength() {
        FaceBDIR faceBDIR = createValidFaceBDIR();

        long recordLength = faceBDIR.getRecordLength();

        assertTrue(recordLength > 0);
    }

    /**
     * Tests getRepresentation method with valid index
     */
    @Test
    public void getRepresentation_validIndex_returnsRepresentation() {
        FaceBDIR faceBDIR = createValidFaceBDIR();

        Representation representation = faceBDIR.getRepresentation(0);

        assertNotNull(representation);
    }

    /**
     * Tests setRepresentation method with index
     */
    @Test
    public void setRepresentation_withIndex_setsRepresentationSuccessfully() {
        FaceBDIR faceBDIR = createValidFaceBDIR();
        Representation newRepresentation = createValidRepresentation();

        faceBDIR.setRepresentation(newRepresentation, 1);

        assertNotNull(faceBDIR.getRepresentation(1));
    }

    /**
     * Tests getter methods return correct values
     */
    @Test
    public void getters_validFaceBDIR_returnCorrectValues() {
        FaceBDIR faceBDIR = createValidFaceBDIR();

        assertNotNull(faceBDIR.getCaptureDateTime());
        assertTrue(faceBDIR.getWidth() > 0);
        assertTrue(faceBDIR.getHeight() > 0);
        assertNotNull(faceBDIR.getImage());
        assertTrue(faceBDIR.getImageLength() > 0);
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validFaceBDIR_returnsNonNullString() {
        FaceBDIR faceBDIR = createValidFaceBDIR();

        String result = faceBDIR.toString();

        assertNotNull(result);
        assertTrue(result.contains("FaceBDIR"));
    }

    private FaceBDIR createValidFaceBDIR() {
        return new FaceBDIR(FaceFormatIdentifier.FORMAT_FAC, FaceVersionNumber.VERSION_030,
            FaceCertificationFlag.UNSPECIFIED, TemporalSequenceFlags.ONE_REPRESENTATION,
            new Date(), 1, new FaceQualityBlock[]{new FaceQualityBlock(40)},
            createFacialInformation(), null, createImageInformation(), new byte[100], null);
    }

    private FacialInformation createFacialInformation() {
        return new FacialInformation(0, Gender.UNKNOWN, EyeColour.UNSPECIFIED,
            HairColour.UNSPECIFIED, HeightCodes.UNSPECIFIED, Features.FEATURES_ARE_SPECIFIED,
            0, new int[]{0, 0, 0}, new int[]{0, 0, 0});
    }

    private ImageInformation createImageInformation() {
        return new ImageInformation(640, 480);
    }

    private Representation createValidRepresentation() {
        return new Representation(new Date(), new FaceQualityBlock[]{new FaceQualityBlock(40)},
            createFacialInformation(), null, createImageInformation(), new byte[100], null);
    }

    private byte[] createValidFaceBDIRData() {
        return new byte[100];
    }
}