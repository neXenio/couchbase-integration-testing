package com.nexenio.couchbase.testing;

import com.couchbase.client.java.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public abstract class IntegrationTestSuite {

    @Autowired private DocumentDeletionAspect documentDeletionAspect;

    @After
    public void onDestroyCleanup() {
        documentDeletionAspect.purgeDocuments();
    }

    @Configuration
    static class MyTestConfiguration {
        @Bean
        public InitialPurgeRunner createInitialPurgeRunner(Bucket bucket) {
            return new InitialPurgeRunner(bucket);
        }

        @Bean
        public DocumentDeletionAspect createDocumentDeletionAspect(Bucket bucket) {
            return new DocumentDeletionAspect(bucket);
        }
    }
}
