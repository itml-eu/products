package ch.roche.products.dto;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductCreateDto {

    @NotBlank
    @NotNull
    private String name;

    @Min(0)
    @NotNull
    private BigDecimal price;

}
