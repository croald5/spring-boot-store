package com.codewithmosh.store.orders;

import com.codewithmosh.store.auth.AuthService;
import com.codewithmosh.store.users.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuthService authService;

    public List<OrderDto> getOrders() {
        User user = authService.getCurrentUser();
        List<Order> orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    public OrderDto getOrder(Long orderId) {
        User customer = authService.getCurrentUser();
        Order order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.isPlacedBy(customer)) {
            throw new AccessDeniedException("You are not authorized to view this order");
        }
        return orderMapper.toDto(order);
    }
}
