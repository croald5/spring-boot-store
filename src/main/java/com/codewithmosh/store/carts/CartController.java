package com.codewithmosh.store.carts;

import com.codewithmosh.store.users.UserMapper;
import com.codewithmosh.store.auth.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriComponentsBuilder) {
        CartDto cartDto = cartService.createCart();
        return ResponseEntity.created(uriComponentsBuilder.path("/cart/{id}").buildAndExpand(cartDto.getId()).toUri()).body(cartDto);
    }

    @PostMapping({"/{cartId}/items"})
    @Operation(summary = "Add products to a cart.")
    public ResponseEntity<CartItemDto> addItemToCart(@Parameter(description = "Id of the cart") @PathVariable UUID cartId, @RequestBody @Valid AddProductToCartRequest request,
            UriComponentsBuilder uriComponentsBuilder) {
        CartItemDto cartItemDto = cartService.addItemToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping({"/{cartId}"})
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        CartDto cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping({"/{cartId}/items/{productId}"})
    public ResponseEntity<?> updateCartItem(@RequestBody @Valid UpdateCartItemRequest request, @PathVariable UUID cartId, @PathVariable Long productId) {
        CartItemDto cartItemDto = cartService.updateCartItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping({"/{cartId}/items/{productId}"})
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    @Transactional
    public ResponseEntity<?> deleteAllCartItems(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
