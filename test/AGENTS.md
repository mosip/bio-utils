# test

```
CLI · NOT reactor · cwd=test/
├─ mvn -pl biometrics-util -am install "-Dgpg.skip=true"
├─ cd test && mvn package "-Dgpg.skip=true"
└─ mvn -q dependency:copy-dependencies -DoutputDirectory=target/lib -DincludeScope=runtime
```

```
run_*.bat|.sh → target/bioutils-$VER.jar + target/lib/*
├─ decoder/encoder/convert Face·Iris·Finger
└─ Jp2ToWsq → *.wsq (lossy 0.75) + *.lossless.wsq
```
