package org.example;

import java.io.File;
import java.util.List;

public class Main {
    static LogCreator logCreator = new LogCreator();
    public static void main(String[] args) {
        logCreator.configureLogger();
        logCreator.logInfo("Logger configured");

        if (args.length < 1 || args.length > 2) {
            logCreator.logError("No input or more files than needed");
            return;
        }

        String transactionFilePath = args[0];
        String messageFilePath = args.length == 2 ? args[1] : null;

        File transactionFile = new File(transactionFilePath);
        if (!transactionFile.exists()) {
            logCreator.logError("No transactions");
            return;
        }

        FileReader fileWork = new FileReader();
        fileWork.getInformationFromFile(transactionFile);
        logCreator.logInfo("File was read");
        List<Transaction> transactions = fileWork.getTransactions();

        JsonCreator jsonCreator = new JsonCreator();
        if (messageFilePath != null) {
            File messageFile = new File(messageFilePath);
            jsonCreator.getOutputFile(transactions, messageFile);
            logCreator.logInfo("Output file was created");
        } else {
            jsonCreator.getOutputFile(transactions, new File("StandartOutput.json"));
            logCreator.logInfo("Standard file was created");
        }
    }
}