package ch.roche.products.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductDto {

    @NotNull
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @Min(0)
    @NotNull
    private BigDecimal price;

    private ZonedDateTime dateCreated;

}
