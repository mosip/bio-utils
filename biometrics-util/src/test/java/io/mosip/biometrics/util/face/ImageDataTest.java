package io.mosip.biometrics.util.face;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ImageDataTest {

    /**
     * Tests ImageData constructor with byte array
     */
    @Test
    public void constructor_byteArray_createsImageData() {
        byte[] image = createSampleImageData();

        ImageData imageData = new ImageData(image);

        assertNotNull(imageData);
        assertEquals(image.length, imageData.getImageLength());
        assertArrayEquals(image, imageData.getImage());
    }

    /**
     * Tests ImageData constructor with DataInputStream
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructor_dataInputStream_createsImageData() throws Exception {
        byte[] testData = createImageDataStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        ImageData imageData = new ImageData(inputStream);

        assertNotNull(imageData);
        assertTrue(imageData.getImageLength() > 0);
    }

    /**
     * Tests ImageData constructor with DataInputStream and onlyImageInformation flag
     * @throws Exception if stream reading fails
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsImageData() throws Exception {
        byte[] testData = createImageDataStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        ImageData imageData = new ImageData(inputStream, true);

        assertNotNull(imageData);
        assertTrue(imageData.getImageLength() > 0);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validImageData_returnsCorrectLength() {
        byte[] image = createSampleImageData();
        ImageData imageData = new ImageData(image);

        long recordLength = imageData.getRecordLength();

        assertEquals(4 + image.length, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     * @throws Exception if writing fails
     */
    @Test
    public void writeObject_validImageData_writesDataSuccessfully() throws Exception {
        byte[] image = createSampleImageData();
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
     * @throws Exception if writing fails
     */
    @Test
    public void writeObject_nullImage_writesLengthOnly() throws Exception {
        ImageData imageData = new ImageData(new byte[0]);
        imageData.setImage(null);
        imageData.setImageLength(0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        imageData.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(4, result.length);
    }

    /**
     * Tests setImageLength method sets value correctly
     */
    @Test
    public void setImageLength_validLength_setsValueCorrectly() {
        ImageData imageData = new ImageData(createSampleImageData());
        long newLength = 2048L;

        imageData.setImageLength(newLength);

        assertEquals(newLength, imageData.getImageLength());
    }

    /**
     * Tests setImage method sets value correctly
     */
    @Test
    public void setImage_validImage_setsValueCorrectly() {
        ImageData imageData = new ImageData(createSampleImageData());
        byte[] newImage = new byte[]{1, 2, 3, 4, 5};

        imageData.setImage(newImage);

        assertArrayEquals(newImage, imageData.getImage());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validImageData_returnsNonNullString() {
        ImageData imageData = new ImageData(createSampleImageData());

        String result = imageData.toString();

        assertNotNull(result);
        assertTrue(result.contains("FaceImageData"));
    }

    /**
     * Tests ImageData with empty byte array
     */
    @Test
    public void constructor_emptyByteArray_createsImageData() {
        byte[] emptyImage = new byte[0];

        ImageData imageData = new ImageData(emptyImage);

        assertNotNull(imageData);
        assertEquals(0, imageData.getImageLength());
        assertArrayEquals(emptyImage, imageData.getImage());
    }

    /**
     * Tests ImageData with large byte array
     */
    @Test
    public void constructor_largeByteArray_createsImageData() {
        byte[] largeImage = new byte[10000];
        for (int i = 0; i < largeImage.length; i++) {
            largeImage[i] = (byte) (i % 256);
        }

        ImageData imageData = new ImageData(largeImage);

        assertNotNull(imageData);
        assertEquals(largeImage.length, imageData.getImageLength());
        assertArrayEquals(largeImage, imageData.getImage());
    }

    private byte[] createSampleImageData() {
        return new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10};
    }

    private byte[] createImageDataStream() {
        byte[] imageData = createSampleImageData();
        byte[] stream = new byte[4 + imageData.length];
        
        stream[0] = 0;
        stream[1] = 0;
        stream[2] = 0;
        stream[3] = (byte) imageData.length;
        
        System.arraycopy(imageData, 0, stream, 4, imageData.length);
        
        return stream;
    }
}