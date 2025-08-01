package io.mosip.biometrics.util.finger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests that exercise the internal helper logic of {@link FingerEncoder}
 * without invoking heavy native / image-dependent code paths.
 */
class FingerEncoderInternalTest {

    /**
     * Helper to invoke the private static getFingerPosition method via reflection.
     *
     * @param subType The input finger subtype string
     * @return The integer constant representing finger position
     * @throws Exception if reflection fails
     */
    private static int callGetFingerPosition(String subType) throws Exception {
        Method m = FingerEncoder.class.getDeclaredMethod("getFingerPosition", String.class);
        m.setAccessible(true);
        return (int) m.invoke(null, subType);
    }

    @Nested
    class GetFingerPositionMapping {

        @Test
        void getFingerPositionNullSubTypeReturnsUnknown() throws Exception {
            int pos = callGetFingerPosition(null);
            assertEquals(FingerPosition.UNKNOWN, pos);
        }

        @Test
        void getFingerPositionKnownSubTypesReturnCorrectConstants() throws Exception {
            assertAll(
                    () -> assertEquals(FingerPosition.RIGHT_THUMB, callGetFingerPosition("Right Thumb")),
                    () -> assertEquals(FingerPosition.LEFT_INDEX_FINGER, callGetFingerPosition("Left IndexFinger")),
                    () -> assertEquals(FingerPosition.LEFT_LITTLE_FINGER, callGetFingerPosition("Left LittleFinger"))
            );
        }

        @Test
        void getFingerPositionInvalidSubTypeReturnsUnknown() throws Exception {
            int pos = callGetFingerPosition("SomeRandomValue");
            assertEquals(FingerPosition.UNKNOWN, pos);
        }
    }
}