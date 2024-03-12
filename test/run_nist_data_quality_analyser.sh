#!/bin/sh

echo "Starting NistDataQualityAnalyser Application..."

nohup java -cp bioutils-1.2.1-SNAPSHOT.jar;lib\* io.mosip.biometrics.util.test.NistDataQualityAnalyser "mosip.mock.sbi.biometric.type.nist.folder.path=/BiometricInfo/NistDataQualityAnalyser/" "bqat.server.ipaddress=172.16.124.12" "bqat.server.port=:8848" "bqat.server.path=/base64?urlsafe=false" "bqat.content.type=application/json" "bqat.content.charset=utf-8" "bqat.json.results=results" > /tmp/NistDataQualityAnalyser.log 2>&1 &

echo "Ended  NistDataQualityAnalyser Application."
