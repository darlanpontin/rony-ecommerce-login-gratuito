package br.com.rony.ecommerce.adapters.controllers;

import br.com.rony.ecommerce.adapters.dto.ApiResponse;
import br.com.rony.ecommerce.adapters.dto.CartDto;
import br.com.rony.ecommerce.adapters.dto.requests.AddCartItemRequest;
import br.com.rony.ecommerce.adapters.dto.requests.UpdateQuantityRequest;
import br.com.rony.ecommerce.domain.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinho")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) { this.cartService = cartService; }

    // Recupera carrinho do usuário atual
    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart() {
        String ownerId = currentOwnerId();
        CartDto dto = cartService.getCart(ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, null, dto));
    }

    // Adiciona item ao carrinho
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartDto>> addItem(@RequestBody AddCartItemRequest req) {
        String ownerId = currentOwnerId();
        CartDto dto = cartService.addItem(ownerId, req.getProdutoId(), req.getQuantidade());
        String produtoNome = dto.getItens().stream()
                .filter(i -> i.getProdutoId().equals(req.getProdutoId()))
                .findFirst()
                .map(i -> i.getNome())
                .orElse("");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Item '" + produtoNome + "' adicionado com sucesso no carrinho", dto));
    }

    // Atualizar quantidade (definir)
    @PatchMapping("/items/{produtoId}/quantidade")
    public ResponseEntity<ApiResponse<CartDto>> updateQuantity(@PathVariable Long produtoId, @RequestBody UpdateQuantityRequest req) {
        String ownerId = currentOwnerId();
        CartDto dto = cartService.updateQuantity(ownerId, produtoId, req.getQuantidade());
        String nome = dto.getItens().stream().filter(i -> i.getProdutoId().equals(produtoId)).map(i->i.getNome()).findFirst().orElse("");
        return ResponseEntity.ok(new ApiResponse<>(true, "Quantidade do item '" + nome + "' atualizada para " + req.getQuantidade(), dto));
    }

    // Incrementar quantidade
    @PatchMapping("/items/{produtoId}/increment")
    public ResponseEntity<ApiResponse<CartDto>> increment(@PathVariable Long produtoId, @RequestBody UpdateQuantityRequest req) {
        String ownerId = currentOwnerId();
        CartDto dto = cartService.incrementQuantity(ownerId, produtoId, req.getQuantidade());
        String nome = dto.getItens().stream().filter(i -> i.getProdutoId().equals(produtoId)).map(i->i.getNome()).findFirst().orElse("");
        return ResponseEntity.ok(new ApiResponse<>(true, "Quantidade do item '" + nome + "' incrementada em " + req.getQuantidade(), dto));
    }

    // Decrementar quantidade
    @PatchMapping("/items/{produtoId}/decrement")
    public ResponseEntity<ApiResponse<CartDto>> decrement(@PathVariable Long produtoId, @RequestBody UpdateQuantityRequest req) {
        String ownerId = currentOwnerId();
        CartDto dto = cartService.decrementQuantity(ownerId, produtoId, req.getQuantidade());
        String nome = produtoNomeSafe(dto, produtoId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Quantidade do item '" + nome + "' decrementada em " + req.getQuantidade(), dto));
    }

    // Remover item específico
    @DeleteMapping("/items/{produtoId}")
    public ResponseEntity<ApiResponse<CartDto>> removeItem(@PathVariable Long produtoId) {
        String ownerId = currentOwnerId();
        CartDto dto = cartService.removeItem(ownerId, produtoId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item removido do carrinho", dto));
    }

    // Limpar todo o carrinho
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<CartDto>> clear() {
        String ownerId = currentOwnerId();
        CartDto dto = cartService.clearCart(ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Todos os itens foram removidos do carrinho", dto));
    }

    // helper para extrair ownerId do contexto de segurança
    private String currentOwnerId() {
        // adapte aqui conforme seu JWT / authentication principal
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    private String produtoNomeSafe(CartDto dto, Long produtoId) {
        return dto.getItens().stream().filter(i->i.getProdutoId().equals(produtoId)).map(i->i.getNome()).findFirst().orElse("");
    }
}