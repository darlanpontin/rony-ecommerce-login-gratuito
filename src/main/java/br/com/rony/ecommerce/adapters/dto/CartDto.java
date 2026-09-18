package br.com.rony.ecommerce.adapters.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDto {
    private String idCarrinho;
    private List<CartItemDto> itens;
    private BigDecimal valorTotalCarrinho;
    private BigDecimal valorTotalComDesconto;
    private Integer quantidadeTotalItens;

    // getters / setters
    public String getIdCarrinho() { return idCarrinho; }
    public void setIdCarrinho(String idCarrinho) { this.idCarrinho = idCarrinho; }
    public List<CartItemDto> getItens() { return itens; }
    public void setItens(List<CartItemDto> itens) { this.itens = itens; }
    public BigDecimal getValorTotalCarrinho() { return valorTotalCarrinho; }
    public void setValorTotalCarrinho(BigDecimal valorTotalCarrinho) { this.valorTotalCarrinho = valorTotalCarrinho; }
    public BigDecimal getValorTotalComDesconto() { return valorTotalComDesconto; }
    public void setValorTotalComDesconto(BigDecimal valorTotalComDesconto) { this.valorTotalComDesconto = valorTotalComDesconto; }
    public Integer getQuantidadeTotalItens() { return quantidadeTotalItens; }
    public void setQuantidadeTotalItens(Integer quantidadeTotalItens) { this.quantidadeTotalItens = quantidadeTotalItens; }
}