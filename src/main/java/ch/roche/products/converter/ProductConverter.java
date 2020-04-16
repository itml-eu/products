package ch.roche.products.converter;

import static ch.roche.products.model.ProductStatus.PERSISTED;

import ch.roche.products.dto.ProductCreateDto;
import ch.roche.products.dto.ProductDto;
import ch.roche.products.model.Product;
import ch.roche.products.model.ProductStatus;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .build();
    }

    public Product toEntity(ProductDto dto) {
        return Product.builder()
            .id(dto.getId())
            .name(dto.getName())
            .price(dto.getPrice())
            .build();
    }

    public Product toEntity(ProductCreateDto dto) {
        return Product.builder()
            .name(dto.getName())
            .price(dto.getPrice())
            .status(PERSISTED)
            .build();
    }

    public Product merge(Product product, ProductDto productDto) {
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        return product;
    }
}
