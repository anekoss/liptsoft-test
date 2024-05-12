package org.liptsoft.app.service;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import model.AddTransactionRequest;
import model.CategoryTransaction;
import model.MonthEnum;
import model.Transaction;
import model.TransactionWithPercent;
import org.liptsoft.app.domain.CategoryEntity;
import org.liptsoft.app.domain.MccEntity;
import org.liptsoft.app.domain.TransactionEntity;
import org.liptsoft.app.exception.NoExistCategoryNameException;
import org.liptsoft.app.exception.NoExistMccCategoryException;
import org.liptsoft.app.exception.NotFoundTransactionException;
import org.liptsoft.app.repository.CategoryRepository;
import org.liptsoft.app.repository.MccRepository;
import org.liptsoft.app.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private static final int MAX_PERCENT = 100;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final MccRepository mccRepository;

    @Transactional
    public List<String> addTransaction(String categoryName, AddTransactionRequest transactionRequest) {
        TransactionEntity transaction = new TransactionEntity(transactionRequest.getTransaction()
            .getValue(), transactionRequest.getTransaction()
            .getMonth());
        Set<CategoryEntity> categories = transaction.getCategories();
        categories.add(findCategoryByName(categoryName));
        for (var mcc : transactionRequest.getMcc()) {
            categories.add(findCategoryByMcc(mcc));
        }
        transactionRepository.saveAndFlush(transaction);
        return categories.stream().map(CategoryEntity::getName).toList();
    }

    @Transactional
    public Transaction deleteTransaction(String categoryName, Transaction transaction) {
        CategoryEntity category = findCategoryByName(categoryName);
        TransactionEntity deleteTransaction = category.getTransactions()
            .stream()
            .filter(entity -> entity.getValue()
                .equals(transaction.getValue()) && entity.getMonth()
                .equals(transaction.getMonth()))
            .findFirst()
            .orElseThrow(NotFoundTransactionException::new);
        category.getTransactions().remove(deleteTransaction);
        categoryRepository.saveAndFlush(category);
        return transaction;
    }

    @Transactional
    public List<CategoryTransaction> getTransactionByCategory(String categoryName) {
        CategoryEntity category = findCategoryByName(categoryName);
        Map<MonthEnum, Double> monthTransactions = new HashMap<>();
        calculateTransactionsForCategory(category, monthTransactions);
        return monthTransactions.entrySet()
            .stream()
            .map(transaction -> new CategoryTransaction(transaction.getValue(), transaction.getKey()))
            .toList();
    }

    @Transactional
    public List<TransactionWithPercent> getTransactionByMonth(MonthEnum month) {
        List<TransactionEntity> transactions = transactionRepository.findByMonthOrderByIdAsc(month);
        Map<String, Double> categoryTransactions = new HashMap<>();
        for (var transaction : transactions) {
            for (var category : transaction.getCategories()) {
                categoryTransactions.merge(category.getName(), transaction.getValue(), Double::sum);
            }
        }
        for (var transaction : transactions) {
            for (var category : transaction.getCategories()) {
                calculateTransactionsForMonth(category, categoryTransactions, month);
            }
        }
        double sum = transactions.stream().mapToDouble(TransactionEntity::getValue).sum();
        return categoryTransactions.entrySet()
            .stream()
            .map(transaction -> new TransactionWithPercent(
                transaction.getKey(),
                transaction.getValue(),
                transaction.getValue() == 0.0 ? 0 : (int) Math.round(transaction.getValue() / sum * MAX_PERCENT)
            ))
            .toList();
    }

    private void calculateTransactionsForMonth(
        CategoryEntity category,
        Map<String, Double> categoryTransactions,
        MonthEnum monthEnum
    ) {
        for (CategoryEntity subcategory : category.getSubCategories()) {
            for (var transaction : subcategory.getTransactions()) {
                if (transaction.getMonth() == monthEnum) {
                    categoryTransactions.merge(category.getName(), transaction.getValue(), Double::sum);
                }
            }
        }
    }

    private void calculateTransactionsForCategory(CategoryEntity category, Map<MonthEnum, Double> monthTransactions) {
        for (TransactionEntity transaction : category.getTransactions()) {
            monthTransactions.merge(transaction.getMonth(), transaction.getValue(), Double::sum);
        }
        for (CategoryEntity subcategory : category.getSubCategories()) {
            calculateTransactionsForCategory(subcategory, monthTransactions);
        }
    }

    private CategoryEntity findCategoryByName(String categoryName) {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findByName(categoryName);
        if (optionalCategory.isEmpty()) {
            throw new NoExistCategoryNameException(categoryName);
        }
        return optionalCategory.get();
    }

    private CategoryEntity findCategoryByMcc(Integer mcc) {
        Optional<MccEntity> optionalMcc = mccRepository.findByMcc(mcc);
        if (optionalMcc.isEmpty()) {
            throw new NoExistMccCategoryException(mcc);
        }
        return optionalMcc.get().getCategory();
    }

}
