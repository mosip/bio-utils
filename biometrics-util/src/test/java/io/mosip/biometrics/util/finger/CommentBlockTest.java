package io.mosip.biometrics.util.finger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link CommentBlock} class to verify the handling of comment data
 * and extended data block functionality.
 */
class CommentBlockTest {

    private static final short IDENTIFICATION_CODE = (short) 0x1234;
    private byte[] sampleComment;

    /**
     * Sets up test data before each test method execution.
     */
    @BeforeEach
    void setUp() {
        sampleComment = "ABC".getBytes();
    }

    /**
     * Verifies that constructor properly initializes fields and calculates record length
     * when provided with comment data and identification code.
     */
    @Test
    void constructorWithByteArrayInitializesCorrectly() {
        CommentBlock block = new CommentBlock(sampleComment, IDENTIFICATION_CODE);

        assertArrayEquals(sampleComment, block.getComment());
        assertEquals(IDENTIFICATION_CODE, block.getExtendedDataBlockIdentificationCode());
        assertEquals(2 + 2 + sampleComment.length, block.getRecordLength());
    }

    /**
     * Verifies that constructor correctly reads and initializes comment data
     * from a DataInputStream.
     */
    @Test
    void constructorWithDataInputStreamReadsCorrectly() throws IOException {
        int lengthField = sampleComment.length + 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(lengthField);
        dos.write(sampleComment);
        dos.flush();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        CommentBlock block = new CommentBlock(dis, IDENTIFICATION_CODE);

        assertArrayEquals(sampleComment, block.getComment());
        assertEquals(lengthField, block.getLengthOfExtendedDataBlock());
    }

    /**
     * Verifies that constructor with onlyImageInformation flag set to true
     * maintains null comment state.
     */
    @Test
    void constructorWithImageInfoFlagMaintainsNullState() throws IOException {
        int lengthField = sampleComment.length + 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(lengthField);
        dos.write(sampleComment);
        dos.flush();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        CommentBlock block = new CommentBlock(dis, IDENTIFICATION_CODE, true);

        assertNull(block.getComment());
    }

    /**
     * Verifies that writeObject correctly serializes the comment block data
     * including header and comment content.
     */
    @Test
    void writeObjectSerializesCorrectly() throws IOException {
        CommentBlock block = new CommentBlock(sampleComment, IDENTIFICATION_CODE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        block.writeObject(dos);

        byte[] written = baos.toByteArray();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(written));

        assertEquals(IDENTIFICATION_CODE, dis.readUnsignedShort());
        int lengthField = dis.readUnsignedShort();
        assertEquals(sampleComment.length + 4, lengthField);
        byte[] readComment = new byte[sampleComment.length];
        dis.readFully(readComment);
        assertArrayEquals(sampleComment, readComment);
    }

    /**
     * Verifies that toString method includes the comment content
     * in its string representation.
     */
    @Test
    void toStringIncludesCommentContent() {
        CommentBlock block = new CommentBlock(sampleComment, IDENTIFICATION_CODE);
        String str = block.toString();

        assertTrue(str.contains("Comment=ABC"));
    }

    /**
     * Verifies that getRecordLength returns correct length
     * when comment is empty.
     */
    @Test
    void getRecordLengthWithEmptyCommentReturnsHeaderLength() {
        CommentBlock block = new CommentBlock(new byte[0], IDENTIFICATION_CODE);

        assertEquals(4, block.getRecordLength());
    }
}