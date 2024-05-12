package org.liptsoft.app.controller;

import api.TransactionApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import model.AddTransactionRequest;
import model.CategoryTransaction;
import model.MonthEnum;
import model.Transaction;
import model.TransactionWithPercent;
import org.liptsoft.app.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {
    private final TransactionService transactionService;

    @Override
    public ResponseEntity<List<String>> addTransaction(
        String categoryName,
        AddTransactionRequest addTransactionRequest
    ) {
        return ResponseEntity.ok(transactionService.addTransaction(categoryName, addTransactionRequest));
    }

    @Override
    public ResponseEntity<Void> deleteTransaction(String categoryName, Transaction transaction) {
        transactionService.deleteTransaction(categoryName, transaction);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CategoryTransaction>> getTransactionByCategory(String categoryName) {
        return ResponseEntity.ok(transactionService.getTransactionByCategory(categoryName));
    }

    @Override
    public ResponseEntity<List<TransactionWithPercent>> getTransactionByMonth(MonthEnum month) {
        return ResponseEntity.ok(transactionService.getTransactionByMonth(month));
    }

}
