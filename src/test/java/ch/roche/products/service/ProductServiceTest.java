package ch.roche.products.service;

import static ch.roche.products.model.ProductStatus.DELETED;
import static ch.roche.products.util.ProductUtil.buildProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.roche.products.converter.ProductConverter;
import ch.roche.products.dto.ProductCreateDto;
import ch.roche.products.dto.ProductDto;
import ch.roche.products.exception.ProductNotFoundException;
import ch.roche.products.model.Product;
import ch.roche.products.repo.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ProductConverter productConverter;

    @InjectMocks
    private ProductService productService;

    @Test
    public void shouldFindAll() {
        // given
        Product p1 = buildProduct("test", BigDecimal.TEN);
        Product p2 = buildProduct("other product", BigDecimal.ONE);

        List<Product> products = new ArrayList<>();
        products.add(p1);
        products.add(p2);

        // when
        when(productRepository.findAll()).thenReturn(products);

        // then
        final List<ProductDto> list = productService.findAll();

        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
        assertThat(list)
            .containsExactlyInAnyOrder(productConverter.toDto(p1), productConverter.toDto(p2));

        verify(productConverter, times(4)).toDto(any(Product.class));
    }

    @Test
    public void shouldDeleteById() {
        // given
        Product p1 = buildProduct("test", BigDecimal.TEN);

        // when
        when(productRepository.findById(5L)).thenReturn(Optional.of(p1));
        productService.deleteById(5L);

        // then
        assertThat(p1.getStatus()).isEqualTo(DELETED);
    }

    @Test
    public void shouldNotDeleteById() {
        // when
        when(productRepository.findById(5L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.deleteById(5L)).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void shouldUpdateProduct() {
        // given
        Product old = Product.builder()
            .id(5L)
            .name("macbook old")
            .price(BigDecimal.valueOf(1000.00))
            .build();


        ProductDto update = ProductDto.builder()
            .id(5L)
            .name("macbook new")
            .price(BigDecimal.valueOf(2000.00))
            .build();

        // when
        when(productRepository.findById(5L)).thenReturn(Optional.of(old));
        when(productRepository.save(any(Product.class))).thenAnswer(invocationOnMock -> {
            final Product product = invocationOnMock.getArgument(0);
            product.setId(5L);
            return product;
        });

        final ProductDto updated = productService.updateProduct(update);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(5L);
        assertThat(updated.getName()).isEqualTo("macbook new");
        assertThat(updated.getPrice()).isEqualTo(BigDecimal.valueOf(2000.00));
    }

    @Test
    public void shouldCreateProduct() {

        // given
        ProductCreateDto createDto = ProductCreateDto.builder()
            .name("macbook")
            .price(BigDecimal.valueOf(1234.88))
            .build();

        // when
        when(productRepository.save(any(Product.class))).thenAnswer(invocationOnMock -> {
            final Product product = invocationOnMock.getArgument(0);
            product.setId(5L);
            return product;
        });
        final ProductDto created = productService.createProduct(createDto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("macbook");
        assertThat(created.getPrice()).isEqualTo(BigDecimal.valueOf(1234.88));

        verify(productConverter).toDto(any(Product.class));
    }

    @Test
    public void shouldFindOne() {
        // given
        Product p1 = buildProduct("test", BigDecimal.TEN);

        // when
        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
        final ProductDto dto = productService.findOne(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("test");
        assertThat(dto.getPrice()).isEqualTo(BigDecimal.TEN);
    }
}