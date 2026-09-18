package br.com.rony.ecommerce.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @Column(name = "id", length = 36)
    private String id; // ex: "usr_123" ou UUID associado ao usuário/sessão

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "owner_id", nullable = false)
    private String ownerId; // usuário ou sessão identifier

    public Cart() {}

    public Cart(String ownerId) {
        this.ownerId = ownerId;
        this.id = "cart_" + UUID.randomUUID().toString();
    }

    // getters / setters
    public String getId() { return id; }
    public List<CartItem> getItems() { return items; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public void addItem(CartItem item) {
        item.setCart(this);
        this.items.add(item);
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null);
    }

    public void clear() {
        this.items.clear();
    }

    // derived helpers
    public int totalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal totalValue() {
        return items.stream()
                .map(CartItem::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalValueWithDiscount() {
        return items.stream()
                .map(CartItem::getValorComDesconto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}