package com.nexenio.couchbase.testing;

import static com.couchbase.client.java.query.dsl.Expression.i;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.Delete;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@Slf4j
@AllArgsConstructor
public class InitialPurgeRunner implements ApplicationRunner {

    private static final String PURGE_INDEX = "PurgeIndex";

    private final Bucket bucket;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Purging bucket {} before starting tests...", bucket.name());

        log.info("Creating purge index [{}]...", PURGE_INDEX);
        bucket.bucketManager().createN1qlPrimaryIndex(PURGE_INDEX, true, false);
        log.info("Purging all documents...");
        N1qlQueryResult result = purgeBucket();
        if (! result.finalSuccess()) {
            log.error("Error purging documents: [{}]", result.errors());
        }
        log.info("Removing purge index...");
        bucket.bucketManager().dropN1qlIndex(PURGE_INDEX, true);
    }

    private N1qlQueryResult purgeBucket() {
        Statement statement = Delete.deleteFrom(i(bucket.name())).where("TRUE");
        N1qlParams params = N1qlParams.build().pretty(false).consistency(ScanConsistency.REQUEST_PLUS);
        N1qlQuery query = N1qlQuery.simple(statement, params);
        return bucket.query(query);
    }
}
