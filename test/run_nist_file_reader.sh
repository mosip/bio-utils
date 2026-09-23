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
java -cp "$CP" io.mosip.biometrics.util.test.SampleNistFileReader "mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistXmlData/"
