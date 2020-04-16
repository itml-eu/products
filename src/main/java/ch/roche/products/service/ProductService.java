package ch.roche.products.service;

import static java.util.stream.Collectors.toList;

import ch.roche.products.converter.ProductConverter;
import ch.roche.products.dto.ProductCreateDto;
import ch.roche.products.dto.ProductDto;
import ch.roche.products.exception.ProductNotFoundException;
import ch.roche.products.model.Product;
import ch.roche.products.repo.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    public List<ProductDto> findAll() {
        return productRepository.findAll()
            .stream()
            .map(productConverter::toDto)
            .collect(toList());
    }

    public void deleteById(Long id) {
        final Optional<Product> byId = productRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ProductNotFoundException(id);
        } else {
            final Product product = byId.get();
            productRepository.save(product.deleted());
        }
    }

    public ProductDto updateProduct(ProductDto productDto) {
        final Optional<Product> byId = productRepository.findById(productDto.getId());
        if (!byId.isPresent()) {
            throw new ProductNotFoundException(productDto.getId());
        } else {
            final Product product = byId.get();
            final Product merged = productConverter.merge(product, productDto);
            final Product saved = productRepository.save(merged);

            return productConverter.toDto(saved);
        }
    }

    public ProductDto createProduct(ProductCreateDto productDto) {
        final Product created = productConverter.toEntity(productDto);
        final Product saved = productRepository.save(created);
        return productConverter.toDto(saved);
    }

    public ProductDto findOne(Long id) {
        final Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        return productConverter.toDto(product);
    }
}
