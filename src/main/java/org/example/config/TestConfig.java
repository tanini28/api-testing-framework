package org.example.config;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

    @Getter
    private static TestConfig instance;
    private final Properties properties;

    private TestConfig() {
        properties = new Properties();
        try {

            InputStream input = getClass().getClassLoader().getResourceAsStream("test.properties");


            if (input == null) {
                System.out.println("Unable to find test.properties in classpath, trying file system...");
                try {
                    input = new FileInputStream("src/main/resources/test.properties");
                } catch (IOException e) {
                    System.out.println("Unable to find test.properties in file system");
                }
            }

            if (input != null) {
                properties.load(input);
                input.close();
                System.out.println("Successfully loaded test.properties");
            } else {
                System.out.println("Unable to find test.properties, using default values");
                setDefaultProperties();
            }
        } catch (IOException ex) {
            System.out.println("Error loading test.properties: " + ex.getMessage());
            ex.printStackTrace();
            setDefaultProperties();
        }
    }

    private static void setInstance(TestConfig instance) {
        TestConfig.instance = instance;
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
