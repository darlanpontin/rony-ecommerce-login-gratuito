package br.com.rony.ecommerce.domain.services;

import br.com.rony.ecommerce.adapters.dto.CartDto;
import br.com.rony.ecommerce.domain.entities.CartItem;

public interface CartService {
    CartDto getCart(String ownerId);
    CartDto addItem(String ownerId, Long produtoId, Integer quantidade);
    CartDto removeItem(String ownerId, Long produtoId);
    CartDto updateQuantity(String ownerId, Long produtoId, Integer quantidade);
    CartDto incrementQuantity(String ownerId, Long produtoId, Integer quantidade);
    CartDto decrementQuantity(String ownerId, Long produtoId, Integer quantidade);
    CartDto clearCart(String ownerId);
}