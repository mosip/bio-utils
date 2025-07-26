package io.mosip.kernel.biometrics.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Jackson deserializer that converts a JSON array of integers into a byte array.
 * Supports signed (-128 to 127) or unsigned (0 to 255) byte conversion based on configuration.
 *
 * @author [Your Name]
 */
public class IntArrayToByteArrayDeserializer extends StdDeserializer<byte[]> {

    private final boolean useUnsigned;

    /**
     * Constructs a deserializer for byte arrays with default signed integer conversion.
     */
    public IntArrayToByteArrayDeserializer() {
        this(byte[].class, false);
    }

    /**
     * Constructs a deserializer for byte arrays with configurable signed/unsigned conversion.
     *
     * @param t           the class type (byte[])
     * @param useUnsigned true to interpret integers as unsigned bytes (0 to 255),
     *                    false for signed bytes (-128 to 127)
     */
    protected IntArrayToByteArrayDeserializer(Class<byte[]> t, boolean useUnsigned) {
        super(t);
        this.useUnsigned = useUnsigned;
    }

    /**
     * Deserializes a JSON array of integers into a byte array.
     *
     * @param parser  the JSON parser
     * @param context the deserialization context
     * @return the deserialized byte array
     * @throws IOException if the JSON is invalid, not an array, or contains out-of-range values
     */
    @Override
    public byte[] deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
            String errorMsg = "Expected JSON array for byte[] deserialization, found: " + parser.getCurrentToken();
            throw new IOException(errorMsg);
        }

        List<Byte> byteList = new ArrayList<>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT) {
                String errorMsg = "Expected integer value in JSON array, found: " + parser.getCurrentToken();
                throw new IOException(errorMsg);
            }

            int value = parser.getIntValue();
            if (useUnsigned) {
                if (value < 0 || value > 255) {
                    String errorMsg = "Unsigned byte value out of range (0 to 255): " + value;
                    throw new IOException(errorMsg);
                }
            } else {
                if (value < -128 || value > 127) {
                    String errorMsg = "Signed byte value out of range (-128 to 127): " + value;
                    throw new IOException(errorMsg);
                }
            }
            byteList.add((byte) value);
        }

        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            result[i] = byteList.get(i);
        }
        return result;
    }
}