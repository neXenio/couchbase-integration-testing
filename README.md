# Couchbase Integration Testing
## Prequesites
In order to successfully run these tests, you need a running couchbase on `localhost` with a bucket `default`.
You can start a preconfigured Couchbase locally using docker: 
```
docker run -d --name cb_demo -p 8091-8094:8091-8094 -p 11210-11211:11210-11211 nexenio/couchbase_demo
```

Start the tests with calling `./gradlew clean test`.
