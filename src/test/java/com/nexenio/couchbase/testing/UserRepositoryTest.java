package com.nexenio.couchbase.testing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends IntegrationTestSuite {

    private final String USER_ID = "9e565cb9-ae43-4aa0-9f92-997595881577";
    private final String USER_NAME = "John Doe";

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User(USER_ID, USER_NAME);
    }

    @Test
    public void findById_succeeds() {
        repository.getCouchbaseOperations().insert(user);

        assertThat(repository.findById(USER_ID)).isPresent();
    }

    @Test
    public void findById_succeedsAgain() {
        repository.getCouchbaseOperations().insert(user);

        assertThat(repository.findById(USER_ID)).isPresent();
    }
}
