package io.mosip.kernel.biometrics.test.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import io.mosip.kernel.biometrics.entities.BIRInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * Unit tests for {@link BIRInfo}.
 */
class BIRInfoTest {

    private BIRInfo.BIRInfoBuilder builder;
    private LocalDateTime testDateTime;
    private LocalDateTime testNotValidBefore;
    private LocalDateTime testNotValidAfter;
    private byte[] testPayload;

    @BeforeEach
    void setUp() {
        builder = new BIRInfo.BIRInfoBuilder();
        testDateTime = LocalDateTime.now();
        testNotValidBefore = testDateTime.minusDays(1);
        testNotValidAfter = testDateTime.plusDays(30);
        testPayload = new byte[]{1, 2, 3, 4, 5};
    }

    /**
     * Tests default constructor creates BIRInfo with null fields.
     */
    @Test
    void defaultConstructorCreatesEmptyBirInfo() {
        BIRInfo birInfo = new BIRInfo();

        assertNotNull(birInfo);
        assertNull(birInfo.getCreator());
        assertNull(birInfo.getIndex());
        assertNull(birInfo.getPayload());
        assertNull(birInfo.getIntegrity());
        assertNull(birInfo.getCreationDate());
        assertNull(birInfo.getNotValidBefore());
        assertNull(birInfo.getNotValidAfter());
    }

    /**
     * Tests builder constructor sets all fields correctly.
     */
    @Test
    void builderConstructorSetsAllFieldsCorrectly() {
        BIRInfo birInfo = builder
                .withCreator("TestCreator")
                .withIndex("test-index")
                .withPayload(testPayload)
                .withIntegrity(true)
                .withCreationDate(testDateTime)
                .withNotValidBefore(testNotValidBefore)
                .withNotValidAfter(testNotValidAfter)
                .build();

        assertEquals("TestCreator", birInfo.getCreator());
        assertEquals("test-index", birInfo.getIndex());
        assertArrayEquals(testPayload, birInfo.getPayload());
        assertTrue(birInfo.getIntegrity());
        assertEquals(testDateTime, birInfo.getCreationDate());
        assertEquals(testNotValidBefore, birInfo.getNotValidBefore());
        assertEquals(testNotValidAfter, birInfo.getNotValidAfter());
    }

    /**
     * Tests builder with creator sets field correctly.
     */
    @Test
    void builderWithCreatorSetsFieldCorrectly() {
        BIRInfo birInfo = builder.withCreator("TestCreator").build();
        assertEquals("TestCreator", birInfo.getCreator());
    }

    /**
     * Tests builder with index sets field correctly.
     */
    @Test
    void builderWithIndexSetsFieldCorrectly() {
        BIRInfo birInfo = builder.withIndex("test-index").build();
        assertEquals("test-index", birInfo.getIndex());
    }

    /**
     * Tests builder with payload sets field correctly.
     */
    @Test
    void builderWithPayloadSetsFieldCorrectly() {
        BIRInfo birInfo = builder.withPayload(testPayload).build();
        assertArrayEquals(testPayload, birInfo.getPayload());
    }

    /**
     * Tests builder with integrity true sets field correctly.
     */
    @Test
    void builderWithIntegrityTrueSetsFieldCorrectly() {
        BIRInfo birInfo = builder.withIntegrity(true).build();
        assertTrue(birInfo.getIntegrity());
    }

    /**
     * Tests builder with integrity false sets field correctly.
     */
    @Test
    void builderWithIntegrityFalseSetsFieldCorrectly() {
        BIRInfo birInfo = builder.withIntegrity(false).build();
        assertFalse(birInfo.getIntegrity());
    }

    /**
     * Tests builder with creation date sets field correctly.
     */
    @Test
    void builderWithCreationDateSetsFieldCorrectly() {
        BIRInfo birInfo = builder.withCreationDate(testDateTime).build();
        assertEquals(testDateTime, birInfo.getCreationDate());
    }

    /**
     * Tests builder with not valid before sets field correctly.
     */
    @Test
    void builderWithNotValidBeforeSetsFieldCorrectly() {
        BIRInfo birInfo = builder.withNotValidBefore(testNotValidBefore).build();
        assertEquals(testNotValidBefore, birInfo.getNotValidBefore());
    }

    /**
     * Tests builder with not valid after sets field correctly.
     */
    @Test
    void builderWithNotValidAfterSetsFieldCorrectly() {
        BIRInfo birInfo = builder.withNotValidAfter(testNotValidAfter).build();
        assertEquals(testNotValidAfter, birInfo.getNotValidAfter());
    }

    /**
     * Tests builder chaining returns same builder instance.
     */
    @Test
    void builderChainingReturnsSameBuilderInstance() {
        BIRInfo.BIRInfoBuilder result = builder
                .withCreator("TestCreator")
                .withIndex("test-index")
                .withIntegrity(true);

        assertEquals(builder, result);
    }

    /**
     * Tests builder build creates new BIRInfo instances.
     */
    @Test
    void builderBuildCreatesNewBirInfoInstances() {
        BIRInfo birInfo1 = builder.withCreator("TestCreator").build();
        BIRInfo birInfo2 = builder.withCreator("TestCreator").build();

        assertNotSame(birInfo1, birInfo2);
        assertEquals(birInfo1.getCreator(), birInfo2.getCreator());
    }

    /**
     * Tests setter and getter for creator.
     */
    @Test
    void creatorSetterGetterWorksCorrectly() {
        BIRInfo birInfo = new BIRInfo();
        birInfo.setCreator("TestCreator");
        assertEquals("TestCreator", birInfo.getCreator());
    }

    /**
     * Tests setter and getter for index.
     */
    @Test
    void indexSetterGetterWorksCorrectly() {
        BIRInfo birInfo = new BIRInfo();
        birInfo.setIndex("test-index");
        assertEquals("test-index", birInfo.getIndex());
    }

    /**
     * Tests setter and getter for payload.
     */
    @Test
    void payloadSetterGetterWorksCorrectly() {
        BIRInfo birInfo = new BIRInfo();
        birInfo.setPayload(testPayload);
        assertArrayEquals(testPayload, birInfo.getPayload());
    }

    /**
     * Tests setter and getter for integrity.
     */
    @Test
    void integritySetterGetterWorksCorrectly() {
        BIRInfo birInfo = new BIRInfo();
        birInfo.setIntegrity(true);
        assertTrue(birInfo.getIntegrity());

        birInfo.setIntegrity(false);
        assertFalse(birInfo.getIntegrity());
    }

    /**
     * Tests setter and getter for creation date.
     */
    @Test
    void creationDateSetterGetterWorksCorrectly() {
        BIRInfo birInfo = new BIRInfo();
        birInfo.setCreationDate(testDateTime);
        assertEquals(testDateTime, birInfo.getCreationDate());
    }

    /**
     * Tests setter and getter for not valid before.
     */
    @Test
    void notValidBeforeSetterGetterWorksCorrectly() {
        BIRInfo birInfo = new BIRInfo();
        birInfo.setNotValidBefore(testNotValidBefore);
        assertEquals(testNotValidBefore, birInfo.getNotValidBefore());
    }

    /**
     * Tests setter and getter for not valid after.
     */
    @Test
    void notValidAfterSetterGetterWorksCorrectly() {
        BIRInfo birInfo = new BIRInfo();
        birInfo.setNotValidAfter(testNotValidAfter);
        assertEquals(testNotValidAfter, birInfo.getNotValidAfter());
    }

    /**
     * Tests setting null values for all fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        BIRInfo birInfo = builder
                .withCreator("TestCreator")
                .withIndex("test-index")
                .withPayload(testPayload)
                .withIntegrity(true)
                .withCreationDate(testDateTime)
                .withNotValidBefore(testNotValidBefore)
                .withNotValidAfter(testNotValidAfter)
                .build();

        birInfo.setCreator(null);
        birInfo.setIndex(null);
        birInfo.setPayload(null);
        birInfo.setIntegrity(null);
        birInfo.setCreationDate(null);
        birInfo.setNotValidBefore(null);
        birInfo.setNotValidAfter(null);

        assertNull(birInfo.getCreator());
        assertNull(birInfo.getIndex());
        assertNull(birInfo.getPayload());
        assertNull(birInfo.getIntegrity());
        assertNull(birInfo.getCreationDate());
        assertNull(birInfo.getNotValidBefore());
        assertNull(birInfo.getNotValidAfter());
    }

    /**
     * Tests builder with empty string values.
     */
    @Test
    void builderWithEmptyStringValuesWorksCorrectly() {
        BIRInfo birInfo = builder
                .withCreator("")
                .withIndex("")
                .build();

        assertEquals("", birInfo.getCreator());
        assertEquals("", birInfo.getIndex());
    }

    /**
     * Tests builder with empty payload array.
     */
    @Test
    void builderWithEmptyPayloadArrayWorksCorrectly() {
        byte[] emptyPayload = new byte[0];
        BIRInfo birInfo = builder.withPayload(emptyPayload).build();

        assertArrayEquals(emptyPayload, birInfo.getPayload());
        assertEquals(0, birInfo.getPayload().length);
    }

    /**
     * Tests builder with null payload.
     */
    @Test
    void builderWithNullPayloadWorksCorrectly() {
        BIRInfo birInfo = builder.withPayload(null).build();
        assertNull(birInfo.getPayload());
    }

    /**
     * Tests builder with same date for all date fields.
     */
    @Test
    void builderWithSameDateForAllDateFieldsWorksCorrectly() {
        LocalDateTime sameDate = LocalDateTime.now();
        BIRInfo birInfo = builder
                .withCreationDate(sameDate)
                .withNotValidBefore(sameDate)
                .withNotValidAfter(sameDate)
                .build();

        assertEquals(sameDate, birInfo.getCreationDate());
        assertEquals(sameDate, birInfo.getNotValidBefore());
        assertEquals(sameDate, birInfo.getNotValidAfter());
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        BIRInfo birInfo = builder.withCreator("TestCreator").build();
        assertTrue(birInfo.equals(birInfo));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        BIRInfo birInfo = builder.withCreator("TestCreator").build();
        assertFalse(birInfo.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        BIRInfo birInfo = builder.withCreator("TestCreator").build();
        assertFalse(birInfo.equals("string"));
    }

    /**
     * Tests equals method with identical content returns true.
     */
    @Test
    void equalsWithIdenticalContentReturnsTrue() {
        BIRInfo birInfo1 = builder
                .withCreator("TestCreator")
                .withIndex("test-index")
                .build();
        BIRInfo birInfo2 = new BIRInfo.BIRInfoBuilder()
                .withCreator("TestCreator")
                .withIndex("test-index")
                .build();

        assertTrue(birInfo1.equals(birInfo2));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        BIRInfo birInfo = builder.withCreator("TestCreator").build();
        int hash1 = birInfo.hashCode();
        int hash2 = birInfo.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests hashCode method with equal objects.
     */
    @Test
    void hashCodeWithEqualObjectsReturnsSameValue() {
        BIRInfo birInfo1 = builder.withCreator("TestCreator").build();
        BIRInfo birInfo2 = new BIRInfo.BIRInfoBuilder().withCreator("TestCreator").build();

        assertEquals(birInfo1.hashCode(), birInfo2.hashCode());
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        BIRInfo birInfo = builder.withCreator("TestCreator").build();
        String result = birInfo.toString();
        assertNotNull(result);
        assertTrue(result.contains("BIRInfo"));
    }

    /**
     * Tests toString method includes field information.
     */
    @Test
    void toStringIncludesFieldInformation() {
        BIRInfo birInfo = builder
                .withCreator("TestCreator")
                .withIndex("test-index")
                .withIntegrity(true)
                .build();

        String result = birInfo.toString();
        assertNotNull(result);
        assertTrue(result.contains("creator"));
        assertTrue(result.contains("index"));
        assertTrue(result.contains("integrity"));
    }

    /**
     * Tests that BIRInfo implements Serializable.
     */
    @Test
    void implementsSerializable() {
        BIRInfo birInfo = new BIRInfo();
        assertTrue(birInfo instanceof java.io.Serializable);
    }

    /**
     * Tests serialization compatibility by checking serialVersionUID.
     */
    @Test
    void serialVersionUidIsCorrect() throws Exception {
        java.lang.reflect.Field field = BIRInfo.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        long actualSerialVersionUID = (Long) field.get(null);
        assertEquals(-2466414332099574792L, actualSerialVersionUID);
    }

    /**
     * Tests builder partial configuration.
     */
    @Test
    void builderPartialConfigurationWorksCorrectly() {
        BIRInfo birInfo = builder
                .withCreator("TestCreator")
                .withIntegrity(true)
                .build();

        assertEquals("TestCreator", birInfo.getCreator());
        assertTrue(birInfo.getIntegrity());
        assertNull(birInfo.getIndex());
        assertNull(birInfo.getPayload());
        assertNull(birInfo.getCreationDate());
        assertNull(birInfo.getNotValidBefore());
        assertNull(birInfo.getNotValidAfter());
    }

    /**
     * Tests payload array is properly handled.
     */
    @Test
    void payloadArrayIsProperlyHandled() {
        byte[] largePayload = new byte[1000];
        for (int i = 0; i < largePayload.length; i++) {
            largePayload[i] = (byte) (i % 256);
        }

        BIRInfo birInfo = builder.withPayload(largePayload).build();

        assertArrayEquals(largePayload, birInfo.getPayload());
        assertEquals(1000, birInfo.getPayload().length);
    }

    /**
     * Tests date validation with valid date ranges.
     */
    @Test
    void dateValidationWithValidDateRangesWorksCorrectly() {
        LocalDateTime creation = LocalDateTime.now();
        LocalDateTime notBefore = creation.minusDays(1);
        LocalDateTime notAfter = creation.plusDays(30);

        BIRInfo birInfo = builder
                .withCreationDate(creation)
                .withNotValidBefore(notBefore)
                .withNotValidAfter(notAfter)
                .build();

        assertTrue(birInfo.getNotValidBefore().isBefore(birInfo.getCreationDate()));
        assertTrue(birInfo.getCreationDate().isBefore(birInfo.getNotValidAfter()));
        assertTrue(birInfo.getNotValidBefore().isBefore(birInfo.getNotValidAfter()));
    }

    /**
     * Tests that different builder instances create independent objects.
     */
    @Test
    void differentBuildersCreateIndependentObjects() {
        BIRInfo birInfo1 = new BIRInfo.BIRInfoBuilder()
                .withCreator("BaseCreator")
                .withIndex("index1")
                .build();

        BIRInfo birInfo2 = new BIRInfo.BIRInfoBuilder()
                .withCreator("BaseCreator")
                .withIndex("index2")
                .build();

        assertEquals("BaseCreator", birInfo1.getCreator());
        assertEquals("BaseCreator", birInfo2.getCreator());
        assertEquals("index1", birInfo1.getIndex());
        assertEquals("index2", birInfo2.getIndex());
        assertNotSame(birInfo1, birInfo2);
    }
}