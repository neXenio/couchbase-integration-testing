package com.nexenio.couchbase.testing;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends IntegrationTestSuite {

    private final String USER_ID = "9e565cb9-ae43-4aa0-9f92-997595881577";
    private final String USER_NAME = "John Doe";

    @Autowired
    private UserRepository repository;


    @Test
    public void findById_succeeds() {
        // arrange
        User user = new User(USER_ID, USER_NAME);
        repository.getCouchbaseOperations().insert(user);

        // act
        Optional<User> result = repository.findById(USER_ID);

        // assert
        assertThat(result).isPresent();
    }

    @Test
    public void findById_succeeds_again() {
        // arrange
        User user = new User(USER_ID, USER_NAME);
        repository.getCouchbaseOperations().insert(user);

        // act
        Optional<User> result = repository.findById(USER_ID);

        // assert
        assertThat(result).isPresent();
    }
}
