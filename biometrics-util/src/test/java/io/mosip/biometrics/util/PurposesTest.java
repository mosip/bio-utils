package io.mosip.biometrics.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import io.mosip.biometrics.util.constant.BiometricUtilErrorCode;
import io.mosip.biometrics.util.exception.BiometricUtilException;

/**
 * Test class for {@link Purposes}
 */
public class PurposesTest {

    /**
     * Tests that all enum values are correctly defined.
     */
    @Test
    public void enumValuesAllValuesDefined() {
        assertEquals(2, Purposes.values().length);
        assertEquals(Purposes.AUTH, Purposes.valueOf("AUTH"));
        assertEquals(Purposes.REGISTRATION, Purposes.valueOf("REGISTRATION"));
    }

    /**
     * Tests that getCode() returns the correct string value for each enum constant.
     */
    @Test
    public void getCodeReturnsCorrectValue() {
        assertEquals("Auth", Purposes.AUTH.getCode());
        assertEquals("Registration", Purposes.REGISTRATION.getCode());
    }

    /**
     * Tests that fromCode() returns the correct enum constant for valid input values.
     */
    @Test
    public void fromCodeWithValidCodeReturnsCorrespondingPurpose() {
        assertEquals(Purposes.AUTH, Purposes.fromCode("Auth"));
        assertEquals(Purposes.REGISTRATION, Purposes.fromCode("Registration"));
    }

    /**
     * Tests that fromCode() throws BiometricUtilException for invalid input values.
     */
    @Test
    public void fromCodeWithInvalidCodeThrowsException() {
        String invalidCode = "InvalidPurpose";
        BiometricUtilException exception = assertThrows(
                BiometricUtilException.class,
                () -> Purposes.fromCode(invalidCode)
        );

        assertEquals(BiometricUtilErrorCode.INVALID_PURPOSE_TYPE_EXCEPTION.getErrorCode(),
                exception.getErrorCode());
        assertEquals(BiometricUtilErrorCode.INVALID_PURPOSE_TYPE_EXCEPTION.getErrorMessage(),
                exception.getErrorText());
    }

    /**
     * Tests that the enum constants are not null.
     */
    @Test
    public void enumConstantsNotNull() {
        assertNotNull(Purposes.AUTH);
        assertNotNull(Purposes.REGISTRATION);
    }

    /**
     * Tests that the error code and message in the exception match the expected values.
     */
    @Test
    public void errorCodeAndMessageAreAsExpected() {
        BiometricUtilException exception = assertThrows(
                BiometricUtilException.class,
                () -> Purposes.fromCode("InvalidCode")
        );

        assertEquals(BiometricUtilErrorCode.INVALID_PURPOSE_TYPE_EXCEPTION.getErrorCode(),
                exception.getErrorCode());
        assertEquals(BiometricUtilErrorCode.INVALID_PURPOSE_TYPE_EXCEPTION.getErrorMessage(),
                exception.getErrorText());
    }
}