package org.example.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

    private static TestConfig instance;
    private Properties properties;

    private TestConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("test.properties")) {
            if (input == null) {
                System.out.println("Unable to find test.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized TestConfig getInstance() {
        if (instance == null) {
            instance = new TestConfig();
        }
        return instance;
    }

    public String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public String getSwaggerUrl() {
        return properties.getProperty("api.swagger.url");
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

    public String getLoggingLevel() {
        return properties.getProperty("logging.level");
    }

    public String getLoggingFilePath() {
        return properties.getProperty("logging.file.path");
    }

    public String getLoggingFileName() {
        return properties.getProperty("logging.file.name");
    }
}
