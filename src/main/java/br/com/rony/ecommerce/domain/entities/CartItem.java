package br.com.rony.ecommerce.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "cart_items", uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "produto_id"}))
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // referencia ao produto
    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(name = "produto_nome", nullable = false)
    private String nome;

    @Column(name = "produto_foto")
    private String foto;

    @Column(name = "valor_unitario", precision = 19, scale = 4, nullable = false)
    private BigDecimal valorUnitario;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "desconto", precision = 19, scale = 4, nullable = false)
    private BigDecimal desconto = BigDecimal.ZERO; // valor absoluto de desconto por item (não percentual)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartItem() {}

    public CartItem(Long produtoId,
                    String nome,
                    String foto,
                    BigDecimal valorUnitario,
                    Integer quantidade,
                    BigDecimal desconto) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.foto = foto;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
        this.desconto = desconto == null ? BigDecimal.ZERO : desconto;
    }

    // getters / setters
    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public String getNome() { return nome; }
    public String getFoto() { return foto; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getDesconto() { return desconto; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    // derived fields
    public BigDecimal getValorTotal() {
        return valorUnitario.multiply(new BigDecimal(quantidade)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValorComDesconto() {
        BigDecimal total = getValorTotal().subtract(desconto);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}