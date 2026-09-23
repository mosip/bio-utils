@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"
if not defined BIOUTILS_VERSION set BIOUTILS_VERSION=1.4.1-SNAPSHOT
set VER=%BIOUTILS_VERSION%
if exist "target\bioutils-%VER%.jar" (
  set "CP=target\bioutils-%VER%.jar;target\lib\*"
) else if exist "bioutils-%VER%.jar" (
  set "CP=bioutils-%VER%.jar;lib\*"
) else (
  echo bioutils-%VER%.jar not found. Run: mvn clean package -Dgpg.skip=true ^&^& mvn -q dependency:copy-dependencies -DoutputDirectory=target/lib -DincludeScope=runtime
  exit /b 1
)
java -cp "!CP!" io.mosip.biometrics.util.test.BioUtilApplication "io.mosip.biometrics.util.image.type.wsq=1" "io.mosip.biometrics.util.convert.image.to.iso=0" "mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/" "mosip.mock.sbi.biometric.type.file.image=info_left_thumb_auth.wsq" "mosip.mock.sbi.biometric.subtype.unknown=UNKNOWN" "io.mosip.biometrics.util.purpose.auth=AUTH"
