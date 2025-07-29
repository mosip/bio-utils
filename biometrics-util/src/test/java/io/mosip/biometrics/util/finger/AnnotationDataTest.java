package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class AnnotationDataTest {

    /**
     * Tests default constructor
     */
    @Test
    public void constructor_default_createsAnnotationData() {
        AnnotationData annotationData = new AnnotationData();

        assertNotNull(annotationData);
        assertEquals(FingerPosition.UNKNOWN, annotationData.getFingerPosition());
        assertEquals(AnnotationCode.AMPUTATED_FINGER, annotationData.getAnnotationCode());
    }

    /**
     * Tests constructor with parameters
     */
    @Test
    public void constructor_withParameters_createsAnnotationData() {
        int fingerPosition = FingerPosition.RIGHT_THUMB;
        int annotationCode = AnnotationCode.UNUSABLE_IMAGE;

        AnnotationData annotationData = new AnnotationData(fingerPosition, annotationCode);

        assertNotNull(annotationData);
        assertEquals(fingerPosition, annotationData.getFingerPosition());
        assertEquals(annotationCode, annotationData.getAnnotationCode());
    }

    /**
     * Tests constructor with DataInputStream
     */
    @Test
    public void constructor_dataInputStream_createsAnnotationData() throws Exception {
        byte[] testData = {0x01, 0x02};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        AnnotationData annotationData = new AnnotationData(inputStream);

        assertNotNull(annotationData);
        assertEquals(0x01, annotationData.getFingerPosition());
        assertEquals(0x02, annotationData.getAnnotationCode());
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructor_dataInputStreamWithImageInfoFlag_createsAnnotationData() throws Exception {
        byte[] testData = {0x03, 0x01};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        AnnotationData annotationData = new AnnotationData(inputStream, true);

        assertNotNull(annotationData);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLength_validAnnotationData_returnsCorrectLength() {
        AnnotationData annotationData = new AnnotationData(FingerPosition.LEFT_INDEX_FINGER, AnnotationCode.AMPUTATED_FINGER);

        long recordLength = annotationData.getRecordLength();

        assertEquals(2, recordLength);
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObject_validAnnotationData_writesDataSuccessfully() throws Exception {
        AnnotationData annotationData = new AnnotationData(FingerPosition.RIGHT_MIDDLE_FINGER, AnnotationCode.UNUSABLE_IMAGE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        annotationData.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(FingerPosition.RIGHT_MIDDLE_FINGER, result[0] & 0xFF);
        assertEquals(AnnotationCode.UNUSABLE_IMAGE, result[1] & 0xFF);
    }

    /**
     * Tests setFingerPosition method sets value correctly
     */
    @Test
    public void setFingerPosition_validPosition_setsValueCorrectly() {
        AnnotationData annotationData = new AnnotationData();
        int newPosition = FingerPosition.LEFT_THUMB;

        annotationData.setFingerPosition(newPosition);

        assertEquals(newPosition, annotationData.getFingerPosition());
    }

    /**
     * Tests setAnnotationCode method sets value correctly
     */
    @Test
    public void setAnnotationCode_validCode_setsValueCorrectly() {
        AnnotationData annotationData = new AnnotationData();
        int newCode = AnnotationCode.UNUSABLE_IMAGE;

        annotationData.setAnnotationCode(newCode);

        assertEquals(newCode, annotationData.getAnnotationCode());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validAnnotationData_returnsNonNullString() {
        AnnotationData annotationData = new AnnotationData(FingerPosition.RIGHT_RING_FINGER, AnnotationCode.AMPUTATED_FINGER);

        String result = annotationData.toString();

        assertNotNull(result);
        assertTrue(result.contains("AnnotationData"));
    }
}