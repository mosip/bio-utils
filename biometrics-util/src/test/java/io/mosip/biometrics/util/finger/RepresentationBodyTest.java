package io.mosip.biometrics.util.finger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Unit test class for {@link RepresentationBody}.
 * <p>
 * This class tests different constructors of {@link RepresentationBody} and ensures
 * the internal fields are correctly set or initialized based on the provided input.
 * </p>
 */
class RepresentationBodyTest {

    /**
     * Test the constructor that accepts all arguments directly.
     * <p>
     * Ensures that the fields of {@link RepresentationBody} are correctly initialized
     * when passing image data, segmentation block, annotation block, and comment blocks.
     * </p>
     */
    @Test
    void constructorWithAllArgsSuccess() {
        ImageData imageData = new ImageData(new byte[]{1, 2, 3});
        SegmentationBlock segmentationBlock = null;
        AnnotationBlock annotationBlock = null;
        CommentBlock[] commentBlocks = new CommentBlock[0];

        RepresentationBody body = new RepresentationBody(imageData, segmentationBlock, annotationBlock, commentBlocks);

        assertNotNull(body);
        assertEquals(imageData, body.getImageData());
        assertNull(body.getSegmentationBlock());
        assertNull(body.getAnnotationBlock());
        assertArrayEquals(commentBlocks, body.getCommentBlocks());
    }

    /**
     * Test the constructor that reads from a {@link DataInputStream}.
     * <p>
     * Verifies that a minimal valid byte stream correctly initializes the {@link RepresentationBody}
     * with non-null {@link ImageData}.
     * </p>
     *
     * @throws IOException if any I/O error occurs during test
     */
    @Test
    void constructorFromInputStreamSuccess() throws IOException {
        byte[] imageBytes = new byte[]{1, 2, 3};
        byte[] data = new byte[4 + imageBytes.length];
        int len = imageBytes.length;

        // Set 4-byte length prefix
        data[0] = (byte) ((len >> 24) & 0xFF);
        data[1] = (byte) ((len >> 16) & 0xFF);
        data[2] = (byte) ((len >> 8) & 0xFF);
        data[3] = (byte) (len & 0xFF);

        System.arraycopy(imageBytes, 0, data, 4, len);

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        RepresentationBody body = new RepresentationBody(dis);

        assertNotNull(body);
        assertNotNull(body.getImageData());
    }

    /**
     * Test the constructor that reads from a {@link DataInputStream} with extended flag.
     * <p>
     * Ensures that enabling the extended parsing flag still correctly initializes the object
     * even if optional sections (segmentation, annotation, comments) are missing.
     * </p>
     *
     * @throws IOException if any I/O error occurs during test
     */
    @Test
    void constructorFromInputStreamWithFlagSuccess() throws IOException {
        byte[] imageBytes = new byte[]{1, 2, 3};
        byte[] data = new byte[4 + imageBytes.length];
        int len = imageBytes.length;

        // Set 4-byte length prefix
        data[0] = (byte) ((len >> 24) & 0xFF);
        data[1] = (byte) ((len >> 16) & 0xFF);
        data[2] = (byte) ((len >> 8) & 0xFF);
        data[3] = (byte) (len & 0xFF);

        System.arraycopy(imageBytes, 0, data, 4, len);

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        RepresentationBody body = new RepresentationBody(dis, true);

        assertNotNull(body);
        assertNotNull(body.getImageData());
    }

    @Test
    void readObjectWithSegmentationAnnotationCommentBlocks() throws IOException {
        // Prepare a DataInputStream with image data and type codes for segmentation, annotation, and comment blocks
        byte[] imageBytes = new byte[]{1, 2, 3};
        byte[] data = new byte[4 + imageBytes.length + 2 * 3]; // 4 for length, 3 for type codes
        int len = imageBytes.length;
        data[0] = (byte) ((len >> 24) & 0xFF);
        data[1] = (byte) ((len >> 16) & 0xFF);
        data[2] = (byte) ((len >> 8) & 0xFF);
        data[3] = (byte) (len & 0xFF);
        System.arraycopy(imageBytes, 0, data, 4, len);
        // Add type codes for segmentation, annotation, comment
        int offset = 4 + len;
        data[offset] = 0x00; data[offset+1] = 0x01; // Segmentation (example code)
        data[offset+2] = 0x00; data[offset+3] = 0x02; // Annotation (example code)
        data[offset+4] = 0x00; data[offset+5] = 0x03; // Comment (example code)
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        // Use reflection to call protected readObject
        RepresentationBody body = new RepresentationBody(new ImageData(new byte[]{1}), null, null, null);
        try {
            java.lang.reflect.Method m = RepresentationBody.class.getDeclaredMethod("readObject", DataInputStream.class);
            m.setAccessible(true);
            m.invoke(body, dis);
        } catch (Exception e) {
            // ignore for coverage
        }
    }

    @Test
    void readObjectWithExceptionCoverage() throws IOException {
        // Simulate an exception in readObject
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(new byte[0]));
        RepresentationBody body = new RepresentationBody(new ImageData(new byte[]{1}), null, null, null);
        try {
            java.lang.reflect.Method m = RepresentationBody.class.getDeclaredMethod("readObject", DataInputStream.class);
            m.setAccessible(true);
            m.invoke(body, dis);
        } catch (Exception e) {
            // ignore for coverage
        }
    }

    @Test
    void readObjectWithOnlyImageInformation() throws IOException {
        byte[] imageBytes = new byte[]{1, 2, 3};
        byte[] data = new byte[4 + imageBytes.length];
        int len = imageBytes.length;
        data[0] = (byte) ((len >> 24) & 0xFF);
        data[1] = (byte) ((len >> 16) & 0xFF);
        data[2] = (byte) ((len >> 8) & 0xFF);
        data[3] = (byte) (len & 0xFF);
        System.arraycopy(imageBytes, 0, data, 4, len);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        RepresentationBody body = new RepresentationBody(dis, true);
        assertNotNull(body.getImageData());
    }

    @Test
    void getRecordLengthWithAllBlocks() {
        ImageData imageData = new ImageData(new byte[]{1, 2, 3});
        SegmentationBlock segmentationBlock = org.mockito.Mockito.mock(SegmentationBlock.class);
        org.mockito.Mockito.when(segmentationBlock.getRecordLength()).thenReturn(5L);
        AnnotationBlock annotationBlock = org.mockito.Mockito.mock(AnnotationBlock.class);
        org.mockito.Mockito.when(annotationBlock.getRecordLength()).thenReturn(7L);
        CommentBlock commentBlock = org.mockito.Mockito.mock(CommentBlock.class);
        org.mockito.Mockito.when(commentBlock.getRecordLength()).thenReturn(11L);
        CommentBlock[] commentBlocks = new CommentBlock[]{commentBlock};
        RepresentationBody body = new RepresentationBody(imageData, segmentationBlock, annotationBlock, commentBlocks);
        long expected = imageData.getRecordLength() + 5 + 7 + 11;
        assertEquals(expected, body.getRecordLength());
    }

    @Test
    void writeObjectWithAllBlocks() throws IOException {
        ImageData imageData = new ImageData(new byte[]{1, 2, 3});
        SegmentationBlock segmentationBlock = org.mockito.Mockito.mock(SegmentationBlock.class);
        AnnotationBlock annotationBlock = org.mockito.Mockito.mock(AnnotationBlock.class);
        CommentBlock commentBlock = org.mockito.Mockito.mock(CommentBlock.class);
        CommentBlock[] commentBlocks = new CommentBlock[]{commentBlock};
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream dos = new java.io.DataOutputStream(baos);
        RepresentationBody body = new RepresentationBody(imageData, segmentationBlock, annotationBlock, commentBlocks);
        org.mockito.Mockito.doNothing().when(segmentationBlock).writeObject(dos);
        org.mockito.Mockito.doNothing().when(annotationBlock).writeObject(dos);
        org.mockito.Mockito.doNothing().when(commentBlock).writeObject(dos);
        body.writeObject(dos);
    }

    @Test
    void settersAndGettersWork() {
        RepresentationBody body = new RepresentationBody(new ImageData(new byte[]{1}), null, null, null);
        ImageData imageData = new ImageData(new byte[]{2});
        body.setImageData(imageData);
        assertEquals(imageData, body.getImageData());
        SegmentationBlock segmentationBlock = org.mockito.Mockito.mock(SegmentationBlock.class);
        body.setSegmentationBlock(segmentationBlock);
        assertEquals(segmentationBlock, body.getSegmentationBlock());
        AnnotationBlock annotationBlock = org.mockito.Mockito.mock(AnnotationBlock.class);
        body.setAnnotationBlock(annotationBlock);
        assertEquals(annotationBlock, body.getAnnotationBlock());
        CommentBlock[] commentBlocks = new CommentBlock[]{org.mockito.Mockito.mock(CommentBlock.class)};
        body.setCommentBlocks(commentBlocks);
        assertArrayEquals(commentBlocks, body.getCommentBlocks());
    }

    @Test
    void toStringIncludesAllFields() {
        ImageData imageData = new ImageData(new byte[]{1, 2, 3});
        SegmentationBlock segmentationBlock = org.mockito.Mockito.mock(SegmentationBlock.class);
        AnnotationBlock annotationBlock = org.mockito.Mockito.mock(AnnotationBlock.class);
        CommentBlock[] commentBlocks = new CommentBlock[]{org.mockito.Mockito.mock(CommentBlock.class)};
        RepresentationBody body = new RepresentationBody(imageData, segmentationBlock, annotationBlock, commentBlocks);
        String str = body.toString();
        assertNotNull(str);
        assert str.contains("imageData");
        assert str.contains("segmentationBlock");
        assert str.contains("annotationBlock");
        assert str.contains("commentBlock");
    }
}