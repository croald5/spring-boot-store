package com.codewithmosh.store.payments;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "Cart ID must not be null!")
    private UUID cartId;
}
