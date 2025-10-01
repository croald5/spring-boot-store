package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.ProductDto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDto {
    private ProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
