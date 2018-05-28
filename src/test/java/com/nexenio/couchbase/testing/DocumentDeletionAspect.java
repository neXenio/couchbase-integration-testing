package com.nexenio.couchbase.testing;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.Document;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import rx.Observable;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class DocumentDeletionAspect {

    private final Set<String> documentIds = ConcurrentHashMap.newKeySet();

    private final Bucket bucket;

    @Around("execution(* com.couchbase.client.java.Bucket.query (com.couchbase.client.java.query.N1qlQuery, ..))")
    public Object query(ProceedingJoinPoint pjp) throws Throwable {
        N1qlQuery query = (N1qlQuery) pjp.getArgs()[0];
        query.params().consistency(ScanConsistency.REQUEST_PLUS);

        return pjp.proceed();
    }

    @After("execution(* com.couchbase.client.java.Bucket.insert (..))")
    public void insert(JoinPoint jp) {
        storeDocumentId(jp);
    }

    @After("execution(* com.couchbase.client.java.Bucket.upsert (..))")
    public void upsert(JoinPoint jp) {
        storeDocumentId(jp);
    }

    @After("execution(* com.couchbase.client.java.Bucket.replace (..))")
    public void replace(JoinPoint jp) {
        storeDocumentId(jp);
    }

    @After("execution(* com.couchbase.client.java.Bucket.counter (..))")
    public void counter(JoinPoint jp) {
        storeDocumentId(jp);
    }

    private void storeDocumentId(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length < 1) {
            return;
        }
        Object firstArgument = joinPoint.getArgs()[0];
        String id = null;
        if (firstArgument instanceof Document) {
            Document document = (Document) firstArgument;
            id = document.id();
        }
        if (firstArgument instanceof String) {
            id = (String) firstArgument;
        }

        if (id != null) {
            documentIds.add(id);
            log.debug("Tracking document with id [{}]", id);
        }
    }

    public void purgeDocuments() {
        if (documentIds.isEmpty()) {
            return;
        }
        log.info("Purging {} document(s)...", documentIds.size());

        int deletedItemsCount = Observable
            .from(documentIds)
            .flatMap(this::deleteAsync)
            .toList()
            .toBlocking()
            .single()
            .size();

        assert documentIds.size() == deletedItemsCount;
        documentIds.clear();

        log.info("Purged {} document(s).", deletedItemsCount);
    }

    private Observable<JsonDocument> deleteAsync(String id) {
        return bucket.async().remove(id).onErrorReturn(this::errorHandler);
    }

    private JsonDocument errorHandler(Throwable e) {
        if (! (e instanceof DocumentDoesNotExistException)) {
            log.warn("Error db purging.", e);
        }
        return null;
    }
}
