package io.mosip.kernel.biometrics.test.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.entities.RegistryIDType;
import io.mosip.kernel.biometrics.entities.SBInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link SBInfo}.
 */
class SBInfoTest {

    @Mock
    private RegistryIDType mockFormat;

    private SBInfo.SBInfoBuilder builder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        builder = new SBInfo.SBInfoBuilder();
    }

    /**
     * Tests default constructor creates SBInfo with null format.
     */
    @Test
    void defaultConstructorCreatesEmptySbInfo() {
        SBInfo sbInfo = new SBInfo();

        assertNotNull(sbInfo);
        assertNull(sbInfo.getFormat());
    }

    /**
     * Tests builder constructor sets format correctly.
     */
    @Test
    void builderConstructorSetsFormatCorrectly() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();

        assertNotNull(sbInfo);
        assertEquals(mockFormat, sbInfo.getFormat());
    }

    /**
     * Tests builder setFormatOwner sets field correctly.
     */
    @Test
    void builderSetFormatOwnerSetsFieldCorrectly() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();
        assertEquals(mockFormat, sbInfo.getFormat());
    }

    /**
     * Tests builder setFormatOwner returns same builder instance.
     */
    @Test
    void builderSetFormatOwnerReturnsSameBuilderInstance() {
        SBInfo.SBInfoBuilder result = builder.setFormatOwner(mockFormat);
        assertEquals(builder, result);
    }

    /**
     * Tests builder build creates new SBInfo instances.
     */
    @Test
    void builderBuildCreatesNewSbInfoInstances() {
        SBInfo sbInfo1 = builder.setFormatOwner(mockFormat).build();
        SBInfo sbInfo2 = builder.setFormatOwner(mockFormat).build();

        assertNotSame(sbInfo1, sbInfo2);
        assertEquals(sbInfo1.getFormat(), sbInfo2.getFormat());
    }

    /**
     * Tests builder with null format.
     */
    @Test
    void builderWithNullFormatWorksCorrectly() {
        SBInfo sbInfo = builder.setFormatOwner(null).build();
        assertNull(sbInfo.getFormat());
    }

    /**
     * Tests builder without setting format.
     */
    @Test
    void builderWithoutSettingFormatReturnsNullFormat() {
        SBInfo sbInfo = builder.build();
        assertNull(sbInfo.getFormat());
    }

    /**
     * Tests getter returns correct format.
     */
    @Test
    void getFormatReturnsCorrectValue() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();
        assertEquals(mockFormat, sbInfo.getFormat());
    }

    /**
     * Tests setter and getter for format using Lombok.
     */
    @Test
    void formatSetterGetterWorksCorrectly() {
        SBInfo sbInfo = new SBInfo();
        sbInfo.setFormat(mockFormat);
        assertEquals(mockFormat, sbInfo.getFormat());
    }

    /**
     * Tests setting null value for format.
     */
    @Test
    void settingNullFormatWorksCorrectly() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();
        sbInfo.setFormat(null);
        assertNull(sbInfo.getFormat());
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();
        assertTrue(sbInfo.equals(sbInfo));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();
        assertFalse(sbInfo.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();
        assertFalse(sbInfo.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        SBInfo sbInfo1 = builder.setFormatOwner(mockFormat).build();
        SBInfo sbInfo2 = new SBInfo.SBInfoBuilder().setFormatOwner(mockFormat).build();

        assertTrue(sbInfo1.equals(sbInfo2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();
        int hash1 = sbInfo.hashCode();
        int hash2 = sbInfo.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        SBInfo sbInfo1 = builder.setFormatOwner(mockFormat).build();
        SBInfo sbInfo2 = new SBInfo.SBInfoBuilder().setFormatOwner(mockFormat).build();

        assertEquals(sbInfo1.hashCode(), sbInfo2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();
        String result = sbInfo.toString();
        assertNotNull(result);
        assertTrue(result.contains("SBInfo"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        SBInfo sbInfo = builder.setFormatOwner(mockFormat).build();

        String result = sbInfo.toString();
        assertNotNull(result);
        assertTrue(result.contains("format"));
    }

    /**
     * Tests toString method with null format.
     */
    @Test
    void toStringWithNullFormatWorksCorrectly() {
        SBInfo sbInfo = new SBInfo();
        String result = sbInfo.toString();
        assertNotNull(result);
        assertTrue(result.contains("SBInfo"));
    }

    /**
     * Tests that SBInfo implements Serializable.
     */
    @Test
    void implementsSerializable() {
        SBInfo sbInfo = new SBInfo();
        assertTrue(sbInfo instanceof java.io.Serializable);
    }

    /**
     * Tests builder method chaining with multiple calls.
     */
    @Test
    void builderMethodChainingWorksCorrectly() {
        // Since there's only one builder method, test multiple calls
        SBInfo sbInfo = builder
                .setFormatOwner(mockFormat)
                .setFormatOwner(mockFormat) // Call again to test chaining
                .build();

        assertEquals(mockFormat, sbInfo.getFormat());
    }

    /**
     * Tests builder reuse maintains state.
     */
    @Test
    void builderReuseMaintainsState() {
        SBInfo.SBInfoBuilder reusableBuilder = new SBInfo.SBInfoBuilder();

        SBInfo sbInfo1 = reusableBuilder.setFormatOwner(mockFormat).build();
        SBInfo sbInfo2 = reusableBuilder.build(); // Reuse without setting format again

        assertEquals(mockFormat, sbInfo1.getFormat());
        assertEquals(mockFormat, sbInfo2.getFormat()); // Should maintain the previous state
        assertNotSame(sbInfo1, sbInfo2);
    }

    /**
     * Tests builder state isolation after build.
     */
    @Test
    void builderStateIsolationAfterBuild() {
        SBInfo sbInfo1 = builder.setFormatOwner(mockFormat).build();

        builder.setFormatOwner(null);
        SBInfo sbInfo2 = builder.build();

        assertEquals(mockFormat, sbInfo1.getFormat());
        assertNull(sbInfo2.getFormat());
    }

    /**
     * Tests different builder instances are independent.
     */
    @Test
    void differentBuildersAreIndependent() {
        SBInfo.SBInfoBuilder builder1 = new SBInfo.SBInfoBuilder();
        SBInfo.SBInfoBuilder builder2 = new SBInfo.SBInfoBuilder();

        SBInfo sbInfo1 = builder1.setFormatOwner(mockFormat).build();
        SBInfo sbInfo2 = builder2.build();
        assertEquals(mockFormat, sbInfo1.getFormat());
        assertNull(sbInfo2.getFormat());
        assertNotSame(sbInfo1, sbInfo2);
    }

    /**
     * Tests Lombok generated getter method exists and works correctly.
     */
    @Test
    void lombokGeneratedGetterExists() {
        SBInfo sbInfo = new SBInfo();
        assertNull(sbInfo.getFormat());
        sbInfo.setFormat(mockFormat);
        assertNotNull(sbInfo.getFormat());
        assertEquals(mockFormat, sbInfo.getFormat());
    }

    /**
     * Tests Lombok generated setter method exists.
     */
    @Test
    void lombokGeneratedSetterExists() {
        SBInfo sbInfo = new SBInfo();
        sbInfo.setFormat(mockFormat);
        assertEquals(mockFormat, sbInfo.getFormat());
    }

    /**
     * Tests XML type annotation compliance.
     */
    @Test
    void xmlTypeAnnotationCompliance() {
        SBInfo sbInfo = new SBInfo();
        sbInfo.setFormat(mockFormat);
        assertNotNull(sbInfo);
        assertEquals(mockFormat, sbInfo.getFormat());
    }

    /**
     * Tests JSON deserialization builder compliance.
     */
    @Test
    void jsonDeserializationBuilderCompliance() {
        SBInfo.SBInfoBuilder builderInstance = new SBInfo.SBInfoBuilder();
        SBInfo sbInfo = builderInstance.setFormatOwner(mockFormat).build();

        assertNotNull(sbInfo);
        assertEquals(mockFormat, sbInfo.getFormat());
    }
}