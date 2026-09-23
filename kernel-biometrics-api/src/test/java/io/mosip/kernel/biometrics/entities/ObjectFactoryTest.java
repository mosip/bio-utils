package io.mosip.kernel.biometrics.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Coverage for JAXB {@link ObjectFactory}.
 */
class ObjectFactoryTest {

    @Test
    void createBirReturnsInstance() {
        ObjectFactory factory = new ObjectFactory();
        assertNotNull(factory.createBIR());
    }
}
