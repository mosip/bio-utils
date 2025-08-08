package io.mosip.biometrics.util.face;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link TemporalSequenceFlags} class to verify the handling
 * of temporal sequence flags according to Table 3 of ISO/IEC 19794-5-2011.
 */
class TemporalSequenceFlagsTest {

    /**
     * Verifies that fromValue method returns the same input value for valid ranges,
     * and that constructor, value() and toString() methods work correctly.
     * Tests edge cases (0x0000, 0xFFFF) and a middle value (0x7FFF).
     */
    @Test
    void fromValueWithValidRangeValuesReturnsSameValue() {
        int[] samples = {
                TemporalSequenceFlags.ONE_REPRESENTATION,
                0x7FFF,
                TemporalSequenceFlags.TEMPOROL_REPRESENTATION_TAKEN_AT_REGULAR_INTERVAL_EXCEEDING_FFFF
        };

        for (int value : samples) {
            int result = TemporalSequenceFlags.fromValue(value);
            assertEquals(value, result);

            TemporalSequenceFlags flagObj = new TemporalSequenceFlags(value);
            assertEquals(value, flagObj.value());
            assertTrue(flagObj.toString().contains(Integer.toHexString(value)));
        }
    }

    /**
     * Verifies that fromValue method throws IllegalArgumentException for values
     * outside the valid range (below 0x0000 or above 0xFFFF).
     */
    @Test
    void fromValueWithInvalidValuesThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> TemporalSequenceFlags.fromValue(-1));
        assertThrows(IllegalArgumentException.class, () -> TemporalSequenceFlags.fromValue(0x1_0000));
    }
}