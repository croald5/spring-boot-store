package com.codewithmosh.store.orders;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private PaymentStatus status;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private List<OrderItemDto> items = new ArrayList<>();
}
