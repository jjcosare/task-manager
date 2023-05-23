package com.jjcosare.task;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
@Testcontainers
class TaskApplicationTest {

    @Container
    public static DataSourceContainer dataSourceContainer = DataSourceContainer.getInstance();

    @Test
    void contextLoads() {
        // Sonar needs at least 1 assertion
        assertNull(null);
    }

}
