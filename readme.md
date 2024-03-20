# Preparations and checks
```
curl -X POST -H 'Content-Type: application/json' -d '{"input": "hello, there!"}' --url 'http://localhost:8282/foo'
echo '{"input": "hello, there!"}' > post.json
```

# Test

Start a microservice
```
cd foo-microservice
./mvnw spring-boot:run
```

Start the gateway
```
cd gateway
./mvnw spring-boot:run
```

Run Apache Benchmark (ab)
```
ab -n 1000000 -p post.json -T application/json -c 8 'http://localhost:8282/foo'
```
