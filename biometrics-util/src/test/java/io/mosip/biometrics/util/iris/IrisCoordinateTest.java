package io.mosip.biometrics.util.iris;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link IrisCoordinate}.
 */
public class IrisCoordinateTest {

    /**
     * value_withValidConstructorInput_returnsCorrectValue
     */
    @Test
    public void valueReturnsCorrectValue() {
        IrisCoordinate coordinate = new IrisCoordinate(IrisCoordinate.COORDINATE_0001);
        assertEquals(IrisCoordinate.COORDINATE_0001, coordinate.value());
    }

    /**
     * fromValue_withMinValidValue_returnsCoordinateUndefined
     */
    @Test
    public void fromValueWithMinimumReturnsUndefined() {
        assertEquals(IrisCoordinate.COORDINATE_UNDEFINIED, IrisCoordinate.fromValue(IrisCoordinate.COORDINATE_UNDEFINIED));
    }

    /**
     * fromValue_withMaxValidValue_returnsCoordinateFFFF
     */
    @Test
    public void fromValueWithMaximumReturnsCoordinateFFFF() {
        assertEquals(IrisCoordinate.COORDINATE_FFFF, IrisCoordinate.fromValue(IrisCoordinate.COORDINATE_FFFF));
    }

    /**
     * fromValue_withMiddleValidValue_returnsCoordinate0001
     */
    @Test
    public void fromValueWithMiddleReturnsCoordinate0001() {
        assertEquals(IrisCoordinate.COORDINATE_0001, IrisCoordinate.fromValue(IrisCoordinate.COORDINATE_0001));
    }

    /**
     * fromValue_withValueBelowRange_throwsIllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueBelowRangeThrowsException() {
        IrisCoordinate.fromValue(-1);
    }

    /**
     * fromValue_withValueAboveRange_throwsIllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueAboveRangeThrowsException() {
        IrisCoordinate.fromValue(0x10000);
    }

    /**
     * toString_withValidCoordinate_returnsNonNullHexString
     */
    @Test
    public void toStringReturnsNonNullHexString() {
        IrisCoordinate coordinate = new IrisCoordinate(IrisCoordinate.COORDINATE_0001);
        String result = coordinate.toString();
        assertNotNull(result);
        assertTrue(result.contains(Integer.toHexString(IrisCoordinate.COORDINATE_0001)));
    }
}