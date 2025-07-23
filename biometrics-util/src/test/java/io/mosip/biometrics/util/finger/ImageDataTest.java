package io.mosip.biometrics.util.finger;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ImageDataTest {

    /**
     * Tests constructor with image data
     */
    @Test
    public void constructor_withImageData_createsImageData() {
        byte[] image = "Test image data".getBytes();

        ImageData imageData = new ImageData(image);

        assertNotNull(imageData);
        assertEquals(image.length, imageData.getImageLength());
        assertArrayEquals(image, imageData.getImage());
    }

    /**
     * Tests constructor with DataInputStream
     */
    @Test
    public void constructor_dataInputStream_createsImageData() throws Exception {
        byte[] testImage = "Sample".getBytes();
        byte[] testData = createTestData(testImage);
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        ImageData imageData = new ImageData(inputStream);

        assertNotNull(imageData);
        assertEquals(testImage.length, imageData.getImageLength());
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsImageData() throws Exception {
        byte[] testImage = "Test".getBytes();
        byte[] testData = createTestData(testImage);
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        ImageData imageData = new ImageData(inputStream, true);

        assertNotNull(imageData);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validImageData_returnsCorrectLength() {
        byte[] image = "Hello World".getBytes();
        ImageData imageData = new ImageData(image);

        long recordLength = imageData.getRecordLength();

        assertEquals(4 + image.length, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObject_validImageData_writesDataSuccessfully() throws Exception {
        byte[] image = "Test data".getBytes();
        ImageData imageData = new ImageData(image);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        imageData.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(4 + image.length, result.length);
    }

    /**
     * Tests writeObject method with null image
     */
    @Test
    public void writeObject_nullImage_writesDataSuccessfully() throws Exception {
        ImageData imageData = new ImageData(new byte[0]);
        imageData.setImage(null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        imageData.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(4, result.length); // Only length field
    }

    /**
     * Tests setImageLength method sets value correctly
     */
    @Test
    public void setImageLength_validLength_setsValueCorrectly() {
        ImageData imageData = new ImageData("Initial".getBytes());
        long newLength = 100L;

        imageData.setImageLength(newLength);

        assertEquals(newLength, imageData.getImageLength());
    }

    /**
     * Tests setImage method sets array correctly
     */
    @Test
    public void setImage_validArray_setsArrayCorrectly() {
        ImageData imageData = new ImageData("Original".getBytes());
        byte[] newImage = "Updated image".getBytes();

        imageData.setImage(newImage);

        assertArrayEquals(newImage, imageData.getImage());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validImageData_returnsNonNullString() {
        byte[] image = "Sample image".getBytes();
        ImageData imageData = new ImageData(image);

        String result = imageData.toString();

        assertNotNull(result);
        assertTrue(result.contains("FingerImageData"));
    }

    private byte[] createTestData(byte[] imageData) {
        byte[] result = new byte[4 + imageData.length];
        // Write length as 4 bytes
        result[0] = (byte) ((imageData.length >> 24) & 0xFF);
        result[1] = (byte) ((imageData.length >> 16) & 0xFF);
        result[2] = (byte) ((imageData.length >> 8) & 0xFF);
        result[3] = (byte) (imageData.length & 0xFF);
        // Copy image data
        System.arraycopy(imageData, 0, result, 4, imageData.length);
        return result;
    }
}