package io.mosip.biometrics.util.iris;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link IrisRange}.
 */
public class IrisRangeTest {

    /**
     * Test constructor and value() method with valid value.
     */
    @Test
    public void testConstructorAndValue_Valid() {
        IrisRange range = new IrisRange(IrisRange.UNASSIGNED);
        assertEquals(IrisRange.UNASSIGNED, range.value());
    }

    /**
     * Test fromValue() with minimum valid value.
     */
    @Test
    public void testFromValue_MinValid() {
        assertEquals(IrisRange.UNASSIGNED, IrisRange.fromValue(IrisRange.UNASSIGNED));
    }

    /**
     * Test fromValue() with maximum valid value.
     */
    @Test
    public void testFromValue_MaxValid() {
        assertEquals(IrisRange.OVERFLOW_FFFF, IrisRange.fromValue(IrisRange.OVERFLOW_FFFF));
    }

    /**
     * Test fromValue() with a valid middle value.
     */
    @Test
    public void testFromValue_MiddleValid() {
        assertEquals(IrisRange.FAILED, IrisRange.fromValue(IrisRange.FAILED));
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value below range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromValue_BelowRange() {
        IrisRange.fromValue(-1);
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value above range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromValue_AboveRange() {
        IrisRange.fromValue(0x10000);
    }

    /**
     * Test toString() returns a non-null string containing the value in hex.
     */
    @Test
    public void testToString() {
        IrisRange range = new IrisRange(IrisRange.OVERFLOW_0002);
        String str = range.toString();
        assertNotNull(str);
        assertTrue(str.contains(Integer.toHexString(IrisRange.OVERFLOW_0002)));
    }
} 