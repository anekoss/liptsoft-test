package org.liptsoft.app.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "mccs")
public class MccEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(1000)
    @Max(9999)
    private Integer mcc;

    @ManyToOne
    @JoinTable(name = "category_mccs", joinColumns = @JoinColumn(name = "mcc_id"),
               inverseJoinColumns = @JoinColumn(name = "category_id"))
    private CategoryEntity category;

    public MccEntity() {

    }

    public MccEntity(Integer mcc, CategoryEntity category) {
        this.mcc = mcc;
        this.category = category;
    }

}
