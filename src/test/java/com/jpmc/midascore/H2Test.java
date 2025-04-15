package com.jpmc.midascore;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("unused")
@SpringBootTest
public class H2Test {

    @Autowired
    private DataSource dataSource;

    @Test
    void testH2DatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Connection to H2 database should not be null");
        }
    }
}
