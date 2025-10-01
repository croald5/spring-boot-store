package com.codewithmosh.store.payments;

import com.codewithmosh.store.common.ErrorDto;
import com.codewithmosh.store.carts.CartEmptyException;
import com.codewithmosh.store.carts.CartNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(@RequestBody @Valid CheckoutRequest request) {
        return checkoutService.processCheckout(request.getCartId());
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler(PaymentException.class)
    private ResponseEntity<ErrorDto> handlePaymentException(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorDto("Error creating a checkout session!"));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    private ResponseEntity<ErrorDto> handleCartException(Exception e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }
}
