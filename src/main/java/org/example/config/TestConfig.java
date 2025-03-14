package org.example.config;

import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

    private static final Logger logger = LogManager.getLogger(TestConfig.class);

    @Getter
    private static TestConfig instance;
    private final Properties properties;

    static {
        instance = new TestConfig();
    }

    private TestConfig() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getPropertiesStream()) {
            if (input != null) {
                properties.load(input);
                logger.info("Successfully loaded test.properties");
            } else {
                logger.warn("Unable to find test.properties, using default values");
                setDefaultProperties();
            }
        } catch (IOException ex) {
            logger.error("Error loading test.properties: " + ex.getMessage(), ex);
            setDefaultProperties();
        }
    }

    private InputStream getPropertiesStream() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("test.properties");

        if (input == null) {
            logger.info("Unable to find test.properties in classpath, trying file system...");
            try {
                input = new FileInputStream("src/main/resources/test.properties");
            } catch (IOException e) {
                logger.info("Unable to find test.properties in file system");
            }
        }

        return input;
    }

    public String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public int getThreadCount() {
        return Integer.parseInt(properties.getProperty("test.thread.count", "3"));
    }

    public String getSupervisorLogin() {
        return properties.getProperty("user.supervisor.login");
    }

    public String getSupervisorPassword() {
        return properties.getProperty("user.supervisor.password");
    }

    public String getAdminLogin() {
        return properties.getProperty("user.admin.login");
    }

    public String getAdminPassword() {
        return properties.getProperty("user.admin.password");
    }

    private void setDefaultProperties() {
        properties.setProperty("api.base.url", "http://localhost:8080");
        properties.setProperty("api.swagger.url", "http://localhost:8080/swagger-ui.html");
        properties.setProperty("test.thread.count", "3");
        properties.setProperty("user.supervisor.login", "supervisor");
        properties.setProperty("user.supervisor.password", "supervisor");
        properties.setProperty("user.admin.login", "admin");
        properties.setProperty("user.admin.password", "admin");
        properties.setProperty("logging.level", "INFO");
        properties.setProperty("logging.file.path", "logs");
        properties.setProperty("logging.file.name", "api-tests.log");
    }
}
