package io.mosip.biometrics.util.finger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class AnnotationBlockTest {

    /**
     * Tests constructor with annotation data
     */
    @Test
    public void constructorWithAnnotationDataCreatesAnnotationBlock() {
        AnnotationData[] annotationData = {new AnnotationData(FingerPosition.RIGHT_THUMB, AnnotationCode.AMPUTATED_FINGER)};

        AnnotationBlock annotationBlock = new AnnotationBlock(1, annotationData);

        assertNotNull(annotationBlock);
        assertEquals(1, annotationBlock.getNoOfAnnotationData());
        assertEquals(ExtendedDataBlockIdentificationCode.ANNOTATION, annotationBlock.getExtendedDataBlockIdentificationCode());
        assertArrayEquals(annotationData, annotationBlock.getAnnotationData());
    }

    /**
     * Tests constructor with DataInputStream
     */
    @Test
    public void constructorDataInputStreamCreatesAnnotationBlock() throws Exception {
        byte[] testData = {0x00, 0x05, 0x01, 0x01, 0x01};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        AnnotationBlock annotationBlock = new AnnotationBlock(inputStream);

        assertNotNull(annotationBlock);
        assertEquals(ExtendedDataBlockIdentificationCode.ANNOTATION, annotationBlock.getExtendedDataBlockIdentificationCode());
    }

    /**
     * Tests constructor with DataInputStream and onlyImageInformation flag
     */
    @Test
    public void constructorDataInputStreamWithImageInfoFlagCreatesAnnotationBlock() throws Exception {
        byte[] testData = {0x00, 0x05, 0x01, 0x01, 0x01};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        DataInputStream inputStream = new DataInputStream(bais);

        AnnotationBlock annotationBlock = new AnnotationBlock(inputStream, true);

        assertNotNull(annotationBlock);
    }

    /**
     * Tests getRecordLength method returns correct length
     */
    @Test
    public void getRecordLengthValidAnnotationBlockReturnsCorrectLength() {
        AnnotationData[] annotationData = {new AnnotationData(FingerPosition.LEFT_INDEX_FINGER, AnnotationCode.UNUSABLE_IMAGE)};
        AnnotationBlock annotationBlock = new AnnotationBlock(1, annotationData);

        long recordLength = annotationBlock.getRecordLength();

        assertEquals(7, recordLength); // 2 + 2 + 1 + 2 (annotation data length)
    }

    /**
     * Tests writeObject method writes data correctly
     */
    @Test
    public void writeObjectValidAnnotationBlockWritesDataSuccessfully() throws Exception {
        AnnotationData[] annotationData = {new AnnotationData(FingerPosition.RIGHT_MIDDLE_FINGER, AnnotationCode.AMPUTATED_FINGER)};
        AnnotationBlock annotationBlock = new AnnotationBlock(1, annotationData);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(baos);

        annotationBlock.writeObject(outputStream);

        byte[] result = baos.toByteArray();
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    /**
     * Tests setNoOfAnnotationData method sets value correctly
     */
    @Test
    public void setNoOfAnnotationDataValidCountSetsValueCorrectly() {
        AnnotationData[] annotationData = {new AnnotationData()};
        AnnotationBlock annotationBlock = new AnnotationBlock(1, annotationData);
        int newCount = 2;

        annotationBlock.setNoOfAnnotationData(newCount);

        assertEquals(newCount, annotationBlock.getNoOfAnnotationData());
    }

    /**
     * Tests setAnnotationData method sets array correctly
     */
    @Test
    public void setAnnotationDataValidArraySetsArrayCorrectly() {
        AnnotationData[] originalData = {new AnnotationData()};
        AnnotationBlock annotationBlock = new AnnotationBlock(1, originalData);
        AnnotationData[] newData = {new AnnotationData(FingerPosition.LEFT_THUMB, AnnotationCode.UNUSABLE_IMAGE)};

        annotationBlock.setAnnotationData(newData);

        assertArrayEquals(newData, annotationBlock.getAnnotationData());
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toStringValidAnnotationBlockReturnsNonNullString() {
        AnnotationData[] annotationData = {new AnnotationData(FingerPosition.RIGHT_RING_FINGER, AnnotationCode.AMPUTATED_FINGER)};
        AnnotationBlock annotationBlock = new AnnotationBlock(1, annotationData);

        String result = annotationBlock.toString();

        assertNotNull(result);
        assertTrue(result.contains("AnnotationBlock"));
    }
}