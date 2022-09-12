package org.example;

import com.example.transaction.service.TransactionService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TransactionService transactionService = new TransactionService();
        transactionService.processTransactions();
        transactionService.aggregateOnAccCode();
        transactionService.aggregateOnAccCodeAndDate();
        transactionService.aggregateOnAccCodeTypeAndDate();
    }
}