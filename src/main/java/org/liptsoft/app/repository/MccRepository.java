package org.liptsoft.app.repository;

import java.util.Optional;
import org.liptsoft.app.domain.MccEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MccRepository extends JpaRepository<MccEntity, Long> {

    Optional<MccEntity> findByMcc(Integer mcc);

    boolean existsByMcc(Integer mcc);
}
