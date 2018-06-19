package com.nexenio.couchbase.testing;

import java.util.List;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends CouchbaseRepository<User, String> {

    List<User> findByName(String name);
}
