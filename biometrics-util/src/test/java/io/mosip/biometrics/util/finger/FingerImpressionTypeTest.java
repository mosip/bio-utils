package io.mosip.biometrics.util.finger;

import static org.junit.Assert.*;
import org.junit.Test;

public class FingerImpressionTypeTest {

    /**
     * Tests constructor with valid value
     */
    @Test
    public void constructor_validValue_createsFingerImpressionType() {
        FingerImpressionType impressionType = new FingerImpressionType(FingerImpressionType.LIVE_SCAN_PLAIN);

        assertEquals(FingerImpressionType.LIVE_SCAN_PLAIN, impressionType.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validImpressionType_returnsCorrectValue() {
        FingerImpressionType impressionType = new FingerImpressionType(FingerImpressionType.LIVE_SCAN_ROLLED);

        int result = impressionType.value();

        assertEquals(FingerImpressionType.LIVE_SCAN_ROLLED, result);
    }

    /**
     * Tests fromValue method with valid minimum value
     */
    @Test
    public void fromValue_validMinimumValue_returnsValue() {
        int result = FingerImpressionType.fromValue(FingerImpressionType.LIVE_SCAN_PLAIN);

        assertEquals(FingerImpressionType.LIVE_SCAN_PLAIN, result);
    }

    /**
     * Tests fromValue method with valid maximum value in first range
     */
    @Test
    public void fromValue_validMaximumValueInFirstRange_returnsValue() {
        int result = FingerImpressionType.fromValue(FingerImpressionType.LATENT_PALM_LIFT);

        assertEquals(FingerImpressionType.LATENT_PALM_LIFT, result);
    }

    /**
     * Tests fromValue method with valid optical contactless value
     */
    @Test
    public void fromValue_validOpticalContactlessValue_returnsValue() {
        int result = FingerImpressionType.fromValue(FingerImpressionType.LIVE_SCAN_OPTICAL_CONTACTLESS_PLAIN);

        assertEquals(FingerImpressionType.LIVE_SCAN_OPTICAL_CONTACTLESS_PLAIN, result);
    }

    /**
     * Tests fromValue method with valid other value
     */
    @Test
    public void fromValue_validOtherValue_returnsValue() {
        int result = FingerImpressionType.fromValue(FingerImpressionType.OTHER);

        assertEquals(FingerImpressionType.OTHER, result);
    }

    /**
     * Tests fromValue method with valid unknown value
     */
    @Test
    public void fromValue_validUnknownValue_returnsValue() {
        int result = FingerImpressionType.fromValue(FingerImpressionType.UNKNOWN);

        assertEquals(FingerImpressionType.UNKNOWN, result);
    }

    /**
     * Tests fromValue method with invalid value in reserved range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueInReservedRange_throwsIllegalArgumentException() {
        FingerImpressionType.fromValue(0x14);
    }

    /**
     * Tests fromValue method with invalid value above range
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValueAboveRange_throwsIllegalArgumentException() {
        FingerImpressionType.fromValue(0x1E);
    }

    /**
     * Tests toString method returns formatted string
     */
    @Test
    public void toString_validImpressionType_returnsFormattedString() {
        FingerImpressionType impressionType = new FingerImpressionType(FingerImpressionType.LATENT_IMPRESSION);

        String result = impressionType.toString();

        assertNotNull(result);
        assertTrue(result.contains("4"));
    }
}