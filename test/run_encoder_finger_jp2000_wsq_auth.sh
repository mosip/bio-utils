#!/bin/sh
set -e
cd "$(dirname "$0")"
VER="${BIOUTILS_VERSION:-1.4.1-SNAPSHOT}"
if [ -f "target/bioutils-${VER}.jar" ]; then
  CP="target/bioutils-${VER}.jar:target/lib/*"
elif [ -f "bioutils-${VER}.jar" ]; then
  CP="bioutils-${VER}.jar:lib/*"
else
  echo "bioutils-${VER}.jar not found. Run: mvn clean package -Dgpg.skip=true && mvn -q dependency:copy-dependencies -DoutputDirectory=target/lib -DincludeScope=runtime" >&2
  exit 1
fi
if [ ! -f "BiometricInfo/Finger/info_left_index_auth.wsq" ]; then
  echo "info_left_index_auth.wsq not found. Run ./run_convert_finger_JP2000_WSQ.sh first." >&2
  exit 1
fi
java -cp "$CP" io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.wsq=1" "io.mosip.biometrics.util.convert.image.to.iso=0" "mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/" "mosip.mock.sbi.biometric.type.file.image=info_left_index_auth.wsq" "mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN" "io.mosip.biometrics.util.purpose.auth=AUTH"
