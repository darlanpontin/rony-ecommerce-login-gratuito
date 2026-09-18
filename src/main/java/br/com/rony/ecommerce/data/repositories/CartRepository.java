package br.com.rony.ecommerce.data.repositories;

import br.com.rony.ecommerce.domain.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByOwnerId(String ownerId);
}