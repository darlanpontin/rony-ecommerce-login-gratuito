package br.com.rony.ecommerce.application.services;

import br.com.rony.ecommerce.adapters.dto.CartDto;
import br.com.rony.ecommerce.adapters.dto.CartItemDto;
import br.com.rony.ecommerce.data.repositories.CartItemRepository;
import br.com.rony.ecommerce.data.repositories.CartRepository;
import br.com.rony.ecommerce.domain.entities.Cart;
import br.com.rony.ecommerce.domain.entities.CartItem;
import br.com.rony.ecommerce.domain.exceptions.BadRequestException;
import br.com.rony.ecommerce.domain.exceptions.InsufficientStockException;
import br.com.rony.ecommerce.domain.exceptions.ResourceNotFoundException;
import br.com.rony.ecommerce.domain.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductLookupService productLookupService; // adapter to product catalog + stock

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductLookupService productLookupService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productLookupService = productLookupService;
    }

    private Cart getOrCreateCart(String ownerId) {
        return cartRepository.findByOwnerId(ownerId)
                .orElseGet(() -> {
                    Cart c = new Cart(ownerId);
                    return cartRepository.save(c);
                });
    }

    @Override
    @Transactional
    public CartDto getCart(String ownerId) {
        Cart cart = getOrCreateCart(ownerId);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addItem(String ownerId, Long produtoId, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new BadRequestException("Quantidade inválida. Deve ser maior que zero.");
        }

        // valida produto existe e estoque
        ProductLookupService.ProductInfo product = productLookupService.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (product.getStock() < quantidade) {
            throw new InsufficientStockException("Estoque insuficiente para o produto '" + product.getNome() + "'.");
        }

        Cart cart = getOrCreateCart(ownerId);

        // se já existe item, incrementa a quantidade
        CartItem existing = cartItemRepository.findByCartIdAndProdutoId(cart.getId(), produtoId).orElse(null);
        if (existing != null) {
            int novo = existing.getQuantidade() + quantidade;
            if (product.getStock() < novo) {
                throw new InsufficientStockException("Estoque insuficiente para atualizar a quantidade do produto '" + product.getNome() + "'.");
            }
            existing.setQuantidade(novo);
            cartItemRepository.save(existing);
        } else {
            // criar novo CartItem com valores do produto (valor unitario, nome, foto)
            CartItem ci = new CartItem(
                    produtoId,
                    product.getNome(),
                    product.getFoto(),
                    product.getPrecoUnitario(),
                    quantidade,
                    product.getDescontoPorItem() != null ? product.getDescontoPorItem() : BigDecimal.ZERO
            );
            cart.addItem(ci);
            cartRepository.save(cart); // cascata salvará o item
        }

        return toDto(cartRepository.findById(cart.getId()).orElse(cart));
    }

    @Override
    @Transactional
    public CartDto removeItem(String ownerId, Long produtoId) {
        Cart cart = cartRepository.findByOwnerId(ownerId).orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado"));
        CartItem existing = cartItemRepository.findByCartIdAndProdutoId(cart.getId(), produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));
        cart.removeItem(existing);
        cartRepository.save(cart);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateQuantity(String ownerId, Long produtoId, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new BadRequestException("Quantidade inválida. Deve ser maior que zero.");
        }
        Cart cart = cartRepository.findByOwnerId(ownerId).orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado"));
        CartItem existing = cartItemRepository.findByCartIdAndProdutoId(cart.getId(), produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));

        ProductLookupService.ProductInfo product = productLookupService.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (product.getStock() < quantidade) {
            throw new InsufficientStockException("Estoque insuficiente para o produto '" + product.getNome() + "'.");
        }

        existing.setQuantidade(quantidade);
        cartItemRepository.save(existing);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartDto incrementQuantity(String ownerId, Long produtoId, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new BadRequestException("Quantidade inválida. Deve ser maior que zero.");
        }
        Cart cart = cartRepository.findByOwnerId(ownerId).orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado"));
        CartItem existing = cartItemRepository.findByCartIdAndProdutoId(cart.getId(), produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));

        ProductLookupService.ProductInfo product = productLookupService.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        int novo = existing.getQuantidade() + quantidade;
        if (product.getStock() < novo) {
            throw new InsufficientStockException("Estoque insuficiente para o produto '" + product.getNome() + "'.");
        }
        existing.setQuantidade(novo);
        cartItemRepository.save(existing);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartDto decrementQuantity(String ownerId, Long produtoId, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new BadRequestException("Quantidade inválida. Deve ser maior que zero.");
        }
        Cart cart = cartRepository.findByOwnerId(ownerId).orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado"));
        CartItem existing = cartItemRepository.findByCartIdAndProdutoId(cart.getId(), produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho"));

        int novo = existing.getQuantidade() - quantidade;
        if (novo <= 0) {
            // remove o item
            cart.removeItem(existing);
        } else {
            existing.setQuantidade(novo);
        }
        cartRepository.save(cart);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartDto clearCart(String ownerId) {
        Cart cart = getOrCreateCart(ownerId);
        cart.clear();
        cartRepository.save(cart);
        return toDto(cart);
    }

    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setIdCarrinho(cart.getId());
        dto.setItens(cart.getItems().stream().map(item -> {
            CartItemDto it = new CartItemDto();
            it.setProdutoId(item.getProdutoId());
            it.setNome(item.getNome());
            it.setFoto(item.getFoto());
            it.setValorUnitario(item.getValorUnitario());
            it.setQuantidade(item.getQuantidade());
            it.setValorTotal(item.getValorTotal());
            it.setDesconto(item.getDesconto());
            it.setValorComDesconto(item.getValorComDesconto());
            return it;
        }).collect(Collectors.toList()));
        dto.setValorTotalCarrinho(cart.totalValue());
        dto.setValorTotalComDesconto(cart.totalValueWithDiscount());
        dto.setQuantidadeTotalItens(cart.totalQuantity());
        return dto;
    }
}