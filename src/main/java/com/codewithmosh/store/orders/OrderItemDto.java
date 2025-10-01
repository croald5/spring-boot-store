package com.codewithmosh.store.orders;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private ProductDto product;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}
