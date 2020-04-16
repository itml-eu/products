package ch.roche.products.model;

import static ch.roche.products.model.ProductStatus.DELETED;
import static ch.roche.products.model.ProductStatus.PERSISTED;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "product" )
@Builder
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Where(clause = "status <> 'DELETED'")
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;

    private BigDecimal price;

    private LocalDateTime dateCreated;

    @Enumerated(STRING)
    private ProductStatus status;

    @PrePersist
    public void onPersist() {
        this.dateCreated = LocalDateTime.now();
        this.status = PERSISTED;
    }

    public Product deleted() {
        this.status = DELETED;
        return this;
    }
}
