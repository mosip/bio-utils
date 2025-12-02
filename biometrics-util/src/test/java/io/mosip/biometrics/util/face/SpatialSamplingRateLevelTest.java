package io.mosip.biometrics.util.face;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link SpatialSamplingRateLevel} class to verify the handling
 * of spatial sampling rate levels according to ISO/IEC 19794-5 standard.
 */
class SpatialSamplingRateLevelTest {

    /**
     * Verifies that fromValue method returns the same input value for valid range,
     * and that constructor, value() and toString() methods work correctly for each
     * valid sampling rate level (0x00 through 0x07).
     */
    @Test
    void fromValueWithValidRangeValuesReturnsSameValue() {
        for (int i = SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_180;
             i <= SpatialSamplingRateLevel.SPATIAL_SAMPLING_RATE_LEVEL_750; i++) {
            int val = SpatialSamplingRateLevel.fromValue(i);
            assertEquals(i, val);

            SpatialSamplingRateLevel level = new SpatialSamplingRateLevel(i);
            assertEquals(i, level.value());
            assertTrue(level.toString().contains(Integer.toHexString(i)));
        }
    }

    /**
     * Verifies that fromValue method throws IllegalArgumentException for values
     * outside the valid range (below 0x00 or above 0x07).
     */
    @Test
    void fromValueWithInvalidValuesThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> SpatialSamplingRateLevel.fromValue(-1));
        assertThrows(IllegalArgumentException.class, () -> SpatialSamplingRateLevel.fromValue(0x09));
    }
}