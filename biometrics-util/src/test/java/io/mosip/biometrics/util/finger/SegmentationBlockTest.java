package io.mosip.biometrics.util.finger;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link SegmentationBlock}.
 */
class SegmentationBlockTest {

    @Test
    void constructorsWriteAndReadRoundTrip() throws Exception {
        SegmentationData[] data = new SegmentationData[] {
                new SegmentationData(FingerPosition.RIGHT_INDEX_FINGER, 80, 2, new int[]{1, 2}, new int[]{3, 4}, 10)
        };
        SegmentationBlock block = new SegmentationBlock(90, 1, data);
        assertEquals(1, block.getNoOfSegmentationData());
        assertTrue(block.getRecordLength() > 0);
        assertNotNull(block.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(baos)) {
            block.writeObject(out);
        }

        byte[] bytes = baos.toByteArray();
        // skip identification code already set by constructor path; readObject expects length first
        // writeObject writes: id(2) + length(2) + payload...
        // DataInputStream constructor path sets id then readObject which reads length first
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes, 2, bytes.length - 2))) {
            SegmentationBlock read = new SegmentationBlock(in);
            assertEquals(block.getNoOfSegmentationData(), read.getNoOfSegmentationData());
            assertEquals(block.getSegmentationQualityScore(), read.getSegmentationQualityScore());
            assertNotNull(read.toString());
        }

        SegmentationBlock onlyImage = new SegmentationBlock(
                new DataInputStream(new ByteArrayInputStream(bytes, 2, bytes.length - 2)), true);
        assertNotNull(onlyImage);

        SegmentationBlock withAlgo = new SegmentationBlock(
                FingerSegmentationAlgorithmVendorIdentifier.UNSPECIFIED,
                FingerSegmentationAlgorithmIdentifier.UNSPECIFIED,
                70, 1, data);
        assertEquals(70, withAlgo.getSegmentationQualityScore());
        assertTrue(withAlgo.getRecordLength() > 0);
    }

    @Test
    void emptySegmentationDataRecordLength() {
        SegmentationBlock block = new SegmentationBlock(50, 0, null);
        assertEquals(0, block.getNoOfSegmentationData());
        assertEquals(14, block.getRecordLength());
    }
}
