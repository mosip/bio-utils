# biometrics-util

```
ISO19794 ↔ image · no Spring
├─ finger 19794-4 · face 19794-5 · iris 19794-6
├─ JP2 ← jai-imageio · Mat ← opencv
├─ WSQ decode ← jnbis · encode ← nist/wsq/encoder
│  ├─ lossy 0.75 AUTH · convertJP2ToWSQ
│  ├─ lossless · convertJP2ToWSQLossless · Huffman 16-bit extras
│  └─ DTT mantissa ← WsqUtil.sroundU32 (not int)
├─ NIST ITL 1-2011 XML
└─ Jackson2 ← spring-boot-jackson2
```

```
deps: kernel-core · no kernel-bom
```
