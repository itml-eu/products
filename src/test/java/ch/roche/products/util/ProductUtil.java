package ch.roche.products.util;

import static ch.roche.products.model.ProductStatus.PERSISTED;
import static java.time.LocalDateTime.now;

import ch.roche.products.model.Product;
import java.math.BigDecimal;

public class ProductUtil {
    public static Product buildProduct(String name, BigDecimal price) {
        return Product.builder()
            .name(name)
            .price(price)
            .dateCreated(now())
            .status(PERSISTED)
            .build();
    }
}
