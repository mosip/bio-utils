package io.mosip.biometrics.util.face;

import static org.junit.Assert.*;
import org.junit.Test;

public class GenderTest {

    /**
     * Tests Gender constructor with valid value
     */
    @Test
    public void constructor_validValue_createsGender() {
        int value = Gender.MALE;

        Gender gender = new Gender(value);

        assertNotNull(gender);
        assertEquals(value, gender.value());
    }

    /**
     * Tests value method returns correct value
     */
    @Test
    public void value_validGender_returnsCorrectValue() {
        Gender gender = new Gender(Gender.FEMALE);

        int result = gender.value();

        assertEquals(Gender.FEMALE, result);
    }

    /**
     * Tests fromValue method with valid unspecified value
     */
    @Test
    public void fromValue_validUnspecifiedValue_returnsValue() {
        int result = Gender.fromValue(Gender.UNSPECIFIED);

        assertEquals(Gender.UNSPECIFIED, result);
    }

    /**
     * Tests fromValue method with valid male value
     */
    @Test
    public void fromValue_validMaleValue_returnsValue() {
        int result = Gender.fromValue(Gender.MALE);

        assertEquals(Gender.MALE, result);
    }

    /**
     * Tests fromValue method with valid female value
     */
    @Test
    public void fromValue_validFemaleValue_returnsValue() {
        int result = Gender.fromValue(Gender.FEMALE);

        assertEquals(Gender.FEMALE, result);
    }

    /**
     * Tests fromValue method with valid unknown value
     */
    @Test
    public void fromValue_validUnknownValue_returnsValue() {
        int result = Gender.fromValue(Gender.UNKNOWN);

        assertEquals(Gender.UNKNOWN, result);
    }

    /**
     * Tests fromValue method with invalid value throws exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValue_invalidValue_throwsIllegalArgumentException() {
        Gender.fromValue(0x99);
    }

    /**
     * Tests toString method returns non-null string
     */
    @Test
    public void toString_validGender_returnsNonNullString() {
        Gender gender = new Gender(Gender.MALE);

        String result = gender.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
    }

    /**
     * Tests all constant values are correct
     */
    @Test
    public void constants_allValues_areCorrect() {
        assertEquals(0x00, Gender.UNSPECIFIED);
        assertEquals(0x01, Gender.MALE);
        assertEquals(0x02, Gender.FEMALE);
        assertEquals(0xFF, Gender.UNKNOWN);
    }
}