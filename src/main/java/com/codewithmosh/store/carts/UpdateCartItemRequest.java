package com.codewithmosh.store.carts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity must not be null!")
    @Min(value = 1, message = "Value must be greater than 0")
    @Max(value = 100, message = "Value must be less than or equal to 100")
    private Integer quantity;
}
