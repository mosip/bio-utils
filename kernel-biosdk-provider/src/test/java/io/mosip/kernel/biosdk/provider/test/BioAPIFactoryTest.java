package io.mosip.kernel.biosdk.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.mosip.kernel.biosdk.provider.factory.BioAPIFactory;
import io.mosip.kernel.biosdk.provider.spi.iBioProviderApi;
import io.mosip.kernel.biosdk.provider.util.ErrorCode;
import io.mosip.kernel.core.bioapi.exception.BiometricException;
import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;

public class BioAPIFactoryTest {

    private BioAPIFactory bioAPIFactory;

    @Before
    public void setUp() {
        bioAPIFactory = new BioAPIFactory();
    }

    /**
     * Tests initialization when provider APIs list is null.
     */
    @Test
    public void initializeBioAPIProviders_nullProviders_throwsNoProvidersException() {
        ReflectionTestUtils.setField(bioAPIFactory, "providerApis", null);

        try {
            bioAPIFactory.initializeBioAPIProviders();
            fail("Expected BiometricException");
        } catch (BiometricException ex) {
            assertEquals(ErrorCode.NO_PROVIDERS.getErrorCode(), ex.getErrorCode());
        }
    }

    /**
     * Tests initialization when provider APIs list is empty.
     */
    @Test
    public void initializeBioAPIProviders_emptyProviders_throwsNoProvidersException() {
        ReflectionTestUtils.setField(bioAPIFactory, "providerApis", new ArrayList<>());

        try {
            bioAPIFactory.initializeBioAPIProviders();
            fail("Expected BiometricException");
        } catch (BiometricException ex) {
            assertEquals(ErrorCode.NO_PROVIDERS.getErrorCode(), ex.getErrorCode());
        }
    }

    /**
     * Tests initialization when providers fail and registry remains empty.
     */
    @Test
    public void initializeBioAPIProviders_failingProviders_throwsSDKRegistryEmptyException() {
        List<iBioProviderApi> providers = new ArrayList<>();
        providers.add(new TestFailingProvider());
        ReflectionTestUtils.setField(bioAPIFactory, "providerApis", providers);

        Map<String, Map<String, String>> fingerConfig = new HashMap<>();
        fingerConfig.put("vendor1", new HashMap<>());
        ReflectionTestUtils.setField(bioAPIFactory, "finger", fingerConfig);

        try {
            bioAPIFactory.initializeBioAPIProviders();
            fail("Expected BiometricException");
        } catch (BiometricException ex) {
            assertEquals(ErrorCode.SDK_REGISTRY_EMPTY.getErrorCode(), ex.getErrorCode());
        }
    }

    /**
     * Tests successful initialization with working providers.
     */
    @Test
    public void initializeBioAPIProviders_successfulProviders_completesSuccessfully() throws BiometricException {
        List<iBioProviderApi> providers = new ArrayList<>();
        providers.add(new TestSuccessProvider());
        ReflectionTestUtils.setField(bioAPIFactory, "providerApis", providers);

        Map<String, Map<String, String>> fingerConfig = new HashMap<>();
        fingerConfig.put("vendor1", new HashMap<>());
        ReflectionTestUtils.setField(bioAPIFactory, "finger", fingerConfig);

        bioAPIFactory.initializeBioAPIProviders();
    }

    /**
     * Tests initialization with multiple vendors where registry gets filled early.
     */
    @Test
    public void initializeBioAPIProviders_multipleVendors_breaksEarlyWhenRegistryFilled() throws BiometricException {
        List<iBioProviderApi> providers = new ArrayList<>();
        providers.add(new TestSuccessProvider());
        ReflectionTestUtils.setField(bioAPIFactory, "providerApis", providers);

        Map<String, Map<String, String>> fingerConfig = new HashMap<>();
        fingerConfig.put("vendor1", new HashMap<>());
        fingerConfig.put("vendor2", new HashMap<>());
        ReflectionTestUtils.setField(bioAPIFactory, "finger", fingerConfig);

        bioAPIFactory.initializeBioAPIProviders();
    }

    /**
     * Tests getBioProvider when no provider is registered.
     */
    @Test
    public void getBioProvider_emptyRegistry_throwsNoProvidersException() {
        BioAPIFactory factory = new BioAPIFactory();

        try {
            factory.getBioProvider(BiometricType.FINGER, BiometricFunction.MATCH);
            fail("Expected BiometricException");
        } catch (BiometricException ex) {
            assertEquals(ErrorCode.NO_PROVIDERS.getErrorCode(), ex.getErrorCode());
        }
    }

    /**
     * Tests successful getBioProvider when provider is registered.
     */
    @Test
    public void getBioProvider_registeredProvider_returnsProvider() throws BiometricException {
        BioAPIFactory factory = new BioAPIFactory();
        TestSuccessProvider provider = new TestSuccessProvider();

        Map<BiometricType, Map<BiometricFunction, iBioProviderApi>> registry = new EnumMap<>(BiometricType.class);
        Map<BiometricFunction, iBioProviderApi> functionMap = new EnumMap<>(BiometricFunction.class);
        functionMap.put(BiometricFunction.MATCH, provider);
        registry.put(BiometricType.FINGER, functionMap);
        ReflectionTestUtils.setField(factory, "providerRegistry", registry);

        iBioProviderApi result = factory.getBioProvider(BiometricType.FINGER, BiometricFunction.MATCH);
        assertNotNull(result);
    }

    /**
     * Tests getVendorIds when all modality configurations are null.
     */
    @Test
    public void getVendorIds_nullConfigurations_returnsEmptyList() {
        BioAPIFactory factory = new BioAPIFactory();
        ReflectionTestUtils.setField(factory, "finger", null);
        ReflectionTestUtils.setField(factory, "iris", null);
        ReflectionTestUtils.setField(factory, "face", null);

        List<String> vendorIds = ReflectionTestUtils.invokeMethod(factory, "getVendorIds");
        assertTrue(vendorIds.isEmpty());
    }

    /**
     * Tests getVendorIds with populated modality configurations.
     */
    @Test
    public void getVendorIds_populatedConfigurations_returnsVendorList() {
        BioAPIFactory factory = new BioAPIFactory();

        Map<String, Map<String, String>> fingerConfig = new HashMap<>();
        fingerConfig.put("vendor1", new HashMap<>());
        Map<String, Map<String, String>> irisConfig = new HashMap<>();
        irisConfig.put("vendor2", new HashMap<>());
        Map<String, Map<String, String>> faceConfig = new HashMap<>();
        faceConfig.put("vendor3", new HashMap<>());

        ReflectionTestUtils.setField(factory, "finger", fingerConfig);
        ReflectionTestUtils.setField(factory, "iris", irisConfig);
        ReflectionTestUtils.setField(factory, "face", faceConfig);

        List<String> vendorIds = ReflectionTestUtils.invokeMethod(factory, "getVendorIds");
        assertEquals(3, vendorIds.size());
    }

    /**
     * Tests isModalityConfigured with unsupported modality type.
     */
    @Test
    public void isModalityConfigured_unsupportedModality_returnsFalse() {
        BioAPIFactory factory = new BioAPIFactory();
        boolean result = ReflectionTestUtils.invokeMethod(factory, "isModalityConfigured", BiometricType.VOICE);
        assertEquals(false, result);
    }

    /**
     * Tests entry retrieval methods when configurations are null.
     */
    @Test
    public void getEntries_nullConfigurations_returnsEmptyMaps() {
        BioAPIFactory factory = new BioAPIFactory();
        ReflectionTestUtils.setField(factory, "finger", null);
        ReflectionTestUtils.setField(factory, "iris", null);
        ReflectionTestUtils.setField(factory, "face", null);

        Map<String, String> fingerEntry = ReflectionTestUtils.invokeMethod(factory, "getFingerEntry", "vendor1");
        Map<String, String> irisEntry = ReflectionTestUtils.invokeMethod(factory, "getIrisEntry", "vendor1");
        Map<String, String> faceEntry = ReflectionTestUtils.invokeMethod(factory, "getFaceEntry", "vendor1");

        assertTrue(fingerEntry.isEmpty());
        assertTrue(irisEntry.isEmpty());
        assertTrue(faceEntry.isEmpty());
    }

    /**
     * Tests entry retrieval methods with populated configurations.
     */
    @Test
    public void getEntries_populatedConfigurations_returnsCorrectEntries() {
        BioAPIFactory factory = new BioAPIFactory();

        Map<String, Map<String, String>> fingerConfig = new HashMap<>();
        Map<String, String> vendorConfig = new HashMap<>();
        vendorConfig.put("key", "value");
        fingerConfig.put("vendor1", vendorConfig);

        ReflectionTestUtils.setField(factory, "finger", fingerConfig);

        Map<String, String> fingerEntry = ReflectionTestUtils.invokeMethod(factory, "getFingerEntry", "vendor1");
        assertEquals("value", fingerEntry.get("key"));

        Map<String, String> nonExistentEntry = ReflectionTestUtils.invokeMethod(factory, "getFingerEntry", "vendor2");
        assertTrue(nonExistentEntry.isEmpty());
    }

    private static class TestFailingProvider implements iBioProviderApi {
        @Override
        public Map<BiometricType, List<BiometricFunction>> init(
                Map<BiometricType, Map<String, String>> params) throws BiometricException {
            throw new BiometricException("INIT_FAILED", "Initialization failed");
        }

        @Override
        public boolean verify(List<io.mosip.kernel.biometrics.entities.BIR> sample, List<io.mosip.kernel.biometrics.entities.BIR> bioRecord,
                              BiometricType modality, Map<String, String> flags) { return false; }
        @Override
        public Map<String, Boolean> identify(List<io.mosip.kernel.biometrics.entities.BIR> sample, Map<String, List<io.mosip.kernel.biometrics.entities.BIR>> gallery,
                                             BiometricType modality, Map<String, String> flags) { return null; }
        @Override
        public float[] getSegmentQuality(io.mosip.kernel.biometrics.entities.BIR[] sample, Map<String, String> flags) { return new float[0]; }
        @Override
        public Map<BiometricType, Float> getModalityQuality(io.mosip.kernel.biometrics.entities.BIR[] sample, Map<String, String> flags) { return null; }
        @Override
        public List<io.mosip.kernel.biometrics.entities.BIR> extractTemplate(List<io.mosip.kernel.biometrics.entities.BIR> sample, Map<String, String> flags) { return null; }
    }

    private static class TestSuccessProvider implements iBioProviderApi {
        @Override
        public Map<BiometricType, List<BiometricFunction>> init(
                Map<BiometricType, Map<String, String>> params) throws BiometricException {
            Map<BiometricType, List<BiometricFunction>> result = new EnumMap<>(BiometricType.class);
            result.put(BiometricType.FINGER, Collections.singletonList(BiometricFunction.MATCH));
            return result;
        }

        @Override
        public boolean verify(List<io.mosip.kernel.biometrics.entities.BIR> sample, List<io.mosip.kernel.biometrics.entities.BIR> bioRecord,
                              BiometricType modality, Map<String, String> flags) { return false; }
        @Override
        public Map<String, Boolean> identify(List<io.mosip.kernel.biometrics.entities.BIR> sample, Map<String, List<io.mosip.kernel.biometrics.entities.BIR>> gallery,
                                             BiometricType modality, Map<String, String> flags) { return null; }
        @Override
        public float[] getSegmentQuality(io.mosip.kernel.biometrics.entities.BIR[] sample, Map<String, String> flags) { return new float[0]; }
        @Override
        public Map<BiometricType, Float> getModalityQuality(io.mosip.kernel.biometrics.entities.BIR[] sample, Map<String, String> flags) { return null; }
        @Override
        public List<io.mosip.kernel.biometrics.entities.BIR> extractTemplate(List<io.mosip.kernel.biometrics.entities.BIR> sample, Map<String, String> flags) { return null; }
    }
}
