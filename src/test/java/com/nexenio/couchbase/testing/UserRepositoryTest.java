package com.nexenio.couchbase.testing;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends IntegrationTestSuite {

    private final String USER_ID = UUID.randomUUID().toString();
    private final String USER_NAME = "John Doe";

    @Autowired
    private UserRepository repository;

    @Before
    public void setUp() {
        User user = new User(USER_ID, USER_NAME);
        repository.getCouchbaseOperations().insert(user);
    }

    @Test
    public void findById_succeeds() {
        // act
        Optional<User> result = repository.findById(USER_ID);

        // assert
        assertThat(result).isPresent();
    }

    @Test
    public void findByName_succeeds() {
        // act
        List<User> result = repository.findByName(USER_NAME);

        // assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(USER_ID);
    }
}
