package io.mosip.biometrics.util.face;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ImageInformationTest {

    /**
     * Tests ImageInformation constructor with basic parameters
     */
    @Test
    public void constructor_basicParameters_createsImageInformation() {
        int width = 640;
        int height = 480;

        ImageInformation imageInfo = new ImageInformation(width, height);

        assertNotNull(imageInfo);
        assertEquals(width, imageInfo.getWidth());
        assertEquals(height, imageInfo.getHeight());
        assertEquals(FaceImageType.BASIC, imageInfo.getFaceImageType());
        assertEquals(ImageDataType.JPEG2000_LOSS_LESS, imageInfo.getImageDataType());
        assertEquals(SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180, 
            imageInfo.getSpatialSamplingRateLevel());
        assertEquals(0, imageInfo.getPostAcquistionProcessing());
        assertEquals(CrossReference.BASIC, imageInfo.getCrossReference());
        assertEquals(ImageColourSpace.UNSPECIFIED, imageInfo.getImageColorSpace());
    }

    /**
     * Tests ImageInformation constructor with all parameters
     */
    @Test
    public void constructor_allParameters_createsImageInformation() {
        int faceImageType = FaceImageType.FULL_FRONTAL;
        int imageDataType = ImageDataType.JPEG2000_LOSSY;
        int width = 800;
        int height = 600;
        int spatialSamplingRateLevel = SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_300_TO_370;
        int postAcquisitionProcessing = 1;
        int crossReference = CrossReference.BASIC;
        int imageColorSpace = ImageColourSpace.BIT_24_RGB;

        ImageInformation imageInfo = new ImageInformation(faceImageType, imageDataType, width, height,
            spatialSamplingRateLevel, postAcquisitionProcessing, crossReference, imageColorSpace);

        assertNotNull(imageInfo);
        assertEquals(faceImageType, imageInfo.getFaceImageType());
        assertEquals(imageDataType, imageInfo.getImageDataType());
        assertEquals(width, imageInfo.getWidth());
        assertEquals(height, imageInfo.getHeight());
        assertEquals(spatialSamplingRateLevel, imageInfo.getSpatialSamplingRateLevel());
        assertEquals(postAcquisitionProcessing, imageInfo.getPostAcquistionProcessing());
        assertEquals(crossReference, imageInfo.getCrossReference());
        assertEquals(imageColorSpace, imageInfo.getImageColorSpace());
    }

    /**
     * Tests ImageInformation constructor with DataInputStream
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructor_dataInputStream_createsImageInformation() throws Exception {
        byte[] testData = createImageInformationData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        ImageInformation imageInfo = new ImageInformation(inputStream);

        assertNotNull(imageInfo);
    }

    /**
     * Tests ImageInformation constructor with DataInputStream and onlyImageInformation flag
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsImageInformation() throws Exception {
        byte[] testData = createImageInformationData();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        ImageInformation imageInfo = new ImageInformation(inputStream, true);

        assertNotNull(imageInfo);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validImageInformation_returnsCorrectLength() {
        ImageInformation imageInfo = new ImageInformation(640, 480);

        long recordLength = imageInfo.getRecordLength();

        assertEquals(11, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     * @throws Exception if writing fails
     */
    @Test
    public void writeObject_validImageInformation_writesDataSuccessfully() throws Exception {
        ImageInformation imageInfo = new ImageInformation(1024, 768);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        imageInfo.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(11, result.length);
    }

    /**
     * Tests setFaceImageType method sets value correctly
     */
    @Test
    public void setFaceImageType_validType_setsValueCorrectly() {
        ImageInformation imageInfo = new ImageInformation(640, 480);
        int newType = FaceImageType.FULL_FRONTAL;

        imageInfo.setFaceImageType(newType);

        assertEquals(newType, imageInfo.getFaceImageType());
    }

    /**
     * Tests setImageDataType method sets value correctly
     */
    @Test
    public void setImageDataType_validType_setsValueCorrectly() {
        ImageInformation imageInfo = new ImageInformation(640, 480);
        int newType = ImageDataType.JPEG2000_LOSSY;

        imageInfo.setImageDataType(newType);

        assertEquals(newType, imageInfo.getImageDataType());
    }

    /**
     * Tests setWidth method sets value correctly
     */
    @Test
    public void setWidth_validWidth_setsValueCorrectly() {
        ImageInformation imageInfo = new ImageInformation(640, 480);
        int newWidth = 1920;

        imageInfo.setWidth(newWidth);

        assertEquals(newWidth, imageInfo.getWidth());
    }

    /**
     * Tests setHeight method sets value correctly
     */
    @Test
    public void setHeight_validHeight_setsValueCorrectly() {
        ImageInformation imageInfo = new ImageInformation(640, 480);
        int newHeight = 1080;

        imageInfo.setHeight(newHeight);

        assertEquals(newHeight, imageInfo.getHeight());
    }

    /**
     * Tests setSpatialSamplingRateLevel method sets value correctly
     */
    @Test
    public void setSpatialSamplingRateLevel_validLevel_setsValueCorrectly() {
        ImageInformation imageInfo = new ImageInformation(640, 480);
        int newLevel = SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_300_TO_370;

        imageInfo.setSpatialSamplingRateLevel(newLevel);

        assertEquals(newLevel, imageInfo.getSpatialSamplingRateLevel());
    }

    /**
     * Tests setPostAcquistionProcessing method sets value correctly
     */
    @Test
    public void setPostAcquistionProcessing_validProcessing_setsValueCorrectly() {
        ImageInformation imageInfo = new ImageInformation(640, 480);
        int newProcessing = 5;

        imageInfo.setPostAcquistionProcessing(newProcessing);

        assertEquals(newProcessing, imageInfo.getPostAcquistionProcessing());
    }

    /**
     * Tests setCrossReference method sets value correctly
     */
    @Test
    public void setCrossReference_validReference_setsValueCorrectly() {
        ImageInformation imageInfo = new ImageInformation(640, 480);
        int newReference = CrossReference.CROSSREFERENCE_FF;

        imageInfo.setCrossReference(newReference);

        assertEquals(newReference, imageInfo.getCrossReference());
    }

    /**
     * Tests setImageColorSpace method sets value correctly
     */
    @Test
    public void setImageColorSpace_validColorSpace_setsValueCorrectly() {
        ImageInformation imageInfo = new ImageInformation(640, 480);
        int newColorSpace = ImageColourSpace.BIT_24_RGB;

        imageInfo.setImageColorSpace(newColorSpace);

        assertEquals(newColorSpace, imageInfo.getImageColorSpace());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validImageInformation_returnsNonNullString() {
        ImageInformation imageInfo = new ImageInformation(640, 480);

        String result = imageInfo.toString();

        assertNotNull(result);
        assertTrue(result.contains("ImageInformation"));
    }

    private byte[] createImageInformationData() {
        return new byte[]{
            0x01, 0x02,
            0x02, (byte) 0x80,
            0x01, (byte) 0xE0,
            (byte) 0xB4,
            0x00, 0x00,
            0x00, 0x01
        };
    }
}