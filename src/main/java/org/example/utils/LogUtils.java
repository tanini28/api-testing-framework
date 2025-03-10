package org.example.utils;

import org.apache.logging.log4j.Logger;

public class LogUtils {

    public static void logInfo(Logger logger, String message) {
        logger.info("INFO: " + message);
    }

    public static void logError(Logger logger, String message) {
        logger.error("ERROR: " + message);
    }

    public static void logDebug(Logger logger, String message) {
        logger.debug("DEBUG: " + message);
    }

    public static void logWarning(Logger logger, String message) {
        logger.warn("WARNING: " + message);
    }

    public static void logStart(Logger logger, String testName) {
        logger.info("=================================");
        logger.info("STARTING TEST: " + testName);
        logger.info("=================================");
    }

    public static void logEnd(Logger logger, String testName) {
        logger.info("=================================");
        logger.info("ENDING TEST: " + testName);
        logger.info("=================================");
    }
}
