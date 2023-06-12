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
    private final Logger logger;

    public LogCreator() {
        logger = LogManager.getLogger(LogCreator.class);
    }

    public void configureLogger() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String PROGRAM_NAME = "trn2msg";
        String logFileName = PROGRAM_NAME + "_" + dateFormat.format(new Date()) + ".log";

        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();

        for (Appender appender : config.getLoggerConfig(logger.getName()).getAppenders().values()) {
            config.getLoggerConfig(logger.getName()).removeAppender(appender.getName());
        }

        String LOG_PATTERN = "%d{HH:mm:ss.SSS} %msg%n";
        FileAppender fileAppender = FileAppender.newBuilder()
                .withFileName(logFileName)
                .withAppend(true)
                .withLayout(PatternLayout.newBuilder().withPattern(LOG_PATTERN).build())
                .setName("FileAppender")
                .build();
        fileAppender.start();

        config.getLoggerConfig(logger.getName()).addAppender(fileAppender, Level.ALL, null);
        config.getLoggerConfig(logger.getName()).setLevel(Level.ALL);
        context.updateLoggers();
    }

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logError(String message) {
        logger.error(message);
    }
}