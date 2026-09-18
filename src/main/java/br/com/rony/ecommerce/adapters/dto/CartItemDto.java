package br.com.rony.ecommerce.adapters.dto;

import java.math.BigDecimal;

public class CartItemDto {
    private Long produtoId;
    private String nome;
    private String foto;
    private BigDecimal valorUnitario;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private BigDecimal desconto;
    private BigDecimal valorComDesconto;

    // getters / setters
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }
    public BigDecimal getValorComDesconto() { return valorComDesconto; }
    public void setValorComDesconto(BigDecimal valorComDesconto) { this.valorComDesconto = valorComDesconto; }
}