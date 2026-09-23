package io.mosip.kernel.biometrics.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import io.mosip.kernel.biometrics.entities.BDBInfo;
import io.mosip.kernel.biometrics.entities.RegistryIDType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.constant.ProcessedLevelType;
import io.mosip.kernel.biometrics.constant.PurposeType;
import io.mosip.kernel.biometrics.constant.QualityType;

/**
 * Unit tests for {@link BDBInfo}.
 */
class BDBInfoTest {

    @Mock
    private RegistryIDType mockFormat;

    @Mock
    private RegistryIDType mockProduct;

    @Mock
    private RegistryIDType mockCaptureDevice;

    @Mock
    private RegistryIDType mockFeatureExtractionAlgorithm;

    @Mock
    private RegistryIDType mockComparisonAlgorithm;

    @Mock
    private RegistryIDType mockCompressionAlgorithm;

    @Mock
    private RegistryIDType mockQualityAlgorithm;

    private BDBInfo.BDBInfoBuilder builder;
    private LocalDateTime testDateTime;
    private byte[] testChallengeResponse;
    private List<BiometricType> testBiometricTypes;
    private List<String> testSubtypes;
    private QualityType testQuality;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        builder = new BDBInfo.BDBInfoBuilder();
        testDateTime = LocalDateTime.now();
        testChallengeResponse = new byte[]{1, 2, 3, 4, 5};
        testBiometricTypes = Arrays.asList(BiometricType.FINGER, BiometricType.FACE);
        testSubtypes = Arrays.asList("LEFT_THUMB", "RIGHT_INDEX");

        // Create a test QualityType instance
        testQuality = new QualityType();
        testQuality.setAlgorithm(mockQualityAlgorithm);
        testQuality.setScore(85L);
        testQuality.setQualityCalculationFailed(null);
    }

    /**
     * Tests default constructor creates BDBInfo with null fields.
     */
    @Test
    void defaultConstructorCreatesEmptyBdbInfo() {
        BDBInfo bdbInfo = new BDBInfo();

        assertNotNull(bdbInfo);
        assertNull(bdbInfo.getChallengeResponse());
        assertNull(bdbInfo.getIndex());
        assertNull(bdbInfo.getFormat());
        assertNull(bdbInfo.getEncryption());
        assertNull(bdbInfo.getCreationDate());
        assertNull(bdbInfo.getNotValidBefore());
        assertNull(bdbInfo.getNotValidAfter());
        assertNull(bdbInfo.getType());
        assertNull(bdbInfo.getSubtype());
        assertNull(bdbInfo.getLevel());
        assertNull(bdbInfo.getProduct());
        assertNull(bdbInfo.getCaptureDevice());
        assertNull(bdbInfo.getFeatureExtractionAlgorithm());
        assertNull(bdbInfo.getComparisonAlgorithm());
        assertNull(bdbInfo.getCompressionAlgorithm());
        assertNull(bdbInfo.getPurpose());
        assertNull(bdbInfo.getQuality());
    }

    /**
     * Tests builder constructor sets all fields correctly.
     */
    @Test
    void builderConstructorSetsAllFieldsCorrectly() {
        BDBInfo bdbInfo = builder
                .withChallengeResponse(testChallengeResponse)
                .withIndex("test-index")
                .withFormat(mockFormat)
                .withEncryption(true)
                .withCreationDate(testDateTime)
                .withNotValidBefore(testDateTime.minusDays(1))
                .withNotValidAfter(testDateTime.plusDays(1))
                .withType(testBiometricTypes)
                .withSubtype(testSubtypes)
                .withLevel(ProcessedLevelType.RAW)
                .withProduct(mockProduct)
                .withCaptureDevice(mockCaptureDevice)
                .withFeatureExtractionAlgorithm(mockFeatureExtractionAlgorithm)
                .withComparisonAlgorithm(mockComparisonAlgorithm)
                .withCompressionAlgorithm(mockCompressionAlgorithm)
                .withPurpose(PurposeType.ENROLL)
                .withQuality(testQuality)
                .build();

        assertArrayEquals(testChallengeResponse, bdbInfo.getChallengeResponse());
        assertEquals("test-index", bdbInfo.getIndex());
        assertEquals(mockFormat, bdbInfo.getFormat());
        assertTrue(bdbInfo.getEncryption());
        assertEquals(testDateTime, bdbInfo.getCreationDate());
        assertEquals(testDateTime.minusDays(1), bdbInfo.getNotValidBefore());
        assertEquals(testDateTime.plusDays(1), bdbInfo.getNotValidAfter());
        assertEquals(testBiometricTypes, bdbInfo.getType());
        assertEquals(testSubtypes, bdbInfo.getSubtype());
        assertEquals(ProcessedLevelType.RAW, bdbInfo.getLevel());
        assertEquals(mockProduct, bdbInfo.getProduct());
        assertEquals(mockCaptureDevice, bdbInfo.getCaptureDevice());
        assertEquals(mockFeatureExtractionAlgorithm, bdbInfo.getFeatureExtractionAlgorithm());
        assertEquals(mockComparisonAlgorithm, bdbInfo.getComparisonAlgorithm());
        assertEquals(mockCompressionAlgorithm, bdbInfo.getCompressionAlgorithm());
        assertEquals(PurposeType.ENROLL, bdbInfo.getPurpose());
        assertEquals(testQuality, bdbInfo.getQuality());
    }

    /**
     * Tests builder with challenge response sets field correctly.
     */
    @Test
    void builderWithChallengeResponseSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withChallengeResponse(testChallengeResponse).build();
        assertArrayEquals(testChallengeResponse, bdbInfo.getChallengeResponse());
    }

    /**
     * Tests builder with index sets field correctly.
     */
    @Test
    void builderWithIndexSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withIndex("test-index").build();
        assertEquals("test-index", bdbInfo.getIndex());
    }

    /**
     * Tests builder with format sets field correctly.
     */
    @Test
    void builderWithFormatSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withFormat(mockFormat).build();
        assertEquals(mockFormat, bdbInfo.getFormat());
    }

    /**
     * Tests builder with encryption true sets field correctly.
     */
    @Test
    void builderWithEncryptionTrueSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withEncryption(true).build();
        assertTrue(bdbInfo.getEncryption());
    }

    /**
     * Tests builder with encryption false sets field correctly.
     */
    @Test
    void builderWithEncryptionFalseSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withEncryption(false).build();
        assertFalse(bdbInfo.getEncryption());
    }

    /**
     * Tests builder with creation date sets field correctly.
     */
    @Test
    void builderWithCreationDateSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withCreationDate(testDateTime).build();
        assertEquals(testDateTime, bdbInfo.getCreationDate());
    }

    /**
     * Tests builder with not valid before sets field correctly.
     */
    @Test
    void builderWithNotValidBeforeSetsFieldCorrectly() {
        LocalDateTime notValidBefore = testDateTime.minusDays(1);
        BDBInfo bdbInfo = builder.withNotValidBefore(notValidBefore).build();
        assertEquals(notValidBefore, bdbInfo.getNotValidBefore());
    }

    /**
     * Tests builder with not valid after sets field correctly.
     */
    @Test
    void builderWithNotValidAfterSetsFieldCorrectly() {
        LocalDateTime notValidAfter = testDateTime.plusDays(1);
        BDBInfo bdbInfo = builder.withNotValidAfter(notValidAfter).build();
        assertEquals(notValidAfter, bdbInfo.getNotValidAfter());
    }

    /**
     * Tests builder with biometric type sets field correctly.
     */
    @Test
    void builderWithBiometricTypeSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withType(testBiometricTypes).build();
        assertEquals(testBiometricTypes, bdbInfo.getType());
    }

    /**
     * Tests builder with subtype sets field correctly.
     */
    @Test
    void builderWithSubtypeSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withSubtype(testSubtypes).build();
        assertEquals(testSubtypes, bdbInfo.getSubtype());
    }

    /**
     * Tests builder with processed level sets field correctly.
     */
    @Test
    void builderWithProcessedLevelSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withLevel(ProcessedLevelType.INTERMEDIATE).build();
        assertEquals(ProcessedLevelType.INTERMEDIATE, bdbInfo.getLevel());
    }

    /**
     * Tests builder with product sets field correctly.
     */
    @Test
    void builderWithProductSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withProduct(mockProduct).build();
        assertEquals(mockProduct, bdbInfo.getProduct());
    }

    /**
     * Tests builder with purpose sets field correctly.
     */
    @Test
    void builderWithPurposeSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withPurpose(PurposeType.VERIFY).build();
        assertEquals(PurposeType.VERIFY, bdbInfo.getPurpose());
    }

    /**
     * Tests builder with quality sets field correctly.
     */
    @Test
    void builderWithQualitySetsFieldCorrectly() {
        QualityType excellentQuality = new QualityType();
        excellentQuality.setScore(95L);
        BDBInfo bdbInfo = builder.withQuality(excellentQuality).build();
        assertEquals(excellentQuality, bdbInfo.getQuality());
    }

    /**
     * Tests builder with capture device sets field correctly.
     */
    @Test
    void builderWithCaptureDeviceSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withCaptureDevice(mockCaptureDevice).build();
        assertEquals(mockCaptureDevice, bdbInfo.getCaptureDevice());
    }

    /**
     * Tests builder with feature extraction algorithm sets field correctly.
     */
    @Test
    void builderWithFeatureExtractionAlgorithmSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withFeatureExtractionAlgorithm(mockFeatureExtractionAlgorithm).build();
        assertEquals(mockFeatureExtractionAlgorithm, bdbInfo.getFeatureExtractionAlgorithm());
    }

    /**
     * Tests builder with comparison algorithm sets field correctly.
     */
    @Test
    void builderWithComparisonAlgorithmSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withComparisonAlgorithm(mockComparisonAlgorithm).build();
        assertEquals(mockComparisonAlgorithm, bdbInfo.getComparisonAlgorithm());
    }

    /**
     * Tests builder with compression algorithm sets field correctly.
     */
    @Test
    void builderWithCompressionAlgorithmSetsFieldCorrectly() {
        BDBInfo bdbInfo = builder.withCompressionAlgorithm(mockCompressionAlgorithm).build();
        assertEquals(mockCompressionAlgorithm, bdbInfo.getCompressionAlgorithm());
    }

    /**
     * Tests builder chaining returns same builder instance.
     */
    @Test
    void builderChainingReturnsSameBuilderInstance() {
        BDBInfo.BDBInfoBuilder result = builder
                .withIndex("test")
                .withEncryption(true)
                .withCreationDate(testDateTime);

        assertEquals(builder, result);
    }

    /**
     * Tests builder build creates new BDBInfo instances.
     */
    @Test
    void builderBuildCreatesNewBdbInfoInstances() {
        BDBInfo bdbInfo1 = builder.withIndex("test").build();
        BDBInfo bdbInfo2 = builder.withIndex("test").build();

        assertNotSame(bdbInfo1, bdbInfo2);
        assertEquals(bdbInfo1.getIndex(), bdbInfo2.getIndex());
    }

    /**
     * Tests setter and getter for challenge response.
     */
    @Test
    void challengeResponseSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setChallengeResponse(testChallengeResponse);
        assertArrayEquals(testChallengeResponse, bdbInfo.getChallengeResponse());
    }

    /**
     * Tests setter and getter for index.
     */
    @Test
    void indexSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setIndex("test-index");
        assertEquals("test-index", bdbInfo.getIndex());
    }

    /**
     * Tests setter and getter for format.
     */
    @Test
    void formatSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setFormat(mockFormat);
        assertEquals(mockFormat, bdbInfo.getFormat());
    }

    /**
     * Tests setter and getter for encryption.
     */
    @Test
    void encryptionSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setEncryption(true);
        assertTrue(bdbInfo.getEncryption());
    }

    /**
     * Tests setter and getter for creation date.
     */
    @Test
    void creationDateSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setCreationDate(testDateTime);
        assertEquals(testDateTime, bdbInfo.getCreationDate());
    }

    /**
     * Tests setter and getter for not valid before.
     */
    @Test
    void notValidBeforeSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        LocalDateTime notValidBefore = testDateTime.minusDays(1);
        bdbInfo.setNotValidBefore(notValidBefore);
        assertEquals(notValidBefore, bdbInfo.getNotValidBefore());
    }

    /**
     * Tests setter and getter for not valid after.
     */
    @Test
    void notValidAfterSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        LocalDateTime notValidAfter = testDateTime.plusDays(1);
        bdbInfo.setNotValidAfter(notValidAfter);
        assertEquals(notValidAfter, bdbInfo.getNotValidAfter());
    }

    /**
     * Tests setter and getter for biometric type.
     */
    @Test
    void biometricTypeSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setType(testBiometricTypes);
        assertEquals(testBiometricTypes, bdbInfo.getType());
    }

    /**
     * Tests setter and getter for subtype.
     */
    @Test
    void subtypeSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setSubtype(testSubtypes);
        assertEquals(testSubtypes, bdbInfo.getSubtype());
    }

    /**
     * Tests setter and getter for processed level.
     */
    @Test
    void processedLevelSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setLevel(ProcessedLevelType.PROCESSED);
        assertEquals(ProcessedLevelType.PROCESSED, bdbInfo.getLevel());
    }

    /**
     * Tests setter and getter for product.
     */
    @Test
    void productSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setProduct(mockProduct);
        assertEquals(mockProduct, bdbInfo.getProduct());
    }

    /**
     * Tests setter and getter for capture device.
     */
    @Test
    void captureDeviceSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setCaptureDevice(mockCaptureDevice);
        assertEquals(mockCaptureDevice, bdbInfo.getCaptureDevice());
    }

    /**
     * Tests setter and getter for feature extraction algorithm.
     */
    @Test
    void featureExtractionAlgorithmSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setFeatureExtractionAlgorithm(mockFeatureExtractionAlgorithm);
        assertEquals(mockFeatureExtractionAlgorithm, bdbInfo.getFeatureExtractionAlgorithm());
    }

    /**
     * Tests setter and getter for comparison algorithm.
     */
    @Test
    void comparisonAlgorithmSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setComparisonAlgorithm(mockComparisonAlgorithm);
        assertEquals(mockComparisonAlgorithm, bdbInfo.getComparisonAlgorithm());
    }

    /**
     * Tests setter and getter for compression algorithm.
     */
    @Test
    void compressionAlgorithmSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setCompressionAlgorithm(mockCompressionAlgorithm);
        assertEquals(mockCompressionAlgorithm, bdbInfo.getCompressionAlgorithm());
    }

    /**
     * Tests setter and getter for purpose.
     */
    @Test
    void purposeSetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        bdbInfo.setPurpose(PurposeType.IDENTIFY);
        assertEquals(PurposeType.IDENTIFY, bdbInfo.getPurpose());
    }

    /**
     * Tests setter and getter for quality.
     */
    @Test
    void qualitySetterGetterWorksCorrectly() {
        BDBInfo bdbInfo = new BDBInfo();
        QualityType poorQuality = new QualityType();
        poorQuality.setScore(25L);
        bdbInfo.setQuality(poorQuality);
        assertEquals(poorQuality, bdbInfo.getQuality());
    }

    /**
     * Tests quality type object creation and field access.
     */
    @Test
    void qualityTypeObjectCreationAndFieldAccess() {
        QualityType quality = new QualityType();
        quality.setAlgorithm(mockQualityAlgorithm);
        quality.setScore(75L);
        quality.setQualityCalculationFailed("No failure");

        assertEquals(mockQualityAlgorithm, quality.getAlgorithm());
        assertEquals(75L, quality.getScore());
        assertEquals("No failure", quality.getQualityCalculationFailed());
    }

    /**
     * Tests setting null values for all fields.
     */
    @Test
    void settingNullValuesWorksCorrectly() {
        BDBInfo bdbInfo = builder
                .withChallengeResponse(testChallengeResponse)
                .withIndex("test")
                .withEncryption(true)
                .build();

        bdbInfo.setChallengeResponse(null);
        bdbInfo.setIndex(null);
        bdbInfo.setFormat(null);
        bdbInfo.setEncryption(null);
        bdbInfo.setCreationDate(null);
        bdbInfo.setNotValidBefore(null);
        bdbInfo.setNotValidAfter(null);
        bdbInfo.setType(null);
        bdbInfo.setSubtype(null);
        bdbInfo.setLevel(null);
        bdbInfo.setProduct(null);
        bdbInfo.setCaptureDevice(null);
        bdbInfo.setFeatureExtractionAlgorithm(null);
        bdbInfo.setComparisonAlgorithm(null);
        bdbInfo.setCompressionAlgorithm(null);
        bdbInfo.setPurpose(null);
        bdbInfo.setQuality(null);

        assertNull(bdbInfo.getChallengeResponse());
        assertNull(bdbInfo.getIndex());
        assertNull(bdbInfo.getFormat());
        assertNull(bdbInfo.getEncryption());
        assertNull(bdbInfo.getCreationDate());
        assertNull(bdbInfo.getNotValidBefore());
        assertNull(bdbInfo.getNotValidAfter());
        assertNull(bdbInfo.getType());
        assertNull(bdbInfo.getSubtype());
        assertNull(bdbInfo.getLevel());
        assertNull(bdbInfo.getProduct());
        assertNull(bdbInfo.getCaptureDevice());
        assertNull(bdbInfo.getFeatureExtractionAlgorithm());
        assertNull(bdbInfo.getComparisonAlgorithm());
        assertNull(bdbInfo.getCompressionAlgorithm());
        assertNull(bdbInfo.getPurpose());
        assertNull(bdbInfo.getQuality());
    }

    /**
     * Tests equals method with same object.
     */
    @Test
    void equalsWithSameObjectReturnsTrue() {
        BDBInfo bdbInfo = builder.withIndex("test").build();
        assertTrue(bdbInfo.equals(bdbInfo));
    }

    /**
     * Tests equals method with null returns false.
     */
    @Test
    void equalsWithNullReturnsFalse() {
        BDBInfo bdbInfo = builder.withIndex("test").build();
        assertFalse(bdbInfo.equals(null));
    }

    /**
     * Tests equals method with different class returns false.
     */
    @Test
    void equalsWithDifferentClassReturnsFalse() {
        BDBInfo bdbInfo = builder.withIndex("test").build();
        assertFalse(bdbInfo.equals("string"));
    }

    /**
     * Tests hashCode method consistency.
     */
    @Test
    void hashCodeIsConsistent() {
        BDBInfo bdbInfo = builder.withIndex("test").build();
        int hash1 = bdbInfo.hashCode();
        int hash2 = bdbInfo.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Tests toString method returns non-null string.
     */
    @Test
    void toStringReturnsNonNullString() {
        BDBInfo bdbInfo = builder.withIndex("test").build();
        String result = bdbInfo.toString();
        assertNotNull(result);
        assertTrue(result.contains("BDBInfo"));
    }

    /**
     * Tests that BDBInfo implements Serializable.
     */
    @Test
    void implementsSerializable() {
        BDBInfo bdbInfo = new BDBInfo();
        assertTrue(bdbInfo instanceof java.io.Serializable);
    }

    /**
     * Tests builder with empty lists.
     */
    @Test
    void builderWithEmptyListsWorksCorrectly() {
        List<BiometricType> emptyTypes = new ArrayList<>();
        List<String> emptySubtypes = new ArrayList<>();

        BDBInfo bdbInfo = builder
                .withType(emptyTypes)
                .withSubtype(emptySubtypes)
                .build();

        assertEquals(emptyTypes, bdbInfo.getType());
        assertEquals(emptySubtypes, bdbInfo.getSubtype());
        assertTrue(bdbInfo.getType().isEmpty());
        assertTrue(bdbInfo.getSubtype().isEmpty());
    }
}