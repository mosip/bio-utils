package io.mosip.biometrics.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ISOStandardsValidator.isValidCaptureDateTime method.
 */
class ISOStandardsValidatorTest {

    static class TestValidator extends ISOStandardsValidator {
    }

    /** Valid full date and time. */
    @Test
    void isValidCaptureDateTime_validFullDateTime_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 5, 10, 12, 30, 45, 500));
    }

    /** Invalid month and day combination. */
    @Test
    void isValidCaptureDateTime_invalidMonthDay_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 13, 32, 12, 30, 45, 500));
    }

    /** Valid only date provided. */
    @Test
    void isValidCaptureDateTime_dateOnlyFieldsProvided_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 5, 10, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /** Invalid year zero. */
    @Test
    void isValidCaptureDateTime_yearZero_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(0, 5, 10, 12, 30, 45, 500));
    }

    /** All fields unprovided. */
    @Test
    void isValidCaptureDateTime_allFieldsUnprovided_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /** Valid time only. */
    @Test
    void isValidCaptureDateTime_timeOnlyFieldsProvided_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 5, 15, 30, 250));
    }

    /** Invalid hour time only. */
    @Test
    void isValidCaptureDateTime_timeOnlyInvalidHour_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 25, 15, 30, 250));
    }

    /** Invalid milliseconds. */
    @Test
    void isValidCaptureDateTime_timeOnlyInvalidMilliseconds_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 5, 15, 30, 4000));
    }

    /** Minimum values. */
    @Test
    void isValidCaptureDateTime_minimumValues_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(1, 1, 1, 0, 0, 0, 0));
    }

    /** Invalid day zero. */
    @Test
    void isValidCaptureDateTime_dayZero_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 1, 0, 0, 0, 0, 0));
    }

    /** Valid Feb 29 on leap year. */
    @Test
    void isValidCaptureDateTime_leapYearFeb29_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2024, 2, 29, 10, 10, 10, 10));
    }

    /** Maximum valid values. */
    @Test
    void isValidCaptureDateTime_maximumValues_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(9999, 12, 31, 23, 59, 59, 999));
    }

    /** Invalid hour. */
    @Test
    void isValidCaptureDateTime_invalidHourBoundary_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 6, 15, 24, 0, 0, 0));
    }

    /** All negative values. */
    @Test
    void isValidCaptureDateTime_negativeValues_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(-1, -1, -1, -1, -1, -1, -1));
    }

    /** Valid upper limits for time only. */
    @Test
    void isValidCaptureDateTime_timeOnlyUpperLimits_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 23, 59, 59, 999));
    }

    /** Partially invalid time values. */
    @Test
    void isValidCaptureDateTime_partialInvalidCombination_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 5, 10, 25, 60, 60, 1000));
    }

    /** Only year provided. */
    @Test
    void isValidCaptureDateTime_onlyYearProvided_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /** All zeros provided. */
    @Test
    void isValidCaptureDateTime_allZeros_false() {
        assertFalse(new TestValidator().isValidCaptureDateTime(0, 0, 0, 0, 0, 0, 0));
    }

    /** Valid April 30. */
    @Test
    void isValidCaptureDateTime_validApril30_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 4, 30, 12, 0, 0, 0));
    }

    /** Valid Feb 28 non-leap year. */
    @Test
    void isValidCaptureDateTime_validFeb28_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 2, 28, 0, 0, 0, 0));
    }

    /** Valid end of year. */
    @Test
    void isValidCaptureDateTime_validYearEnd_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 12, 31, 23, 59, 59, 999));
    }

    /** Valid mid-year date. */
    @Test
    void isValidCaptureDateTime_validMidYear_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 6, 15, 6, 30, 30, 300));
    }

    /** Valid low year. */
    @Test
    void isValidCaptureDateTime_lowYear_true() {
        assertTrue(new TestValidator().isValidCaptureDateTime(1, 1, 1, 0, 0, 0, 0));
    }
}