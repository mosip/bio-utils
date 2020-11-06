#!/bin/sh

echo "Starting Bio Util Application..."

nohup java -cp bioutils-0.0.1-SNAPSHOT.jar:lib/* org.mosip.bio.utils.test.BioUtilApplication "org.mosip.bio.utils.convert.jp2000.to.iso=0" "mosip.mock.sbi.biometric.type.face.folder.path=/BiometricInfo/Face/" "org.mosip.bio.utils.biometric.type.file.jp2000=info.jp2" "mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN" > /tmp/mock_mds.log 2>&1 &

echo "Ended  Bio Util Application."
