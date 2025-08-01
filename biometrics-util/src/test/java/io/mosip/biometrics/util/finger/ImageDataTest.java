package io.mosip.biometrics.util.finger;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ImageDataTest {

    /**
     * Tests constructor with image data
     */
    @Test
    public void constructorWithImageDataCreatesImageData() {
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
    public void constructorDataInputStreamCreatesImageData() throws Exception {
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
    public void constructorDataInputStreamWithImageInfoFlagCreatesImageData() throws Exception {
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
    public void getRecordLengthReturnsCorrectLength() {
        byte[] image = "Hello World".getBytes();
        ImageData imageData = new ImageData(image);

        long recordLength = imageData.getRecordLength();

        assertEquals(4 + image.length, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObjectWritesDataSuccessfully() throws Exception {
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
    public void writeObjectNullImageWritesDataSuccessfully() throws Exception {
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
    public void setImageLengthSetsValueCorrectly() {
        ImageData imageData = new ImageData("Initial".getBytes());
        long newLength = 100L;

        imageData.setImageLength(newLength);

        assertEquals(newLength, imageData.getImageLength());
    }

    /**
     * Tests setImage method sets array correctly
     */
    @Test
    public void setImageSetsArrayCorrectly() {
        ImageData imageData = new ImageData("Original".getBytes());
        byte[] newImage = "Updated image".getBytes();

        imageData.setImage(newImage);

        assertArrayEquals(newImage, imageData.getImage());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toStringReturnsNonNullString() {
        byte[] image = "Sample image".getBytes();
        ImageData imageData = new ImageData(image);

        String result = imageData.toString();

        assertNotNull(result);
        assertTrue(result.contains("FingerImageData"));
    }

    private byte[] createTestData(byte[] imageData) {
        byte[] result = new byte[4 + imageData.length];
        result[0] = (byte) ((imageData.length >> 24) & 0xFF);
        result[1] = (byte) ((imageData.length >> 16) & 0xFF);
        result[2] = (byte) ((imageData.length >> 8) & 0xFF);
        result[3] = (byte) (imageData.length & 0xFF);
        System.arraycopy(imageData, 0, result, 4, imageData.length);
        return result;
    }
}