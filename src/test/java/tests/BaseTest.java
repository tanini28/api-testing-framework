package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.api.PlayerController;
import org.example.api.client.DefaultRestClient;
import org.example.api.client.RestClient;
import org.example.config.TestConfig;
import org.example.utils.LogUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static final TestConfig config = TestConfig.getInstance();

    protected RestClient restClient;
    protected PlayerController playerController;

    @BeforeSuite
    public void setUp() {
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(LogDetail.ALL),
                new ResponseLoggingFilter(LogDetail.ALL)
        );

        restClient = new DefaultRestClient(config);
        playerController = new PlayerController(restClient);

        LogUtils.logInfo(logger, "Test environment setup completed");
        LogUtils.logInfo(logger, "Base API URL: " + config.getBaseUrl());
        LogUtils.logInfo(logger, "Thread count for tests: " + config.getThreadCount());
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        LogUtils.logStart(logger, method.getName());
    }

    @AfterMethod
    public void afterMethod(ITestResult result, Method method) {
        if (result.getStatus() == ITestResult.FAILURE) {
            LogUtils.logError(logger, "Test " + method.getName() + " failed");
            LogUtils.logError(logger, "Error reason: " + result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            LogUtils.logInfo(logger, "Test " + method.getName() + " passed successfully");
        }
        LogUtils.logEnd(logger, method.getName());
    }
}
