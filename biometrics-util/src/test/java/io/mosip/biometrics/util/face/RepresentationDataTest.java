package io.mosip.biometrics.util.face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link RepresentationData}.
 */
@ExtendWith(MockitoExtension.class)
class RepresentationDataTest {

    @Mock
    private ImageData mockImageData;

    @Mock
    private DataInputStream mockDataInputStream;

    @Mock
    private DataOutputStream mockDataOutputStream;

    private byte[] testThreeDData;
    private RepresentationData representationData;

    /**
     * Sets up test data and mocks before each test.
     */
    @BeforeEach
    void setUp() {
        testThreeDData = new byte[]{1, 2, 3, 4, 5};
    }

    /**
     * Creates a valid ImageData byte stream that ImageData constructor can parse.
     */
    private byte[] createValidImageDataBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Write minimal valid ImageData structure
        dos.writeInt(100); // imageLength or similar field
        dos.writeInt(50);  // width
        dos.writeInt(50);  // height
        dos.writeByte(1);  // some byte field

        // Write some image data
        byte[] imageBytes = new byte[100];
        for (int i = 0; i < 100; i++) {
            imageBytes[i] = (byte) (i % 256);
        }
        dos.write(imageBytes);

        dos.flush();
        return baos.toByteArray();
    }

    /**
     * Tests constructor with ImageData and threeDInformationAndData parameters.
     */
    @Test
    void constructorWithParameters() {
        representationData = new RepresentationData(mockImageData, testThreeDData);

        assertEquals(mockImageData, representationData.getImageData());
        assertArrayEquals(testThreeDData, representationData.getThreeDInformationAndData());
    }

    /**
     * Tests constructor with null threeDInformationAndData.
     */
    @Test
    void constructorWithNullThreeDData() {
        representationData = new RepresentationData(mockImageData, null);

        assertEquals(mockImageData, representationData.getImageData());
        assertNull(representationData.getThreeDInformationAndData());
    }

    /**
     * Tests constructor with DataInputStream throws IOException due to invalid data.
     */
    @Test
    void constructorWithDataInputStreamThrowsEOFException() {
        byte[] insufficientData = {10, 20, 30};
        ByteArrayInputStream bais = new ByteArrayInputStream(insufficientData);
        DataInputStream dis = new DataInputStream(bais);

        assertThrows(IOException.class, () -> new RepresentationData(dis));
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag throws IOException.
     */
    @Test
    void constructorWithDataInputStreamAndFlagThrowsEOFException() {
        byte[] insufficientData = {10, 20, 30};
        ByteArrayInputStream bais = new ByteArrayInputStream(insufficientData);
        DataInputStream dis = new DataInputStream(bais);

        assertThrows(IOException.class, () -> new RepresentationData(dis, true));
    }

    /**
     * Tests readObject method with mocked ImageData to avoid EOFException.
     */
    @Test
    void readObjectWithMockedImageData() throws IOException {
        representationData = new RepresentationData(mockImageData, null);

        when(mockDataInputStream.available()).thenReturn(5);
        when(mockDataInputStream.read()).thenReturn(1, 2, 3, 4, 5, -1);

        representationData.readObject(mockDataInputStream);

        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, representationData.getThreeDInformationAndData());
    }

    /**
     * Tests readObject method with no additional data available.
     */
    @Test
    void readObjectWithNoAdditionalDataAvailable() throws IOException {
        representationData = new RepresentationData(mockImageData, null);

        when(mockDataInputStream.available()).thenReturn(0);

        representationData.readObject(mockDataInputStream);

        assertNull(representationData.getThreeDInformationAndData());
    }

    /**
     * Tests readObject method with exception handling.
     */
    @Test
    void readObjectWithException() throws IOException {
        when(mockDataInputStream.available()).thenThrow(new RuntimeException("Test exception"));

        representationData = new RepresentationData(mockImageData, null);

        assertThrows(IOException.class, () -> representationData.readObject(mockDataInputStream));
    }

    /**
     * Tests readObject method with onlyImageInformation flag using mock.
     */
    @Test
    void readObjectWithOnlyImageInformation() throws IOException {
        representationData = new RepresentationData(mockImageData, null);

        representationData.readObject(mockDataInputStream, true);

        assertNotNull(representationData.getImageData());
    }

    /**
     * Tests getRecordLength with null threeDInformationAndData.
     */
    @Test
    void getRecordLengthWithNullThreeDData() {
        when(mockImageData.getRecordLength()).thenReturn(100L);

        representationData = new RepresentationData(mockImageData, null);
        long result = representationData.getRecordLength();

        assertEquals(100L, result);
    }

    /**
     * Tests getRecordLength with non-null threeDInformationAndData.
     */
    @Test
    void getRecordLengthWithThreeDData() {
        when(mockImageData.getRecordLength()).thenReturn(100L);

        representationData = new RepresentationData(mockImageData, testThreeDData);
        long result = representationData.getRecordLength();

        assertEquals(105L, result);
    }

    /**
     * Tests writeObject method with null threeDInformationAndData.
     */
    @Test
    void writeObjectWithNullThreeDData() throws IOException {
        representationData = new RepresentationData(mockImageData, null);

        representationData.writeObject(mockDataOutputStream);

        verify(mockImageData).writeObject(mockDataOutputStream);
        verify(mockDataOutputStream).flush();
    }

    /**
     * Tests writeObject method with non-null threeDInformationAndData.
     */
    @Test
    void writeObjectWithThreeDData() throws IOException {
        representationData = new RepresentationData(mockImageData, testThreeDData);

        representationData.writeObject(mockDataOutputStream);

        verify(mockImageData).writeObject(mockDataOutputStream);
        verify(mockDataOutputStream).write(testThreeDData, 0, testThreeDData.length);
        verify(mockDataOutputStream).flush();
    }

    /**
     * Tests writeObject method throws IOException.
     */
    @Test
    void writeObjectThrowsIOException() throws IOException {
        doThrow(new IOException("Write failed")).when(mockImageData).writeObject(mockDataOutputStream);

        representationData = new RepresentationData(mockImageData, testThreeDData);

        assertThrows(IOException.class, () -> representationData.writeObject(mockDataOutputStream));
    }

    /**
     * Tests setImageData method.
     */
    @Test
    void setImageData() {
        representationData = new RepresentationData(null, null);

        representationData.setImageData(mockImageData);

        assertEquals(mockImageData, representationData.getImageData());
    }

    /**
     * Tests setThreeDInformationAndData method.
     */
    @Test
    void setThreeDInformationAndData() {
        representationData = new RepresentationData(mockImageData, null);

        representationData.setThreeDInformationAndData(testThreeDData);

        assertArrayEquals(testThreeDData, representationData.getThreeDInformationAndData());
    }

    /**
     * Tests toString method with null threeDInformationAndData.
     */
    @Test
    void toStringWithNullThreeDData() {
        when(mockImageData.toString()).thenReturn("MockImageData");
        when(mockImageData.getRecordLength()).thenReturn(100L);

        representationData = new RepresentationData(mockImageData, null);
        String result = representationData.toString();

        assertNotNull(result);
        assertTrue(result.contains("RepresentationData"));
        assertTrue(result.contains("100"));
        assertTrue(result.contains("null"));
    }

    /**
     * Tests toString method with non-null threeDInformationAndData.
     */
    @Test
    void toStringWithThreeDData() {
        when(mockImageData.toString()).thenReturn("MockImageData");
        when(mockImageData.getRecordLength()).thenReturn(100L);

        representationData = new RepresentationData(mockImageData, testThreeDData);
        String result = representationData.toString();

        assertNotNull(result);
        assertTrue(result.contains("RepresentationData"));
        assertTrue(result.contains("105"));
        assertTrue(result.contains("[1, 2, 3, 4, 5]"));
    }

    /**
     * Tests readObject with IOException during stream operations.
     */
    @Test
    void readObjectWithIOExceptionDuringRead() throws IOException {
        when(mockDataInputStream.available()).thenReturn(5);
        when(mockDataInputStream.read()).thenThrow(new IOException("Stream read failed"));

        representationData = new RepresentationData(mockImageData, null);

        assertThrows(IOException.class, () -> representationData.readObject(mockDataInputStream));
    }

    /**
     * Tests constructor with DataInputStream that throws IOException.
     */
    @Test
    void constructorWithDataInputStreamThrowsIOException() throws IOException {
        when(mockDataInputStream.available()).thenThrow(new IOException("Stream error"));

        assertThrows(IOException.class, () -> new RepresentationData(mockDataInputStream));
    }
}
