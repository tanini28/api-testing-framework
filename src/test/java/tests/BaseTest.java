package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @BeforeSuite
    public void setUp() {
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(LogDetail.ALL),
                new ResponseLoggingFilter(LogDetail.ALL)
        );
        LogUtils.logInfo(logger, "Налаштування тестового середовища завершено");
        LogUtils.logInfo(logger, "Базовий URL API: " + config.getBaseUrl());
        LogUtils.logInfo(logger, "Кількість потоків для тестів: " + config.getThreadCount());
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        LogUtils.logStart(logger, method.getName());
    }

    @AfterMethod
    public void afterMethod(ITestResult result, Method method) {
        if (result.getStatus() == ITestResult.FAILURE) {
            LogUtils.logError(logger, "Тест " + method.getName() + " не пройшов");
            LogUtils.logError(logger, "Причина помилки: " + result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            LogUtils.logInfo(logger, "Тест " + method.getName() + " успішно пройшов");
        }
        LogUtils.logEnd(logger, method.getName());
    }
}
