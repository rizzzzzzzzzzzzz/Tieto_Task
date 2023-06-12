package org.example;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogCreator {
    private final String LOG_PATTERN = "%d{HH:mm:ss.SSS} %msg%n";
    private final String PROGRAM_NAME = "trn2msg";
    private final Logger logger;

    public LogCreator() {
        logger = LogManager.getLogger(LogCreator.class);
    }

    public void configureLogger() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String logFileName = PROGRAM_NAME + "" + dateFormat.format(new Date()) + ".log";

        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();

        for (Appender appender : config.getLoggerConfig(logger.getName()).getAppenders().values()) {
            config.getLoggerConfig(logger.getName()).removeAppender(appender.getName());
        }


        // Create the FileAppender with the desired log file name and pattern
        FileAppender fileAppender = FileAppender.newBuilder()
                .withFileName(logFileName)
                .withAppend(true)
                .withLayout(PatternLayout.newBuilder().withPattern(LOG_PATTERN).build())
                .setName("FileAppender")
                .build();
        fileAppender.start();

        // Add the FileAppender to the logger
        config.getLoggerConfig(logger.getName()).addAppender(fileAppender, Level.ALL, null);
        config.getLoggerConfig(logger.getName()).setLevel(Level.ALL);
        // Update the logger configuration
        context.updateLoggers();
    }

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logError(String message) {
        logger.error(message);
    }
}