package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonCreator {

    private List<String> jsonTransactions = new ArrayList<>();

    public void convertToJson(List<Transaction> transactions) {

        for (Transaction transaction : transactions) {
            String jsonTransaction = generateTransactionString(transaction);
            jsonTransactions.add(jsonTransaction);
        }
    }

    public void getOutputFile(List<Transaction> transactions) {
        convertToJson(transactions);

        JSONObject root = new JSONObject();
        JSONArray msgList = new JSONArray();
        msgList.addAll(jsonTransactions);
        root.put("msg-list", msgList);

        JSONObject totals = new JSONObject();
        totals.put("cnt", Integer.toString(transactions.size()));
        totals.put("sum", calculateTotalAmount(transactions).toString());
        totals.put("date", getCurrentDate());
        root.put("totals", totals);

        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("root", root);

        System.out.println(jsonOutput.toJSONString());

        try (FileWriter fileWriter = new FileWriter("Output.json")) {
            fileWriter.write(jsonOutput.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal calculateTotalAmount(List<Transaction> transactions) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (Transaction transaction : transactions) {
            BigDecimal decimalValue = new BigDecimal(transaction.getAmount());

            totalAmount = totalAmount.add(decimalValue);
        }
        return totalAmount;
    }

    public String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    private String maskPAN(String input) {
        if (input.length() <= 10) {
            return input;
        }

        return input.substring(0, 6) + "********" + input.substring(input.length() - 4);
    }

    private String getType(String input){
        if (input.equals("00")) return "Purchase";
        else if (input.equals("01")) return "Withdrawal";
        else return "";
    }

    private String getCurrency(String input){
        switch (input) {
            case "840":
                return "usd";
            case "978":
                return "eur";
            case "826":
                return "gbp";
            case "643":
                return "rub";
            default:
                return "";
        }


    }

    public String formatTime(String input) {
        String year = input.substring(0, 4);
        String date = input.substring(4, 6);
        String month = input.substring(6, 8);
        String hours = input.substring(8, 10);
        String minutes = input.substring(10, 12);

        return date + "." + month + "." + year + " " + hours + ":" + minutes;
    }

    private String generateTransactionString(Transaction transaction) {
        String type = getType(transaction.getType());
        String maskedPAN = maskPAN(transaction.getPAN());
        String date = formatTime(transaction.getTime());
        String amount = transaction.getAmount();
        String currency = getCurrency(transaction.getCode());

        return type + " with card " + maskedPAN + " on " + date + ", amount " + amount + " " + currency + ".";
    }

}
