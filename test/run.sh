#!/bin/sh

echo "Starting Bio Util Application..."

nohup java -cp bioutils-0.0.1-SNAPSHOT.jar:lib/* io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.convert.jp2000.to.iso=0" "mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/" "mosip.mock.sbi.biometric.type.file.jp2000=info.jp2" "mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN" "io.mosip.biometrics.util.purpose.registration=REGISTRATION" > /tmp/mock_mds.log 2>&1 &

echo "Ended  Bio Util Application."
