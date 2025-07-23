package io.mosip.biometrics.util.iris;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link IrisCoordinate}.
 */
public class IrisCoordinateTest {

    /**
     * Test constructor and value() method with valid value.
     */
    @Test
    public void testConstructorAndValue_Valid() {
        IrisCoordinate coordinate = new IrisCoordinate(IrisCoordinate.COORDINATE_0001);
        assertEquals(IrisCoordinate.COORDINATE_0001, coordinate.value());
    }

    /**
     * Test fromValue() with minimum valid value.
     */
    @Test
    public void testFromValue_MinValid() {
        assertEquals(IrisCoordinate.COORDINATE_UNDEFINIED, IrisCoordinate.fromValue(IrisCoordinate.COORDINATE_UNDEFINIED));
    }

    /**
     * Test fromValue() with maximum valid value.
     */
    @Test
    public void testFromValue_MaxValid() {
        assertEquals(IrisCoordinate.COORDINATE_FFFF, IrisCoordinate.fromValue(IrisCoordinate.COORDINATE_FFFF));
    }

    /**
     * Test fromValue() with a valid middle value.
     */
    @Test
    public void testFromValue_MiddleValid() {
        assertEquals(IrisCoordinate.COORDINATE_0001, IrisCoordinate.fromValue(IrisCoordinate.COORDINATE_0001));
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value below range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromValue_BelowRange() {
        IrisCoordinate.fromValue(-1);
    }

    /**
     * Test fromValue() throws IllegalArgumentException for value above range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromValue_AboveRange() {
        IrisCoordinate.fromValue(0x10000);
    }

    /**
     * Test toString() returns a non-null string containing the value in hex.
     */
    @Test
    public void testToString() {
        IrisCoordinate coordinate = new IrisCoordinate(IrisCoordinate.COORDINATE_0001);
        String str = coordinate.toString();
        assertNotNull(str);
        assertTrue(str.contains(Integer.toHexString(IrisCoordinate.COORDINATE_0001)));
    }
} 