package io.mosip.biometrics.util.finger;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CommentBlock}.
 */
class CommentBlockTest {

    private static final short IDENTIFICATION_CODE = (short) 0x1234;
    private byte[] sampleComment;

    @BeforeEach
    void setUp() {
        sampleComment = "ABC".getBytes();
    }

    /**
     * Tests the constructor that accepts a byte[] comment and ID code.
     * Verifies that fields are set and record length is calculated correctly.
     */
    @Test
    void testConstructor_WithCommentAndIdentificationCode_SetsFields() {
        CommentBlock block = new CommentBlock(sampleComment, IDENTIFICATION_CODE);
        assertArrayEquals(sampleComment, block.getComment(), "Comment should be stored");
        assertEquals(IDENTIFICATION_CODE, block.getExtendedDataBlockIdentificationCode(),
                "Identification code should match");
        // record length = 2 (ID) + 2 (length field) + comment length
        assertEquals(2 + 2 + sampleComment.length, block.getRecordLength(),
                "Record length should include header and comment bytes");
    }

    /**
     * Tests the constructor that reads from a DataInputStream.
     * Verifies that the comment is read correctly.
     */
    @Test
    void testConstructor_WithDataInputStream_ReadsComment() throws IOException {
        // prepare a stream: length field = comment length + 4, then comment bytes
        int lengthField = sampleComment.length + 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(lengthField);
        dos.write(sampleComment);
        dos.flush();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        CommentBlock block = new CommentBlock(dis, IDENTIFICATION_CODE);
        assertArrayEquals(sampleComment, block.getComment(), "Comment should be read from stream");
        assertEquals(lengthField, block.getLengthOfExtendedDataBlock(),
                "Length field should match what was written");
    }

    /**
     * Tests that readObject with onlyImageInformation does not throw and leaves state unchanged.
     */
    @Test
    void testConstructor_WithDataInputStreamAndOnlyImageInformation_DoesNothing() throws IOException {
        // same stream as above
        int lengthField = sampleComment.length + 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(lengthField);
        dos.write(sampleComment);
        dos.flush();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        // call the constructor variant; it should not throw
        CommentBlock block = new CommentBlock(dis, IDENTIFICATION_CODE, true);
        // comment remains null because readObject(boolean) is empty
        assertNull(block.getComment(), "Comment should remain null when onlyImageInformation is true");
    }

    /**
     * Tests writeObject produces correct byte sequence including header and comment.
     */
    @Test
    void testWriteObject_WritesCorrectBytes() throws IOException {
        CommentBlock block = new CommentBlock(sampleComment, IDENTIFICATION_CODE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        block.writeObject(dos);

        byte[] written = baos.toByteArray();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(written));
        // read back ID, length, and comment
        assertEquals(IDENTIFICATION_CODE, dis.readUnsignedShort(), "First two bytes are ID code");
        int lengthField = dis.readUnsignedShort();
        assertEquals(sampleComment.length + 4, lengthField, "Length field should match");
        byte[] readComment = new byte[sampleComment.length];
        dis.readFully(readComment);
        assertArrayEquals(sampleComment, readComment, "Comment bytes should match");
    }

    /**
     * Tests that toString includes the comment content.
     */
    @Test
    void testToString_IncludesComment() {
        CommentBlock block = new CommentBlock(sampleComment, IDENTIFICATION_CODE);
        String str = block.toString();
        assertTrue(str.contains("Comment=ABC"), "toString should contain the comment text");
    }

    /**
     * Tests that getRecordLength returns 4 when comment is empty.
     */
    @Test
    void testGetRecordLength_EmptyComment() {
        CommentBlock block = new CommentBlock(new byte[0], IDENTIFICATION_CODE);
        assertEquals(4, block.getRecordLength(), "With empty comment, record length should be header only");
    }
}

