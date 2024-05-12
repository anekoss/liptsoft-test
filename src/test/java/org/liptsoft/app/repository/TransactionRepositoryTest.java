package org.liptsoft.app.repository;

import java.util.List;
import model.MonthEnum;
import org.junit.jupiter.api.Test;
import org.liptsoft.app.IntegrationTest;
import org.liptsoft.app.domain.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TransactionRepositoryTest extends IntegrationTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void findByMonth_shouldCorrectlyReturnTransactionList() {
        List<TransactionEntity> transactions = transactionRepository.findByMonthOrderByIdAsc(MonthEnum.MARCH);
        assert !transactions.isEmpty();
        assertThat(transactions.get(0).getValue()).isEqualTo(150);
        assertThat(transactions.get(transactions.size() - 1).getValue()).isEqualTo(450);
    }

    @Test
    public void findByMonth_shouldReturnEmptyListIfNoTransactions() {
        List<TransactionEntity> transactions = transactionRepository.findByMonthOrderByIdAsc(MonthEnum.JULY);
        assert transactions.isEmpty();
    }

}
