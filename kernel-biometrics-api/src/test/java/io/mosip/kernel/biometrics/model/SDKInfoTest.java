package io.mosip.kernel.biometrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.model.SDKInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.RegistryIDType;

/**
 * Unit tests for {@link SDKInfo}.
 */
class SDKInfoTest {

    private SDKInfo sdkInfo;
    private final String testApiVersion = "1.0.0";
    private final String testSdkVersion = "2.1.0";
    private final String testOrganization = "MOSIP";
    private final String testType = "BIOMETRIC_SDK";

    @BeforeEach
    void setUp() {
        sdkInfo = new SDKInfo(testApiVersion, testSdkVersion, testOrganization, testType);
    }

    /**
     * Tests constructor initializes all fields correctly.
     */
    @Test
    void constructorInitializesFieldsCorrectly() {
        SDKInfo newSdkInfo = new SDKInfo("1.2.0", "3.0.0", "TestOrg", "SDK_TYPE");

        assertEquals("1.2.0", newSdkInfo.getApiVersion());
        assertEquals("3.0.0", newSdkInfo.getSdkVersion());
        assertNotNull(newSdkInfo.getProductOwner());
        assertEquals("TestOrg", newSdkInfo.getProductOwner().getOrganization());
        assertEquals("SDK_TYPE", newSdkInfo.getProductOwner().getType());
        assertNotNull(newSdkInfo.getSupportedModalities());
        assertTrue(newSdkInfo.getSupportedModalities().isEmpty());
        assertNotNull(newSdkInfo.getSupportedMethods());
        assertTrue(newSdkInfo.getSupportedMethods().isEmpty());
        assertTrue(newSdkInfo.getSupportedMethods() instanceof EnumMap);
        assertNotNull(newSdkInfo.getOtherInfo());
        assertTrue(newSdkInfo.getOtherInfo().isEmpty());
    }

    /**
     * Tests setter and getter for apiVersion field.
     */
    @Test
    void apiVersionSetterGetterWorksCorrectly() {
        sdkInfo.setApiVersion("2.0.0");
        assertEquals("2.0.0", sdkInfo.getApiVersion());
    }

    /**
     * Tests setter and getter for sdkVersion field.
     */
    @Test
    void sdkVersionSetterGetterWorksCorrectly() {
        sdkInfo.setSdkVersion("3.5.1");
        assertEquals("3.5.1", sdkInfo.getSdkVersion());
    }

    /**
     * Tests setter and getter for supportedModalities field.
     */
    @Test
    void supportedModalitiesSetterGetterWorksCorrectly() {
        List<BiometricType> modalities = new ArrayList<>();
        modalities.add(BiometricType.FINGER);
        modalities.add(BiometricType.IRIS);

        sdkInfo.setSupportedModalities(modalities);
        assertEquals(modalities, sdkInfo.getSupportedModalities());
        assertEquals(2, sdkInfo.getSupportedModalities().size());
        assertTrue(sdkInfo.getSupportedModalities().contains(BiometricType.FINGER));
        assertTrue(sdkInfo.getSupportedModalities().contains(BiometricType.IRIS));
    }

    /**
     * Tests setter and getter for supportedMethods field.
     */
    @Test
    void supportedMethodsSetterGetterWorksCorrectly() {
        Map<BiometricFunction, List<BiometricType>> methods = new EnumMap<>(BiometricFunction.class);
        List<BiometricType> extractTypes = new ArrayList<>();
        extractTypes.add(BiometricType.FINGER);
        extractTypes.add(BiometricType.FACE);
        methods.put(BiometricFunction.EXTRACT, extractTypes);

        sdkInfo.setSupportedMethods(methods);
        assertEquals(methods, sdkInfo.getSupportedMethods());
        assertEquals(1, sdkInfo.getSupportedMethods().size());
        assertTrue(sdkInfo.getSupportedMethods().containsKey(BiometricFunction.EXTRACT));
        assertEquals(2, sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).size());
    }

    /**
     * Tests setter and getter for otherInfo field.
     */
    @Test
    void otherInfoSetterGetterWorksCorrectly() {
        Map<String, String> otherInfo = new HashMap<>();
        otherInfo.put("licenseExpiry", "2024-12-31");
        otherInfo.put("vendor", "BiometricTech Inc");

        sdkInfo.setOtherInfo(otherInfo);
        assertEquals(otherInfo, sdkInfo.getOtherInfo());
        assertEquals(2, sdkInfo.getOtherInfo().size());
        assertEquals("2024-12-31", sdkInfo.getOtherInfo().get("licenseExpiry"));
        assertEquals("BiometricTech Inc", sdkInfo.getOtherInfo().get("vendor"));
    }

    /**
     * Tests setter and getter for productOwner field.
     */
    @Test
    void productOwnerSetterGetterWorksCorrectly() {
        RegistryIDType newOwner = new RegistryIDType("NewOrg", "NEW_TYPE");
        sdkInfo.setProductOwner(newOwner);
        assertEquals(newOwner, sdkInfo.getProductOwner());
        assertEquals("NewOrg", sdkInfo.getProductOwner().getOrganization());
        assertEquals("NEW_TYPE", sdkInfo.getProductOwner().getType());
    }

    /**
     * Tests withSupportedMethod adds method correctly.
     */
    @Test
    void withSupportedMethodAddsMethodCorrectly() {
        SDKInfo result = sdkInfo.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER);

        assertEquals(sdkInfo, result); // Should return same instance for fluent interface
        assertEquals(1, sdkInfo.getSupportedMethods().size());
        assertTrue(sdkInfo.getSupportedMethods().containsKey(BiometricFunction.EXTRACT));
        assertEquals(1, sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).size());
        assertTrue(sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).contains(BiometricType.FINGER));
    }

    /**
     * Tests withSupportedMethod with multiple calls for same function.
     */
    @Test
    void withSupportedMethodMultipleCallsSameFunctionWorksCorrectly() {
        sdkInfo.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER)
                .withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.IRIS)
                .withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FACE);

        assertEquals(1, sdkInfo.getSupportedMethods().size());
        assertEquals(3, sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).size());
        assertTrue(sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).contains(BiometricType.FINGER));
        assertTrue(sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).contains(BiometricType.IRIS));
        assertTrue(sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).contains(BiometricType.FACE));
    }

    /**
     * Tests withSupportedMethod with multiple functions.
     */
    @Test
    void withSupportedMethodMultipleFunctionsWorksCorrectly() {
        sdkInfo.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER)
                .withSupportedMethod(BiometricFunction.MATCH, BiometricType.IRIS)
                .withSupportedMethod(BiometricFunction.QUALITY_CHECK, BiometricType.FACE);

        assertEquals(3, sdkInfo.getSupportedMethods().size());
        assertTrue(sdkInfo.getSupportedMethods().containsKey(BiometricFunction.EXTRACT));
        assertTrue(sdkInfo.getSupportedMethods().containsKey(BiometricFunction.MATCH));
        assertTrue(sdkInfo.getSupportedMethods().containsKey(BiometricFunction.QUALITY_CHECK));

        assertEquals(1, sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).size());
        assertEquals(1, sdkInfo.getSupportedMethods().get(BiometricFunction.MATCH).size());
        assertEquals(1, sdkInfo.getSupportedMethods().get(BiometricFunction.QUALITY_CHECK).size());
    }

    /**
     * Tests adding items to supportedModalities list.
     */
    @Test
    void addingItemsToSupportedModalitiesListWorksCorrectly() {
        sdkInfo.getSupportedModalities().add(BiometricType.FINGER);
        sdkInfo.getSupportedModalities().add(BiometricType.VOICE);

        assertEquals(2, sdkInfo.getSupportedModalities().size());
        assertTrue(sdkInfo.getSupportedModalities().contains(BiometricType.FINGER));
        assertTrue(sdkInfo.getSupportedModalities().contains(BiometricType.VOICE));
    }

    /**
     * Tests adding items to otherInfo map.
     */
    @Test
    void addingItemsToOtherInfoMapWorksCorrectly() {
        sdkInfo.getOtherInfo().put("version", "1.0");
        sdkInfo.getOtherInfo().put("buildDate", "2023-10-15");

        assertEquals(2, sdkInfo.getOtherInfo().size());
        assertEquals("1.0", sdkInfo.getOtherInfo().get("version"));
        assertEquals("2023-10-15", sdkInfo.getOtherInfo().get("buildDate"));
    }

    /**
     * Tests that collections are mutable.
     */
    @Test
    void collectionsAreMutable() {
        List<BiometricType> originalModalities = sdkInfo.getSupportedModalities();
        Map<BiometricFunction, List<BiometricType>> originalMethods = sdkInfo.getSupportedMethods();
        Map<String, String> originalOtherInfo = sdkInfo.getOtherInfo();

        originalModalities.add(BiometricType.FINGER);
        originalOtherInfo.put("testKey", "testValue");

        assertEquals(1, sdkInfo.getSupportedModalities().size());
        assertEquals(1, sdkInfo.getOtherInfo().size());
        assertTrue(sdkInfo.getSupportedModalities().contains(BiometricType.FINGER));
        assertEquals("testValue", sdkInfo.getOtherInfo().get("testKey"));
    }

    /**
     * Tests setting null values for fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        sdkInfo.setApiVersion(null);
        sdkInfo.setSdkVersion(null);
        sdkInfo.setSupportedModalities(null);
        sdkInfo.setSupportedMethods(null);
        sdkInfo.setOtherInfo(null);
        sdkInfo.setProductOwner(null);

        assertNull(sdkInfo.getApiVersion());
        assertNull(sdkInfo.getSdkVersion());
        assertNull(sdkInfo.getSupportedModalities());
        assertNull(sdkInfo.getSupportedMethods());
        assertNull(sdkInfo.getOtherInfo());
        assertNull(sdkInfo.getProductOwner());
    }

    /**
     * Tests constructor with null values.
     */
    @Test
    void constructorWithNullValuesWorksCorrectly() {
        SDKInfo nullSdkInfo = new SDKInfo(null, null, null, null);

        assertNull(nullSdkInfo.getApiVersion());
        assertNull(nullSdkInfo.getSdkVersion());
        assertNotNull(nullSdkInfo.getProductOwner());
        assertNull(nullSdkInfo.getProductOwner().getOrganization());
        assertNull(nullSdkInfo.getProductOwner().getType());
        assertNotNull(nullSdkInfo.getSupportedModalities());
        assertNotNull(nullSdkInfo.getSupportedMethods());
        assertNotNull(nullSdkInfo.getOtherInfo());
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        assertTrue(sdkInfo.equals(sdkInfo));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        assertFalse(sdkInfo.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        assertFalse(sdkInfo.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        SDKInfo sdkInfo1 = new SDKInfo("1.0", "2.0", "TestOrg", "SDK");
        sdkInfo1.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER);
        sdkInfo1.getOtherInfo().put("key", "value");

        SDKInfo sdkInfo2 = new SDKInfo("1.0", "2.0", "TestOrg", "SDK");
        sdkInfo2.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER);
        sdkInfo2.getOtherInfo().put("key", "value");

        assertTrue(sdkInfo1.equals(sdkInfo2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        sdkInfo.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER);
        int hash1 = sdkInfo.hashCode();
        int hash2 = sdkInfo.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        SDKInfo sdkInfo1 = new SDKInfo("1.0", "2.0", "TestOrg", "SDK");
        SDKInfo sdkInfo2 = new SDKInfo("1.0", "2.0", "TestOrg", "SDK");

        assertEquals(sdkInfo1.hashCode(), sdkInfo2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        String result = sdkInfo.toString();
        assertNotNull(result);
        assertTrue(result.contains("SDKInfo"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        sdkInfo.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER);
        sdkInfo.getOtherInfo().put("testKey", "testValue");

        String result = sdkInfo.toString();
        assertNotNull(result);
        assertTrue(result.contains("apiVersion"));
        assertTrue(result.contains("sdkVersion"));
        assertTrue(result.contains("supportedModalities"));
        assertTrue(result.contains("supportedMethods"));
        assertTrue(result.contains("otherInfo"));
        assertTrue(result.contains("productOwner"));
    }

    /**
     * Tests that constructor creates new instances for collections.
     */
    @Test
    void constructorCreatesNewInstancesForCollections() {
        SDKInfo sdkInfo1 = new SDKInfo("1.0", "2.0", "Org1", "Type1");
        SDKInfo sdkInfo2 = new SDKInfo("1.0", "2.0", "Org2", "Type2");

        assertNotSame(sdkInfo1.getSupportedModalities(), sdkInfo2.getSupportedModalities());
        assertNotSame(sdkInfo1.getSupportedMethods(), sdkInfo2.getSupportedMethods());
        assertNotSame(sdkInfo1.getOtherInfo(), sdkInfo2.getOtherInfo());
        assertNotSame(sdkInfo1.getProductOwner(), sdkInfo2.getProductOwner());
    }

    /**
     * Tests complete SDK info object setup.
     */
    @Test
    void completeSdkInfoObjectSetup() {
        sdkInfo.getSupportedModalities().add(BiometricType.FINGER);
        sdkInfo.getSupportedModalities().add(BiometricType.IRIS);
        sdkInfo.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER)
                .withSupportedMethod(BiometricFunction.MATCH, BiometricType.IRIS)
                .withSupportedMethod(BiometricFunction.QUALITY_CHECK, BiometricType.FACE);
        sdkInfo.getOtherInfo().put("licenseExpiry", "2024-12-31");
        sdkInfo.getOtherInfo().put("supportContact", "support@mosip.io");

        assertEquals(testApiVersion, sdkInfo.getApiVersion());
        assertEquals(testSdkVersion, sdkInfo.getSdkVersion());
        assertEquals(2, sdkInfo.getSupportedModalities().size());
        assertEquals(3, sdkInfo.getSupportedMethods().size());
        assertEquals(2, sdkInfo.getOtherInfo().size());
        assertEquals(testOrganization, sdkInfo.getProductOwner().getOrganization());
        assertEquals(testType, sdkInfo.getProductOwner().getType());
    }

    /**
     * Tests EnumMap specific behavior for supportedMethods.
     */
    @Test
    void enumMapSpecificBehaviorForSupportedMethods() {
        for (BiometricFunction function : BiometricFunction.values()) {
            sdkInfo.withSupportedMethod(function, BiometricType.FINGER);
        }

        assertEquals(BiometricFunction.values().length, sdkInfo.getSupportedMethods().size());

        assertTrue(sdkInfo.getSupportedMethods() instanceof EnumMap);
    }

    /**
     * Tests fluent interface chaining.
     */
    @Test
    void fluentInterfaceChainingWorksCorrectly() {
        SDKInfo result = sdkInfo
                .withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER)
                .withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.IRIS)
                .withSupportedMethod(BiometricFunction.MATCH, BiometricType.FINGER)
                .withSupportedMethod(BiometricFunction.QUALITY_CHECK, BiometricType.FACE);

        assertEquals(sdkInfo, result); // All calls should return same instance
        assertEquals(3, sdkInfo.getSupportedMethods().size());
        assertEquals(2, sdkInfo.getSupportedMethods().get(BiometricFunction.EXTRACT).size());
        assertEquals(1, sdkInfo.getSupportedMethods().get(BiometricFunction.MATCH).size());
        assertEquals(1, sdkInfo.getSupportedMethods().get(BiometricFunction.QUALITY_CHECK).size());
    }

    /**
     * Tests SDK info state after multiple modifications.
     */
    @Test
    void sdkInfoStateAfterMultipleModifications() {
        // Initial setup
        sdkInfo.getSupportedModalities().add(BiometricType.FINGER);
        sdkInfo.withSupportedMethod(BiometricFunction.EXTRACT, BiometricType.FINGER);
        sdkInfo.getOtherInfo().put("initial", "value");

        sdkInfo.setApiVersion("2.0.0");
        sdkInfo.getSupportedModalities().add(BiometricType.IRIS);
        sdkInfo.withSupportedMethod(BiometricFunction.MATCH, BiometricType.IRIS);
        sdkInfo.getOtherInfo().put("updated", "newValue");
        sdkInfo.getOtherInfo().remove("initial");

        assertEquals("2.0.0", sdkInfo.getApiVersion());
        assertEquals(2, sdkInfo.getSupportedModalities().size());
        assertEquals(2, sdkInfo.getSupportedMethods().size());
        assertEquals(1, sdkInfo.getOtherInfo().size());
        assertEquals("newValue", sdkInfo.getOtherInfo().get("updated"));
        assertFalse(sdkInfo.getOtherInfo().containsKey("initial"));
    }

    /**
     * Tests empty string values handling.
     */
    @Test
    void emptyStringValuesHandlingWorksCorrectly() {
        SDKInfo emptySdkInfo = new SDKInfo("", "", "", "");

        assertEquals("", emptySdkInfo.getApiVersion());
        assertEquals("", emptySdkInfo.getSdkVersion());
        assertEquals("", emptySdkInfo.getProductOwner().getOrganization());
        assertEquals("", emptySdkInfo.getProductOwner().getType());

        emptySdkInfo.getOtherInfo().put("", "emptyKey");
        emptySdkInfo.getOtherInfo().put("emptyValue", "");

        assertEquals(2, emptySdkInfo.getOtherInfo().size());
        assertEquals("emptyKey", emptySdkInfo.getOtherInfo().get(""));
        assertEquals("", emptySdkInfo.getOtherInfo().get("emptyValue"));
    }
}
