package com.nexenio.couchbase.testing;

import com.couchbase.client.java.repository.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
public class User {
    @Id
    private String id;

    private String name;
}
