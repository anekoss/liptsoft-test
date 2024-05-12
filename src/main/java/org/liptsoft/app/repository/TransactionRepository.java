package org.liptsoft.app.repository;

import java.util.List;
import model.MonthEnum;
import org.liptsoft.app.domain.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByMonthOrderByIdAsc(MonthEnum month);
}
