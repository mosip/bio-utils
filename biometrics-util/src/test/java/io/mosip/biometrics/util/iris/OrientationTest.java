package io.mosip.biometrics.util.iris;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Coverage for iris {@link Orientation}.
 */
class OrientationTest {

    @Test
    void valueFromValueAndToString() {
        Orientation orientation = new Orientation(Orientation.BASE);
        assertEquals(Orientation.BASE, orientation.value());
        assertEquals(Orientation.FLIPPED, Orientation.fromValue(Orientation.FLIPPED));
        assertNotNull(orientation.toString());
        assertThrows(IllegalArgumentException.class, () -> Orientation.fromValue(5));
    }
}
