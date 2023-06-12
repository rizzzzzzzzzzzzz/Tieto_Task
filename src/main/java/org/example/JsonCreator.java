package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JsonCreator {

    private List<String> jsonTransactions = new ArrayList<>();

    private void convertToJson(List<Transaction> transactions) {

        for (Transaction transaction : transactions) {
            String jsonTransaction = generateTransactionString(transaction);
            jsonTransactions.add(jsonTransaction);
        }
    }

    public void getOutputFile(List<Transaction> transactions, File file) {
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

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonOutput.toJSONString());
            //log info
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

    private String getCurrentDate() {
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

    private String formatAmount(String input) {
        int length = input.length();
        int scale = length - 2;

        BigDecimal bigDecimal = new BigDecimal(input);
        bigDecimal = bigDecimal.setScale(scale).movePointLeft(2).stripTrailingZeros();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');

        return new DecimalFormat("0.00", symbols).format(bigDecimal);
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
        String amount = formatAmount(transaction.getAmount());
        String currency = getCurrency(transaction.getCode());

        return type + " with card " + maskedPAN + " on " + date + ", amount " + amount + " " + currency + ".";
    }

}
