To make your microservices resilient to restart consul you should 
1) set `spring.cloud.consul.discovery.heartbeat.enabled: true` and 
2) run consul v1.7.1 as
```bash
./consul agent -data-dir /var/tmp/consul -advertise 127.0.0.1 -server -bootstrap
```