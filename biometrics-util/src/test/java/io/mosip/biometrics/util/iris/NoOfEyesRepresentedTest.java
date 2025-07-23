package io.mosip.biometrics.util.iris;

import static org.junit.Assert.*;
import org.junit.Test;

public class NoOfEyesRepresentedTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsNoOfEyesRepresented() {
        NoOfEyesRepresented noOfEyes = new NoOfEyesRepresented(NoOfEyesRepresented.LEFT_OR_RIGHT_EYE_PRESENT);

        assertEquals(NoOfEyesRepresented.LEFT_OR_RIGHT_EYE_PRESENT, noOfEyes.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validNoOfEyes_returnsCorrectValue() {
        NoOfEyesRepresented noOfEyes = new NoOfEyesRepresented(NoOfEyesRepresented.LEFT_AND_RIGHT_EYE_PRESENT);

        int result = noOfEyes.value();

        assertEquals(NoOfEyesRepresented.LEFT_AND_RIGHT_EYE_PRESENT, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = NoOfEyesRepresented.fromValue(NoOfEyesRepresented.UNKNOWN);

        assertEquals(NoOfEyesRepresented.UNKNOWN, result);
    }

    /**
     * Tests fromValue method with valid maximum value
     */
    @Test
    public void fromValue_validMaximumValue_returnsValue() {
        int result = NoOfEyesRepresented.fromValue(NoOfEyesRepresented.LEFT_AND_RIGHT_EYE_PRESENT);

        assertEquals(NoOfEyesRepresented.LEFT_AND_RIGHT_EYE_PRESENT, result);
    }

    /**
     * Tests fromValue method with valid single eye value
     */
    @Test
    public void fromValue_validSingleEyeValue_returnsValue() {
        int result = NoOfEyesRepresented.fromValue(NoOfEyesRepresented.LEFT_OR_RIGHT_EYE_PRESENT);

        assertEquals(NoOfEyesRepresented.LEFT_OR_RIGHT_EYE_PRESENT, result);
    }

    /**
     * Tests fromValue method with invalid value below range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueBelowRange_throwsIllegalArgumentException() {
        NoOfEyesRepresented.fromValue(-1);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        NoOfEyesRepresented.fromValue(0x03);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validNoOfEyes_returnsFormattedString() {
        NoOfEyesRepresented noOfEyes = new NoOfEyesRepresented(NoOfEyesRepresented.LEFT_AND_RIGHT_EYE_PRESENT);

        String result = noOfEyes.toString();

        assertNotNull(result);
        assertTrue(result.contains("2"));
    }
}