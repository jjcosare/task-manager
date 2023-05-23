package com.jjcosare.task;

import org.testcontainers.containers.PostgreSQLContainer;

public class DataSourceContainer extends PostgreSQLContainer<DataSourceContainer> {

    private static final String DOCKER_DATASOURCE_VERSION = "postgres:15";

    private static DataSourceContainer dataSourceContainer;

    private DataSourceContainer() {
        super(DOCKER_DATASOURCE_VERSION);
    }

    public static DataSourceContainer getInstance() {
        if (dataSourceContainer == null) {
            dataSourceContainer = new DataSourceContainer();
            dataSourceContainer.start();
            dataSourceContainer.waitUntilContainerStarted();
            System.setProperty("DATASOURCE_DRIVER", dataSourceContainer.getDriverClassName());
            System.setProperty("DATASOURCE_URL", dataSourceContainer.getJdbcUrl());
            System.setProperty("DATASOURCE_USERNAME", dataSourceContainer.getUsername());
            System.setProperty("DATASOURCE_PASSWORD", dataSourceContainer.getPassword());
        }
        return dataSourceContainer;
    }

    @Override
    public void start() {
        super.start();

    }

    @Override
    public void stop() {
        super.stop();
    }
}