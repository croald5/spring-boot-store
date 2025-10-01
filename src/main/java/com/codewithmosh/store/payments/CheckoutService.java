package com.codewithmosh.store.payments;

import com.codewithmosh.store.carts.Cart;
import com.codewithmosh.store.orders.Order;
import com.codewithmosh.store.users.User;
import com.codewithmosh.store.carts.CartEmptyException;
import com.codewithmosh.store.carts.CartNotFoundException;
import com.codewithmosh.store.products.ProductNotFoundException;
import com.codewithmosh.store.carts.CartRepository;
import com.codewithmosh.store.orders.OrderRepository;
import com.codewithmosh.store.auth.AuthService;
import com.codewithmosh.store.carts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final PaymentGetaway paymentGetaway;

    @Transactional
    public CheckoutResponse processCheckout(UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (isNull(cart)) {
            throw new CartNotFoundException();
        }
        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }
        if (!cart.areProductsValid()) {
            throw new ProductNotFoundException();
        }
        User user = authService.getCurrentUser();
        Order order = Order.fromCart(cart, user);
        orderRepository.save(order);
        try {
            CheckoutSession sessionResponse = paymentGetaway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckoutResponse(order.getId(), sessionResponse.getSessionUrl());
        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGetaway.parseWebhookRequest(request).ifPresent(paymentResult -> {
            Order order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
        });
    }
}
