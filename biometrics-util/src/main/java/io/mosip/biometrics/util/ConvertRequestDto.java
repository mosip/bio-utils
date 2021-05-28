package io.mosip.biometrics.util;

import lombok.Data;

@Data
public class ConvertRequestDto {

    private String version; // currently supported ISO19794_4_2011
    private String purpose; // Registration or Auth
    private int imageType; //JP2000 --> 0 or WSQ --> 1
    private byte[] inputBytes;

    private String modality; //Face or Iris or Finger
    private String biometricSubType;

}
