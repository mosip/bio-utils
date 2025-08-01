package io.mosip.kernel.biometrics.test.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.mosip.kernel.biometrics.entities.VersionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link VersionType}.
 */
class VersionTypeTest {

    private VersionType.VersionTypeBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new VersionType.VersionTypeBuilder();
    }

    /**
     * Tests default constructor creates VersionType with zero values.
     */
    @Test
    void defaultConstructorCreatesEmptyVersionType() {
        VersionType versionType = new VersionType();

        assertNotNull(versionType);
        assertEquals(0, versionType.getMajor());
        assertEquals(0, versionType.getMinor());
    }

    /**
     * Tests all args constructor sets both fields correctly.
     */
    @Test
    void allArgsConstructorSetsBothFieldsCorrectly() {
        VersionType versionType = new VersionType(2, 5);

        assertEquals(2, versionType.getMajor());
        assertEquals(5, versionType.getMinor());
    }

    /**
     * Tests builder constructor sets fields correctly.
     */
    @Test
    void builderConstructorSetsFieldsCorrectly() {
        VersionType versionType = builder
                .withMajor(3)
                .withMinor(7)
                .build();

        assertEquals(3, versionType.getMajor());
        assertEquals(7, versionType.getMinor());
    }

    /**
     * Tests builder with major sets field correctly.
     */
    @Test
    void builderWithMajorSetsFieldCorrectly() {
        VersionType versionType = builder.withMajor(5).build();
        assertEquals(5, versionType.getMajor());
        assertEquals(0, versionType.getMinor());
    }

    /**
     * Tests builder with minor sets field correctly.
     */
    @Test
    void builderWithMinorSetsFieldCorrectly() {
        VersionType versionType = builder.withMinor(8).build();
        assertEquals(0, versionType.getMajor());
        assertEquals(8, versionType.getMinor());
    }

    /**
     * Tests builder chaining returns same builder instance.
     */
    @Test
    void builderChainingReturnsSameBuilderInstance() {
        VersionType.VersionTypeBuilder result = builder
                .withMajor(1)
                .withMinor(2);

        assertEquals(builder, result);
    }

    /**
     * Tests builder build creates new VersionType instances.
     */
    @Test
    void builderBuildCreatesNewVersionTypeInstances() {
        VersionType version1 = builder.withMajor(1).withMinor(0).build();
        VersionType version2 = builder.withMajor(1).withMinor(0).build();

        assertNotSame(version1, version2);
        assertEquals(version1.getMajor(), version2.getMajor());
        assertEquals(version1.getMinor(), version2.getMinor());
    }

    /**
     * Tests setter and getter for major version.
     */
    @Test
    void majorVersionSetterGetterWorksCorrectly() {
        VersionType versionType = new VersionType();
        versionType.setMajor(10);
        assertEquals(10, versionType.getMajor());
    }

    /**
     * Tests setter and getter for minor version.
     */
    @Test
    void minorVersionSetterGetterWorksCorrectly() {
        VersionType versionType = new VersionType();
        versionType.setMinor(15);
        assertEquals(15, versionType.getMinor());
    }

    /**
     * Tests version with zero values.
     */
    @Test
    void versionWithZeroValuesWorksCorrectly() {
        VersionType versionType = new VersionType(0, 0);
        assertEquals(0, versionType.getMajor());
        assertEquals(0, versionType.getMinor());
    }

    /**
     * Tests version with large values.
     */
    @Test
    void versionWithLargeValuesWorksCorrectly() {
        int largeMajor = Integer.MAX_VALUE;
        int largeMinor = Integer.MAX_VALUE - 1;

        VersionType versionType = new VersionType(largeMajor, largeMinor);
        assertEquals(largeMajor, versionType.getMajor());
        assertEquals(largeMinor, versionType.getMinor());
    }

    /**
     * Tests builder with zero values.
     */
    @Test
    void builderWithZeroValuesWorksCorrectly() {
        VersionType versionType = builder
                .withMajor(0)
                .withMinor(0)
                .build();

        assertEquals(0, versionType.getMajor());
        assertEquals(0, versionType.getMinor());
    }

    /**
     * Tests builder with large values.
     */
    @Test
    void builderWithLargeValuesWorksCorrectly() {
        int largeMajor = 999999;
        int largeMinor = 888888;

        VersionType versionType = builder
                .withMajor(largeMajor)
                .withMinor(largeMinor)
                .build();

        assertEquals(largeMajor, versionType.getMajor());
        assertEquals(largeMinor, versionType.getMinor());
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        VersionType versionType = new VersionType(1, 2);
        assertTrue(versionType.equals(versionType));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        VersionType versionType = new VersionType(1, 2);
        assertFalse(versionType.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        VersionType versionType = new VersionType(1, 2);
        assertFalse(versionType.equals("1.2"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        VersionType version1 = new VersionType(2, 5);
        VersionType version2 = new VersionType(2, 5);

        assertTrue(version1.equals(version2));
    }

    /**
     * Tests equals method with different major version returns false.
     */
    @Test
    void equalsWithDifferentMajorVersionReturnsFalse() {
        VersionType version1 = new VersionType(1, 5);
        VersionType version2 = new VersionType(2, 5);

        assertFalse(version1.equals(version2));
    }

    /**
     * Tests equals method with different minor version returns false.
     */
    @Test
    void equalsWithDifferentMinorVersionReturnsFalse() {
        VersionType version1 = new VersionType(2, 3);
        VersionType version2 = new VersionType(2, 4);

        assertFalse(version1.equals(version2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        VersionType versionType = new VersionType(3, 7);
        int hash1 = versionType.hashCode();
        int hash2 = versionType.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        VersionType version1 = new VersionType(4, 8);
        VersionType version2 = new VersionType(4, 8);

        assertEquals(version1.hashCode(), version2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        VersionType versionType = new VersionType(1, 5);
        String result = versionType.toString();
        assertNotNull(result);
        assertTrue(result.contains("VersionType"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        VersionType versionType = new VersionType(2, 7);

        String result = versionType.toString();
        assertNotNull(result);
        assertTrue(result.contains("major"));
        assertTrue(result.contains("minor"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("7"));
    }

    /**
     * Tests that VersionType implements Serializable.
     */
    @Test
    void implementsSerializable() {
        VersionType versionType = new VersionType();
        assertTrue(versionType instanceof java.io.Serializable);
    }

    /**
     * Tests builder reuse maintains state.
     */
    @Test
    void builderReuseMaintainsState() {
        VersionType.VersionTypeBuilder reusableBuilder = new VersionType.VersionTypeBuilder()
                .withMajor(5);

        VersionType version1 = reusableBuilder.withMinor(1).build();
        VersionType version2 = reusableBuilder.withMinor(2).build();

        assertEquals(5, version1.getMajor());
        assertEquals(5, version2.getMajor());
        assertEquals(1, version1.getMinor());
        assertEquals(2, version2.getMinor());
        assertNotSame(version1, version2);
    }

    /**
     * Tests different builder instances are independent.
     */
    @Test
    void differentBuildersAreIndependent() {
        VersionType.VersionTypeBuilder builder1 = new VersionType.VersionTypeBuilder();
        VersionType.VersionTypeBuilder builder2 = new VersionType.VersionTypeBuilder();

        VersionType version1 = builder1.withMajor(1).withMinor(0).build();
        VersionType version2 = builder2.withMajor(2).withMinor(0).build();

        assertEquals(1, version1.getMajor());
        assertEquals(2, version2.getMajor());
        assertNotSame(version1, version2);
    }

    /**
     * Tests version comparison scenarios.
     */
    @Test
    void versionComparisonScenarios() {
        VersionType version1_0 = new VersionType(1, 0);
        VersionType version1_1 = new VersionType(1, 1);
        VersionType version2_0 = new VersionType(2, 0);

        // Test different combinations for logical version comparison context
        assertFalse(version1_0.equals(version1_1));
        assertFalse(version1_0.equals(version2_0));
        assertFalse(version1_1.equals(version2_0));

        // Test same versions
        VersionType anotherVersion1_0 = new VersionType(1, 0);
        assertTrue(version1_0.equals(anotherVersion1_0));
    }

    /**
     * Tests typical version scenarios.
     */
    @Test
    void typicalVersionScenarios() {
        // Test common version patterns
        VersionType version1_0 = builder.withMajor(1).withMinor(0).build();
        VersionType version1_1 = builder.withMajor(1).withMinor(1).build();
        VersionType version2_0 = builder.withMajor(2).withMinor(0).build();

        assertEquals(1, version1_0.getMajor());
        assertEquals(0, version1_0.getMinor());

        assertEquals(1, version1_1.getMajor());
        assertEquals(1, version1_1.getMinor());

        assertEquals(2, version2_0.getMajor());
        assertEquals(0, version2_0.getMinor());
    }

    /**
     * Tests XML type annotation compliance.
     */
    @Test
    void xmlTypeAnnotationCompliance() {
        // Test that the object is properly constructed for XML binding
        VersionType versionType = new VersionType(1, 5);

        assertNotNull(versionType);
        assertEquals(1, versionType.getMajor());
        assertEquals(5, versionType.getMinor());
    }

    /**
     * Tests JSON deserialization builder compliance.
     */
    @Test
    void jsonDeserializationBuilderCompliance() {
        // Test that the builder is properly accessible for JSON deserialization
        VersionType.VersionTypeBuilder builderInstance = new VersionType.VersionTypeBuilder();
        VersionType versionType = builderInstance.withMajor(3).withMinor(2).build();

        assertNotNull(versionType);
        assertEquals(3, versionType.getMajor());
        assertEquals(2, versionType.getMinor());
    }

    /**
     * Tests edge case with maximum integer values.
     */
    @Test
    void edgeCaseWithMaximumIntegerValues() {
        VersionType versionType = new VersionType(Integer.MAX_VALUE, Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, versionType.getMajor());
        assertEquals(Integer.MAX_VALUE, versionType.getMinor());
    }

    /**
     * Tests negative values handling.
     */
    @Test
    void negativeValuesHandling() {
        VersionType versionType = builder
                .withMajor(-1)
                .withMinor(-5)
                .build();

        assertEquals(-1, versionType.getMajor());
        assertEquals(-5, versionType.getMinor());
    }
}

