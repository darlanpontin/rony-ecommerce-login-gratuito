package br.com.rony.ecommerce.data.repositories;

import br.com.rony.ecommerce.domain.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProdutoId(String cartId, Long produtoId);
    void deleteByCartIdAndProdutoId(String cartId, Long produtoId);
}