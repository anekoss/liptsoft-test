package org.liptsoft.app.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
@Entity(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(name = "subcategories",
               joinColumns = @JoinColumn(name = "category_id"),
               inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
    private Set<CategoryEntity> subCategories = new HashSet<>();
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MccEntity> mccs = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "category_transactions",
               joinColumns = @JoinColumn(name = "category_id"),
               inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    private List<TransactionEntity> transactions = new ArrayList<>();

    public CategoryEntity() {

    }

    public void addSubcategory(CategoryEntity subcategory) {
        this.subCategories.add(subcategory);
    }

}
