package org.liptsoft.app.repository;

import java.util.Optional;
import org.liptsoft.app.domain.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);

    boolean existsByName(String name);
}
