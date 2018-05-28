package com.nexenio.couchbase.testing;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends CouchbaseRepository<User, String> {
}
