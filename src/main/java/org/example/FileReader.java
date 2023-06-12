package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void getInformationFromFile(File file){

        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))){
            String line;
            while ((line = reader.readLine()) != null){
                Transaction transaction = new Transaction();
                transaction.setType(line.substring(0,2));
                transaction.setPAN(line.substring(2,18));
                transaction.setAmount(line.substring(18,30));
                transaction.setTime(line.substring(30,44));
                transaction.setCode(line.substring(44,47));
                transactions.add(transaction);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
