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
java -cp "!CP!" io.mosip.biometrics.util.test.NistDataQualityAnalyser "mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistDataQualityAnalyser/" "bqat.server.ipaddress=91.203.134.4" "bqat.server.port=:8848" "bqat.server.path=/base64?urlsafe=false" "bqat.content.type=application/json" "bqat.content.charset=utf-8" "bqat.json.results=results"
