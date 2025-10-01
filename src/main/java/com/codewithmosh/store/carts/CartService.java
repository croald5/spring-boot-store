package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.Product;
import com.codewithmosh.store.products.ProductNotFoundException;
import com.codewithmosh.store.orders.OrderMapper;
import com.codewithmosh.store.orders.OrderRepository;
import com.codewithmosh.store.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import static java.util.Objects.*;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private CartMapper cartMapper;
    private OrderMapper orderMapper;

    public CartDto createCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addItemToCart(UUID cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (isNull(cart)) {
            throw new CartNotFoundException();
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (isNull(product)) {
            throw new ProductNotFoundException();
        }
        CartItem cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (isNull(cart)) {
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (isNull(cart)) {
            throw new CartNotFoundException();
        }
        CartItem cartItem = cart.getItem(productId);
        if (isNull(cartItem)) {
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void deleteCartItem(UUID cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (isNull(cart)) {
            throw new CartNotFoundException();
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (isNull(cart)) {
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);
    }
}
