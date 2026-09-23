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
java -cp "$CP" io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.jp2000=0" "io.mosip.biometrics.util.convert.image.to.iso=0" "mosip.mock.sbi.biometric.type.iris.folder.path=/BiometricInfo/Iris/" "mosip.mock.sbi.biometric.type.file.image=info_left_auth.jp2" "mosip.mock.sbi.biometric.subtype.left=Left" "io.mosip.biometrics.util.purpose.auth=AUTH"
