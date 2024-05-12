package org.liptsoft.app.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import model.MonthEnum;

@Getter
@Setter
@Entity(name = "transactions")
@Accessors(chain = true)
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double value;
    @Enumerated(EnumType.STRING)
    private MonthEnum month;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "category_transactions",
               joinColumns = @JoinColumn(name = "transaction_id"),
               inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories = new HashSet<>();

    public TransactionEntity() {

    }

    public TransactionEntity(Double value, MonthEnum month) {
        this.value = value;
        this.month = month;
    }

}
