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
    void isValidCaptureDateTimeValidFullDateTime() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 5, 10, 12, 30, 45, 500));
    }

    /** Invalid month and day combination. */
    @Test
    void isValidCaptureDateTimeInvalidMonthDay() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 13, 32, 12, 30, 45, 500));
    }

    /** Valid only date provided. */
    @Test
    void isValidCaptureDateTimeDateOnlyFieldsProvided() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 5, 10, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /** Invalid year zero. */
    @Test
    void isValidCaptureDateTimeYearZero() {
        assertFalse(new TestValidator().isValidCaptureDateTime(0, 5, 10, 12, 30, 45, 500));
    }

    /** All fields unprovided. */
    @Test
    void isValidCaptureDateTimeAllFieldsUnprovided() {
        assertTrue(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /** Valid time only. */
    @Test
    void isValidCaptureDateTimeTimeOnlyFieldsProvided() {
        assertTrue(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 5, 15, 30, 250));
    }

    /** Invalid hour time only. */
    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidHour() {
        assertFalse(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 25, 15, 30, 250));
    }

    /** Invalid milliseconds. */
    @Test
    void isValidCaptureDateTimeTimeOnlyInvalidMilliseconds() {
        assertFalse(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 5, 15, 30, 4000));
    }

    /** Minimum values. */
    @Test
    void isValidCaptureDateTimeMinimumValues() {
        assertTrue(new TestValidator().isValidCaptureDateTime(1, 1, 1, 0, 0, 0, 0));
    }

    /** Invalid day zero. */
    @Test
    void isValidCaptureDateTimeDayZero() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 1, 0, 0, 0, 0, 0));
    }

    /** Valid Feb 29 on leap year. */
    @Test
    void isValidCaptureDateTimeLeapYearFeb29() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2024, 2, 29, 10, 10, 10, 10));
    }

    /** Maximum valid values. */
    @Test
    void isValidCaptureDateTimeMaximumValues() {
        assertTrue(new TestValidator().isValidCaptureDateTime(9999, 12, 31, 23, 59, 59, 999));
    }

    /** Invalid hour. */
    @Test
    void isValidCaptureDateTimeInvalidHourBoundary() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 6, 15, 24, 0, 0, 0));
    }

    /** All negative values. */
    @Test
    void isValidCaptureDateTimeNegativeValues() {
        assertFalse(new TestValidator().isValidCaptureDateTime(-1, -1, -1, -1, -1, -1, -1));
    }

    /** Valid upper limits for time only. */
    @Test
    void isValidCaptureDateTimeTimeOnlyUpperLimits() {
        assertTrue(new TestValidator().isValidCaptureDateTime(0xFFFF, 0xFF, 0xFF, 23, 59, 59, 999));
    }

    /** Partially invalid time values. */
    @Test
    void isValidCaptureDateTimePartialInvalidCombination() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 5, 10, 25, 60, 60, 1000));
    }

    /** Only year provided. */
    @Test
    void isValidCaptureDateTimeOnlyYearProvided() {
        assertFalse(new TestValidator().isValidCaptureDateTime(2023, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFFFF));
    }

    /** All zeros provided. */
    @Test
    void isValidCaptureDateTimeAllZeros() {
        assertFalse(new TestValidator().isValidCaptureDateTime(0, 0, 0, 0, 0, 0, 0));
    }

    /** Valid April 30. */
    @Test
    void isValidCaptureDateTimeValidApril30() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 4, 30, 12, 0, 0, 0));
    }

    /** Valid Feb 28 non-leap year. */
    @Test
    void isValidCaptureDateTimeValidFeb28() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 2, 28, 0, 0, 0, 0));
    }

    /** Valid end of year. */
    @Test
    void isValidCaptureDateTimeValidYearEnd() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 12, 31, 23, 59, 59, 999));
    }

    /** Valid mid-year date. */
    @Test
    void isValidCaptureDateTimeValidMidYear() {
        assertTrue(new TestValidator().isValidCaptureDateTime(2023, 6, 15, 6, 30, 30, 300));
    }

    /** Valid low year. */
    @Test
    void isValidCaptureDateTimeLowYear() {
        assertTrue(new TestValidator().isValidCaptureDateTime(1, 1, 1, 0, 0, 0, 0));
    }
}