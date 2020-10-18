How to detect native memory leak
* You need enforce GC pressure by setting low value of Xmx
* Also set -Dio.netty.leakDetection.level=PARANOID
```
-Xms32m
-Xmx32m
-Dio.netty.leakDetection.level=PARANOID
```

![pic](.markdown/depict-error.png)

# Links
* https://www.youtube.com/watch?v=RwiFssLLW9E