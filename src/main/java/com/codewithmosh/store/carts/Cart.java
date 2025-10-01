package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import static java.util.Objects.*;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true,  fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice(){
        return items.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getItem(Long productId) {
        return this.getItems().stream().
                        filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
    }

    public CartItem addItem(Product product) {
        CartItem cartItem = getItem(product.getId());
        if (!isNull(cartItem)) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else{
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            this.getItems().add(cartItem);
        }
        return cartItem;
    }

    public void removeItem(Long productId) {
        CartItem cartItem = getItem(productId);
       if(!isNull(cartItem)){
           getItems().remove(cartItem);
       }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean areProductsValid(){
        return items.stream().allMatch(item -> item.getProduct() != null);
    }

    public void clear() {
        items.clear();
    }
}