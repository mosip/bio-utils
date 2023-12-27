# Biometrics-Util Samples

The test contains samples to show usage of Biometrics-Util library.

# BioAuthDecoderValueCreaterApplication
The application shows how to decode the auth biometric value and get the following values Salt, AAD, Encoded Data

```run_bio_auth_decoder.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.BioAuthDecoderValueCreaterApplication
"mosip.mock.sbi.biometric.transaction.id=SBI1069-208"
"mosip.mock.sbi.biometric.time.stamp=2023-01-03T05:56:45Z"
"mosip.mock.sbi.biometric.thumb.print=2F6FB5590B21E9526F8E4A23B5CC961021614C236D517794F0A7F29E5BA32C2C"
"mosip.mock.sbi.biometric.session.key=eCgWEQpCRs45gVzVW5gsGiGrQtMN567b5qX_NLuEEan2-fDSyK9ppRavASVp-FjdNkAa7fzdoXR7EPh9J3o5IMfQV4S_EGdbi2bON4g3-kOgwbSZ5KR4KjmrZISou6zb7vAUrulkqq0z61qTod8OooIMWNVuMlqEj-WQ1cQS5B2l7B5eJ-i-8eIZLBcBzKx82yjNC4O8IjRvFElwwu0Hk76W9u5gC5X-YKqJOx_hziM7bM7uEa4xBPmpYc3EbKo4FDZ5eTWYS-O8E-JVTgiXkObFFA3U-TkecNcJ7PRNVQzAwBGCzLVa_KpanjgFdesHaXzBEQIF7xfM2TqxT7HuMg"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_encyrpted.iso" 
```

# BioUtilApplication
The application shows how to decode a ISO file into jpeg image or encode jp2000 or wsq images into respective ISO file.

## Face
#### Face decoder
```run_decoder_face.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.iso=info_face_registration.iso" 
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN" 
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```
#### Face encoder for Auth
```run_encoder_face_auth.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.image=info_face_auth.jp2"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN" 
"io.mosip.biometrics.util.purpose.auth=AUTH"
```
#### Face encoder for Registration
```run_encoder_face_registration.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.image=info_face_registration.jp2" "mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

## Iris
#### Iris decoder
```run_decoder_iris.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.iris.folder.path=/BiometricInfo/Iris/"
"mosip.mock.sbi.biometric.type.file.iso=info_right_registration.iso" 
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```
#### Iris encoder for Auth
```run_encoder_iris_auth.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.iris.folder.path=/BiometricInfo/Iris/"
"mosip.mock.sbi.biometric.type.file.image=info_left_auth.jp2"
"mosip.mock.sbi.biometric.subtype.left=Left" 
"io.mosip.biometrics.util.purpose.auth=AUTH"
```
#### Iris encoder for Registration
```run_encoder_iris_registration.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.iris.folder.path=/BiometricInfo/Iris/"
"mosip.mock.sbi.biometric.type.file.image=info_right_registration.jp2"
"mosip.mock.sbi.biometric.subtype.right=Right"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

## Finger
#### Finger decoder for JP2000
```run_decoder_finger_jp2000.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_index_registration_jp2000.iso"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION" 
```
#### Finger decoder for WSQ
```run_decoder_finger_wsq.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.wsq=1" 
"io.mosip.biometrics.util.convert.iso.to.image=1"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_thumb_auth_wsq.iso" 
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```
#### Finger encoder for Auth With JP2000
```run_encoder_finger_jp2000_auth.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.image=info_left_index_auth.jp2"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN" "io.mosip.biometrics.util.purpose.auth=AUTH"
```
#### Finger encoder for Auth With WSQ
```run_encoder_finger_wsq_auth.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.wsq=1" 
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.image=info_left_thumb_auth.wsq"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN" 
"io.mosip.biometrics.util.purpose.auth=AUTH"
```
#### Finger encoder for Registration with JP2000
```run_encoder_finger_jp2000_registration.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" 
"io.mosip.biometrics.util.convert.image.to.iso=0"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.image=info_left_index_registration.jp2"
"mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN"
"io.mosip.biometrics.util.purpose.registration=REGISTRATION"
```

# BioUtilConvertApplication
The application shows how to Convert one form of ISO [containing JP2000 or WSQ image] to ISO [containing JPEG or PNG image]

## Face
#### Face convert from JP2000 to JPEG
```run_convert_face_JP2000_JPEG.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.jpeg=2"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.iso=info_face_registration.iso"
```
#### Face convert from JP2000 to PNG
```run_convert_face_JP2000_PNG.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.png=3"
"mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/"
"mosip.mock.sbi.biometric.type.file.iso=info_face_registration.iso"
```

## Iris
#### Iris convert from JP2000 to JPEG
```run_convert_iris_JP2000_JPEG.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.jpeg=2"
"mosip.mock.sbi.biometric.type.iris.folder.path=/BiometricInfo/Iris/"
"mosip.mock.sbi.biometric.type.file.iso=info_right_registration.iso"
```
#### Iris convert from JP2000 to PNG
```run_convert_iris_JP2000_PNG.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.png=3"
"mosip.mock.sbi.biometric.type.iris.folder.path=/BiometricInfo/Iris/"
"mosip.mock.sbi.biometric.type.file.iso=info_right_registration.iso"
```

## Finger
#### Finger convert from JP2000 to JPEG
```run_convert_finger_JP2000_JPEG.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.jpeg=2"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_index_registration_jp2000.iso"
```
#### Finger convert from JP2000 to PNG
```run_convert_finger_JP2000_PNG.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.png=3"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_index_registration_jp2000.iso" 
```
#### Finger convert from WSQ to JPEG
```run_convert_finger_WSQ_JPEG.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.jpeg=2"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_thumb_auth_wsq.iso"
```
#### Finger convert from WSQ to PNG
```run_convert_finger_WSQ_PNG.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.BioUtilConvertApplication
"io.mosip.biometrics.util.image.type.png=3"
"mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/"
"mosip.mock.sbi.biometric.type.file.iso=info_left_thumb_auth_wsq.iso"
```

# SampleNistFileReader
The application shows how to parse the different types of NIST files.

```run_nist_file_reader.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.SampleNistFileReader
"mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistXmlData/" 
```

# NistDataQualityAnalyser
The application shows how to parse the NIST files read the finger[WSQ], iris[JPEGL], face[JPEGL] base64 encoded biometric images do the quality analysis of each record and save them to csv file.<br>

```run_nist_data_quality_analyser.bat
java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* 
io.mosip.biometrics.util.test.NistDataQualityAnalyser
"mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistDataQualityAnalyser/"
"bqat.server.ipaddress=91.203.134.4" 
"bqat.server.port=:8848" 
"bqat.server.path=/base64?urlsafe=false" 
"bqat.content.type=application/json" 
"bqat.content.charset=utf-8" 
"bqat.json.results=results"
```

## License

[MIT](https://choosealicense.com/licenses/mit/)