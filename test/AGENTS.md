# test

```
CLI · NOT reactor · cwd=test/
├─ mvn -pl biometrics-util -am install "-Dgpg.skip=true"
└─ cd test && mvn package "-Dgpg.skip=true"   # copies target/lib
```

```
run_*.bat|.sh → target/bioutils-$VER.jar + target/lib/*
├─ decoder/encoder/convert Face·Iris·Finger
└─ Jp2ToWsq → *.wsq (lossy 1.5) + *.lossless.wsq (sip 16384)
```
