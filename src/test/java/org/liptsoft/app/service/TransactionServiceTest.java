package org.liptsoft.app.service;

import jakarta.transaction.Transactional;
import java.util.List;
import model.AddTransactionRequest;
import model.CategoryTransaction;
import model.MonthEnum;
import model.Transaction;
import model.TransactionWithPercent;
import org.junit.jupiter.api.Test;
import org.liptsoft.app.IntegrationTest;
import org.liptsoft.app.domain.CategoryEntity;
import org.liptsoft.app.exception.NoExistCategoryNameException;
import org.liptsoft.app.exception.NoExistMccCategoryException;
import org.liptsoft.app.exception.NotFoundTransactionException;
import org.liptsoft.app.repository.CategoryRepository;
import org.liptsoft.app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TransactionServiceTest extends IntegrationTest {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @Rollback
    @Transactional
    public void addTransaction_shouldCorrectlyAddTransaction()
        throws NoExistMccCategoryException, NoExistCategoryNameException {
        final String categoryName = "Фастфуд";
        final AddTransactionRequest transactionRequest =
            new AddTransactionRequest().transaction(new Transaction().value(1700.0)
                    .month(MonthEnum.AUGUST))
                .mcc(List.of(4299, 5298));
        transactionService.addTransaction(categoryName, transactionRequest);
        assertThat(transactionRepository.findByMonthOrderByIdAsc(MonthEnum.AUGUST)
            .get(0)
            .getCategories()
            .stream()
            .map(CategoryEntity::getName)).containsAll(List.of("Фастфуд", "Развлечения", "Еда"));
    }

    @Test
    @Rollback
    @Transactional
    public void addTransaction_shouldThrowExceptionIfCategoryNameNoExist() {
        final String categoryName = "Путешествия";
        final AddTransactionRequest transactionRequest =
            new AddTransactionRequest().transaction(new Transaction().value(1700.0)
                    .month(MonthEnum.AUGUST))
                .mcc(List.of(5811, 5298));
        NoExistCategoryNameException exception = assertThrows(NoExistCategoryNameException.class, () -> {
            transactionService.addTransaction(categoryName, transactionRequest);
        });
        assertThat(exception.getMessage()).isEqualTo("Категория с именем \"Путешествия\" не найдена");
    }

    @Test
    @Rollback
    @Transactional
    public void addTransaction_shouldThrowExceptionIfMccNoExist() {
        final String categoryName = "Фастфуд";
        final AddTransactionRequest transactionRequest =
            new AddTransactionRequest().transaction(new Transaction().value(1700.0)
                    .month(MonthEnum.AUGUST))
                .mcc(List.of(5811, 8765));
        NoExistMccCategoryException exception = assertThrows(NoExistMccCategoryException.class, () -> {
            transactionService.addTransaction(categoryName, transactionRequest);
        });
        assertThat(exception.getMessage()).isEqualTo("Категория с mcc \"8765\" не найдена");
    }

    @Test
    @Rollback
    @Transactional
    public void deleteTransaction_shouldCorrectlyDeleteTransaction()
        throws NotFoundTransactionException, NoExistCategoryNameException {
        final String categoryName = "Рестораны";
        final Transaction transaction = new Transaction(500.0, MonthEnum.JANUARY);
        transactionService.deleteTransaction(categoryName, transaction);
        CategoryEntity category = categoryRepository.findByName(categoryName).get();
        assert category.getTransactions().stream().filter(categoryTransaction -> transaction.getValue()
                .equals(categoryTransaction.getValue()) && transaction.getMonth()
                .equals(categoryTransaction.getMonth()))
            .findAny()
            .isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    public void deleteTransaction_shouldThrowExceptionIfCategoryNameNoExist() {
        final String categoryName = "Путешествия";
        final Transaction transaction = new Transaction(500.0, MonthEnum.JANUARY);
        NoExistCategoryNameException exception = assertThrows(NoExistCategoryNameException.class, () -> {
            transactionService.deleteTransaction(categoryName, transaction);
        });
        assertThat(exception.getMessage()).isEqualTo("Категория с именем \"Путешествия\" не найдена");
    }

    @Test
    @Rollback
    @Transactional
    public void deleteTransaction_shouldThrowExceptionIfTransactionNotFound() {
        final String categoryName = "Рестораны";
        final Transaction transaction = new Transaction(500.0, MonthEnum.DECEMBER);
        NotFoundTransactionException exception = assertThrows(NotFoundTransactionException.class, () -> {
            transactionService.deleteTransaction(categoryName, transaction);
        });
    }

    @Test
    @Rollback
    @Transactional
    public void getTransactionByCategory_shouldCorrectlyReturnStats() throws NoExistCategoryNameException {
        final String categoryName = "Еда";
        List<CategoryTransaction> excepted = List.of(new CategoryTransaction(2000.0, MonthEnum.JANUARY),
            new CategoryTransaction(1403.0, MonthEnum.FEBRUARY),
            new CategoryTransaction(350.0, MonthEnum.MARCH)
        );
        List<CategoryTransaction> actual = transactionService.getTransactionByCategory(categoryName);
        assert actual.size() == 3;
        assertThat(actual).containsAll(excepted);
    }

    @Test
    @Rollback
    @Transactional
    public void getTransactionByCategory_shouldThrowExceptionIfCategoryNameNoExist() {
        final String categoryName = "Путешествия";
        NoExistCategoryNameException exception = assertThrows(NoExistCategoryNameException.class, () -> {
            transactionService.getTransactionByCategory(categoryName);
        });
        assertThat(exception.getMessage()).isEqualTo("Категория с именем \"Путешествия\" не найдена");
    }

    @Test
    @Rollback
    @Transactional
    public void getTransactionByCategory_shouldReturnEmptyListIfNoTransactions() throws NoExistCategoryNameException {
        transactionRepository.deleteAllInBatch();
        final String categoryName = "Еда";
        assert transactionService.getTransactionByCategory(categoryName).isEmpty();
    }

    @Test
    @Rollback
    @Transactional
    public void getTransactionByMonth_shouldCorrectlyReturnMonthTransactions() {
        List<TransactionWithPercent> excepted = List.of(new TransactionWithPercent("Рестораны", 600.0, 24),
            new TransactionWithPercent("Супермаркеты", 700.0, 28),
            new TransactionWithPercent("Еда", 2000.0, 80)
            ,
            new TransactionWithPercent("Фастфуд", 500.0, 20),
            new TransactionWithPercent("Развлечения", 1100.0, 44)
        );
        List<TransactionWithPercent> actual = transactionService.getTransactionByMonth(MonthEnum.JANUARY);
        assert actual.size() == 5;
        assertThat(actual).containsAll(excepted);
    }

    @Test
    @Rollback
    @Transactional
    public void getTransactionByMonth_shouldReturnListIfNoTransaction() {
        transactionRepository.deleteAllInBatch();
        assert transactionService.getTransactionByMonth(MonthEnum.JANUARY).isEmpty();
    }

}
