package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionService {

    //property file
    final String filePath = "D:\\code\\input";
    List<Transaction> transactionList = new ArrayList<Transaction>();

    public void processTransactions() throws IOException {
        System.out.println("Starting processing");
        try {
            File csvData = new File(filePath + "\\transactions.csv");
            CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : parser) {
                Transaction transaction = new Transaction();
                transaction.setMerchant(csvRecord.get("Merchant"));
                transaction.setPaymentId(csvRecord.get("PaymentId"));
                transaction.setPaymentDate(LocalDate.parse(csvRecord.get("ActionDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                transaction.setCreditDebitCode(csvRecord.get("CreditDebitCode"));
                transaction.setAmount(Double.parseDouble(csvRecord.get("Amount")));
                transaction.setAccountCode(csvRecord.get("AccountCode"));
                transaction.setAccountType(csvRecord.get("AccountType"));
                transactionList.add(transaction);
            }
        } catch (IOException e) {
            System.out.println("error processing file " + e.getMessage());
            throw e;
        }

    }

    public void aggregateOnAccCode() throws IOException {

        FileWriter out = new FileWriter(filePath + "\\AccCode-" + LocalDateTime.now().getSecond() + ".csv");
        final CSVPrinter printer = CSVFormat.DEFAULT.withHeader("AccountCode", "Amount").print(out);

        Map<String, List<Transaction>> groupList = groupList(transactionList, "accountCode");

        System.out.println("AccountCode\t\tAmount");

        groupList.entrySet().forEach(entry -> {
            Double amt = calculateAmount(entry.getValue());
            System.out.println(entry.getKey() + "\t\t" + amt);
            try {
                printer.printRecord(entry.getKey(), amt);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        printer.close();
        System.out.println("\n");
    }

    public void aggregateOnAccCodeAndDate() throws IOException {

        FileWriter out = new FileWriter(filePath + "\\AccCodeDate-" + LocalDateTime.now().getSecond() + ".csv");
        final CSVPrinter printer = CSVFormat.DEFAULT.withHeader("Date","AccountCode", "Amount").print(out);

        Map<LocalDate, List<Transaction>> groupedByDate = transactionList.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentDate));

        System.out.println("Date\t\tAccountCode\t\tAmount");

        groupedByDate.entrySet().forEach(entry -> {
            Map<String, List<Transaction>> byCodeDate = groupList(entry.getValue(), "accountCode");
            byCodeDate.forEach((code, list) -> {
                Double amt = calculateAmount(list);
                System.out.println(entry.getKey() + "\t\t" + code + "\t\t" + amt);
                try {
                    printer.printRecord(entry.getKey(), code,amt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        printer.close();
        System.out.println("\n");
    }

    public void aggregateOnAccCodeTypeAndDate() throws IOException {

        FileWriter out = new FileWriter(filePath + "\\AccCodeTypeDate-" + LocalDateTime.now().getSecond() + ".csv");
        final CSVPrinter printer = CSVFormat.DEFAULT.withHeader("Date","AccountCode","AccountType","Amount").print(out);


        Map<LocalDate, List<Transaction>> groupedByDate = transactionList.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentDate));

        System.out.println("Date\t\tAccountCode\t\tType\t\tAmount");

        groupedByDate.entrySet().forEach(entry -> {
            Map<String, List<Transaction>> byCodeDate = groupList(entry.getValue(), "accountCode");
            byCodeDate.forEach((code, list) -> {
                Map<String, List<Transaction>> byCodeTypeDate = groupList(entry.getValue(), "accountType");
                byCodeTypeDate.forEach((type, typeList) -> {
                    Double amt = calculateAmount(typeList);
                    System.out.println(entry.getKey() + "\t\t" + code + "\t\t" + type + "\t\t" + calculateAmount(typeList));
                    try {
                        printer.printRecord(entry.getKey(), code,type,amt);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        });
        printer.close();
        System.out.println("\n");
    }

    public Map<String, List<Transaction>> groupList(List<Transaction> list, String field) {
        switch (field) {
            case "accountCode":
                return list.stream().collect(Collectors.groupingBy(Transaction::getAccountCode));
            case "accountType":
                return list.stream().collect(Collectors.groupingBy(Transaction::getAccountType));
        }
        return null;
    }

    public Double calculateAmount(List<Transaction> list) {
        return list.stream()
                .map(t -> {
                    if (t.getCreditDebitCode().equalsIgnoreCase("c"))
                        return t.getAmount();
                    else return -t.getAmount();
                })
                .reduce((i, j) -> {
                    return i + j;
                }).get();
    }

}
