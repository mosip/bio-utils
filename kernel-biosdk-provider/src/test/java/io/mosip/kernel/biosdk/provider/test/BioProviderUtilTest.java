package io.mosip.kernel.biosdk.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.mosip.kernel.biosdk.provider.util.BioProviderUtil;
import io.mosip.kernel.biosdk.provider.util.ErrorCode;
import io.mosip.kernel.biosdk.provider.util.ProviderConstants;
import io.mosip.kernel.core.bioapi.exception.BiometricException;

public class BioProviderUtilTest {

    /**
     * Tests that private constructor throws IllegalStateException when accessed.
     */
    @Test
    public void constructor_privateAccess_throwsIllegalStateException() {
        try {
            Constructor<?> constructor = BioProviderUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            fail("Expected IllegalStateException");
        } catch (Exception ex) {
            assertEquals("BioProviderUtil class", ex.getCause().getMessage());
        }
    }

    /**
     * Tests getSDKInstance when constructor arguments are invalid.
     */
    @Test
    public void getSDKInstance_invalidArguments_throwsSDKInitializationFailedException() {
        Map<String, String> params = new HashMap<>();
        params.put(ProviderConstants.CLASSNAME, "java.lang.String");
        params.put(ProviderConstants.ARGUMENTS, "invalid,args");

        try {
            BioProviderUtil.getSDKInstance(params);
            fail("Expected BiometricException");
        } catch (BiometricException ex) {
            assertEquals(ErrorCode.SDK_INITIALIZATION_FAILED.getErrorCode(), ex.getErrorCode());
        }
    }

    /**
     * Tests findRequiredMethod when method does not exist.
     */
    @Test
    public void findRequiredMethod_nonExistentMethod_throwsIllegalArgumentException() {
        try {
            BioProviderUtil.findRequiredMethod(String.class, "nonExistentMethod", int.class, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("Unable to find method nonExistentMethod(int, class java.lang.String) on class java.lang.String", ex.getMessage());
        }
    }

    /**
     * Tests successful method finding when method exists.
     */
    @Test
    public void findRequiredMethod_existingMethod_returnsMethod() {
        Method method = BioProviderUtil.findRequiredMethod(String.class, "charAt", int.class);
        assertNotNull(method);
        assertEquals("charAt", method.getName());
    }
}