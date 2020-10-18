How to detect native memory leak
* You need enforce GC pressure by setting low value of Xmx
* Also set -Dio.netty.leakDetection.level=PARANOID
```
-Xms32m
-Xmx32m
-Dio.netty.leakDetection.level=PARANOID
```

Full tested conf
```
-Xms32m
-Xmx32m
-XX:MetaspaceSize=128M
-XX:MaxMetaspaceSize=128M
-Dio.netty.leakDetection.level=PARANOID
-XX:NativeMemoryTracking=summary
-XX:MaxDirectMemorySize=2m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/tmp
```

If you have problems with native memory, which is used by Netty, consider to tune
`-Djdk.nio.maxCachedBufferSize` and `-XX:MaxDirectMemorySize`
See [also](https://dzone.com/articles/troubleshooting-problems-with-native-off-heap-memo).