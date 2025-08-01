package io.mosip.biometrics.util.iris;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link IrisRange}.
 */
public class IrisRangeTest {

    /**
     * Test constructor and value() method with valid value.
     */
    @Test
    public void constructorAndValueReturnsSameValue() {
        IrisRange range = new IrisRange(IrisRange.UNASSIGNED);
        assertEquals(IrisRange.UNASSIGNED, range.value());
    }

    /**
     * Test fromValue() with minimum valid value.
     */
    @Test
    public void fromValueWithMinimumReturnsValue() {
        assertEquals(IrisRange.UNASSIGNED, IrisRange.fromValue(IrisRange.UNASSIGNED));
    }

    /**
     * Test fromValue() with maximum valid value.
     */
    @Test
    public void fromValueWithMaximumReturnsValue() {
        assertEquals(IrisRange.OVERFLOW_FFFF, IrisRange.fromValue(IrisRange.OVERFLOW_FFFF));
    }

    /**
     * Test fromValue() with a valid middle value.
     */
    @Test
    public void fromValueWithMiddleReturnsValue() {
        assertEquals(IrisRange.FAILED, IrisRange.fromValue(IrisRange.FAILED));
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value below range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        IrisRange.fromValue(-1);
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value above range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        IrisRange.fromValue(0x10000);
    }

    /**
     * Test toString() returns a non-null string containing the value in hex.
     */
    @Test
    public void toStringReturnsNonNullHexString() {
        IrisRange range = new IrisRange(IrisRange.OVERFLOW_0002);
        String str = range.toString();
        assertNotNull(str);
        assertTrue(str.contains(Integer.toHexString(IrisRange.OVERFLOW_0002)));
    }
}