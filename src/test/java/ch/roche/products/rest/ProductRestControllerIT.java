package ch.roche.products.rest;

import static ch.roche.products.util.ProductUtil.buildProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.roche.products.dto.ProductCreateDto;
import ch.roche.products.dto.ProductDto;
import ch.roche.products.model.Product;
import ch.roche.products.repo.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class ProductRestControllerIT extends BaseMockMvc {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void beforeEach() {
        productRepository.deleteAll();
    }

    @Test
    public void shouldGetSingleProduct() throws Exception {
        final Product macbook = productRepository.save(buildProduct("macbook", BigDecimal.TEN));

        this.mockMvc.perform(get("/products/{id}", macbook.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("macbook")))
            .andExpect(jsonPath("$.id", is(macbook.getId().intValue())))
            .andExpect(jsonPath("$.price", is(BigDecimal.TEN.doubleValue())))
            .andExpect(jsonPath("$.dateCreated", is(macbook.getDateCreated().toString())));
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        final ProductCreateDto createDto = ProductCreateDto.builder()
            .name("test product")
            .price(BigDecimal.valueOf(1000.40))
            .build();

        this.mockMvc.perform(post("/products")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(createDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("test product")))
            .andExpect(jsonPath("$.id", is(notNullValue())))
            .andExpect(jsonPath("$.price", is(1000.40)));
    }


    @Test
    public void shouldGetListOfNotDeletedProducts() throws Exception {
        final Product chair = productRepository.save(buildProduct("chair", BigDecimal.ONE));
        final Product book = productRepository.save(buildProduct("book", BigDecimal.TEN).deleted());

        productRepository.save(chair.deleted());
        productRepository.save(book.deleted());
        productRepository.save(buildProduct("macbook", BigDecimal.TEN));
        productRepository.save(buildProduct("bottle", BigDecimal.ONE));


        final String response = this.mockMvc.perform(get("/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        final List<ProductDto> list = objectMapper
            .readValue(response, new TypeReference<List<ProductDto>>() {
            });

        assertThat(list).hasSize(2);

        final ProductDto macbook = list.stream()
            .filter(productDto -> productDto.getName().equals("macbook")).findFirst().get();

        assertThat(macbook).isNotNull();
        assertThat(macbook.getName()).isEqualTo("macbook");
        assertThat(macbook.getPrice()).isEqualTo(new BigDecimal("10.00"));

        final ProductDto bottle = list.stream()
            .filter(productDto -> productDto.getName().equals("bottle")).findFirst().get();

        assertThat(bottle).isNotNull();
        assertThat(bottle.getName()).isEqualTo("bottle");
        assertThat(bottle.getPrice()).isEqualTo(new BigDecimal("1.00"));
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        final Product book = productRepository.save(buildProduct("book", BigDecimal.TEN).deleted());

        this.mockMvc.perform(get("/products/{id}", book.getId()))
            .andDo(print())
            .andExpect(status().isOk());

        productRepository.save(book.deleted());

        this.mockMvc.perform(get("/products/{id}", book.getId()))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
}
