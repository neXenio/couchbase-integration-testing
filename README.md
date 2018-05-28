# Couchbase Integration Testing
## Prequesites
In order to successfully run these tests, you need a running couchbase on `localhost` with a bucket `default`.
You can start a local Couchbase using docker: 
```
docker run -d -p 8091:8091 couchbase
```

Start the tests with calling `./gradlew test`.
