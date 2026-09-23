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
java -cp "!CP!" io.mosip.biometrics.util.test.BioAuthDecoderValueCreaterApplication "mosip.mock.sbi.biometric.transaction.id=SBI1069-208" "mosip.mock.sbi.biometric.time.stamp=2023-01-03T05:56:45Z" "mosip.mock.sbi.biometric.thumb.print=2F6FB5590B21E9526F8E4A23B5CC961021614C236D517794F0A7F29E5BA32C2C" "mosip.mock.sbi.biometric.session.key=eCgWEQpCRs45gVzVW5gsGiGrQtMN567b5qX_NLuEEan2-fDSyK9ppRavASVp-FjdNkAa7fzdoXR7EPh9J3o5IMfQV4S_EGdbi2bON4g3-kOgwbSZ5KR4KjmrZISou6zb7vAUrulkqq0z61qTod8OooIMWNVuMlqEj-WQ1cQS5B2l7B5eJ-i-8eIZLBcBzKx82yjNC4O8IjRvFElwwu0Hk76W9u5gC5X-YKqJOx_hziM7bM7uEa4xBPmpYc3EbKo4FDZ5eTWYS-O8E-JVTgiXkObFFA3U-TkecNcJ7PRNVQzAwBGCzLVa_KpanjgFdesHaXzBEQIF7xfM2TqxT7HuMg" "mosip.mock.sbi.biometric.type.finger.folder.path=/BiometricInfo/Finger/" "mosip.mock.sbi.biometric.type.file.iso=info_encyrpted.iso"
