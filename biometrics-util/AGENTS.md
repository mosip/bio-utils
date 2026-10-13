# biometrics-util

```
ISO19794 ↔ image · no Spring
├─ finger 19794-4 · face 19794-5 · iris 19794-6
├─ JP2 ← jai-imageio · Mat ← opencv
├─ WSQ decode ← jnbis
│  encode ← nist/wsq/encoder
│  ├─ WsqEncoder.encode          DEFAULT_BIT_RATE 1.5
│  ├─ WsqEncoder.encodeLossless  LOSSLESS_SIP_RANGE 16384 · Z=Q · no bitrate cap
│  ├─ CommonUtil.convertJP2ToWSQ | convertJP2ToWSQLossless
│  └─ DTT mantissa ← WsqUtil.sroundU32 (not int)
├─ NIST ITL 1-2011 XML
└─ Jackson2 ← spring-boot-jackson2
```

```
deps: kernel-core · no kernel-bom
```
