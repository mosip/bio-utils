package io.mosip.biometrics.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Test class for {@link Modality}
 */
public class ModalityTest {

    /**
     * Tests that all enum values are correctly defined.
     */
    @Test
    public void enumValuesAllValuesDefined() {
        assertEquals(4, Modality.values().length);
        assertEquals(Modality.UnSpecified, Modality.valueOf("UnSpecified"));
        assertEquals(Modality.Finger, Modality.valueOf("Finger"));
        assertEquals(Modality.Face, Modality.valueOf("Face"));
        assertEquals(Modality.Iris, Modality.valueOf("Iris"));
    }

    /**
     * Tests that the value() method returns the correct integer value for each enum constant.
     */
    @Test
    public void valueReturnsCorrectValue() {
        assertEquals(0x0000, Modality.UnSpecified.value());
        assertEquals(0x0001, Modality.Finger.value());
        assertEquals(0x0002, Modality.Face.value());
        assertEquals(0x0003, Modality.Iris.value());
    }

    /**
     * Tests that fromValue() returns the correct enum constant for valid input values.
     */
    @Test
    public void fromValueWithValidValueReturnsCorrespondingModality() {
        assertEquals(Modality.UnSpecified, Modality.fromValue(0x0000));
        assertEquals(Modality.Finger, Modality.fromValue(0x0001));
        assertEquals(Modality.Face, Modality.fromValue(0x0002));
        assertEquals(Modality.Iris, Modality.fromValue(0x0003));
    }

    /**
     * Tests that fromValue() throws IllegalArgumentException for invalid input values.
     */
    @Test
    public void fromValueWithInvalidValueThrowsException() {
        int invalidValue = 999;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Modality.fromValue(invalidValue)
        );
        assertEquals(String.valueOf(invalidValue), exception.getMessage());
    }

    /**
     * Tests that toString() returns the expected string representation for each enum constant.
     */
    @Test
    public void toStringReturnsExpectedFormat() {
        assertEquals("UnSpecified(0)", Modality.UnSpecified.toString());
        assertEquals("Finger(1)", Modality.Finger.toString());
        assertEquals("Face(2)", Modality.Face.toString());
        assertEquals("Iris(3)", Modality.Iris.toString());
    }

    /**
     * Tests that the enum constants are not null.
     */
    @Test
    public void enumConstantsNotNull() {
        assertNotNull(Modality.UnSpecified);
        assertNotNull(Modality.Finger);
        assertNotNull(Modality.Face);
        assertNotNull(Modality.Iris);
    }
}