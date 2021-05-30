To make your microservices resilient to restart consul you should 
1) set `spring.cloud.consul.discovery.heartbeat.enabled: true` and 
2) run consul v1.7.1 as
```bash
./consul agent -data-dir /var/tmp/consul -advertise 127.0.0.1 -server -bootstrap
```

If you have problems with native memory, which is used by Netty, consider to tune
`-Djdk.nio.maxCachedBufferSize` and `-XX:MaxDirectMemorySize`
See [also](https://dzone.com/articles/troubleshooting-problems-with-native-off-heap-memo).

# Dummy test
```
./consul agent -dev
curl -i 'http://localhost:8282/foo'
```