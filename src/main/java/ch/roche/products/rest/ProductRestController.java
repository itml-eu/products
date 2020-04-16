package ch.roche.products.rest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import ch.roche.products.dto.ProductCreateDto;
import ch.roche.products.dto.ProductDto;
import ch.roche.products.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/products")
@RequiredArgsConstructor
@Api(value = "/products", description = "Product API", consumes = "application/json")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    @ApiOperation(value = "Finds all products",
        response = ProductDto.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Product list retrieved successfully")
    })
    public List<ProductDto> getList() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds single product",
        response = ProductDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Product retrieved successfully"),
        @ApiResponse(code = 404, message = "Product not found")
    })
    public ProductDto getProduct(@PathVariable(name = "id") final Long id) {
        return productService.findOne(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @GetMapping("/{id}")
    @ApiOperation(value = "Deletes product by Id")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfull deletion with no content returned"),
        @ApiResponse(code = 404, message = "Product not found")
    })
    public void deleteProduct(@PathVariable(name = "id") final Long id) {
        productService.deleteById(id);
    }

    @PutMapping
    @ApiOperation(value = "Updates single product",
        response = ProductDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated product"),
        @ApiResponse(code = 404, message = "Product not found")
    })
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Creates product",
        response = ProductDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created product")
    })
    public ProductDto createProduct(@Valid @RequestBody ProductCreateDto productDto) {
        return productService.createProduct(productDto);
    }
}
