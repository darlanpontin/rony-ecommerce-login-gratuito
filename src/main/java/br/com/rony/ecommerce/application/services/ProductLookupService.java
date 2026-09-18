package br.com.rony.ecommerce.application.services;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductLookupService {
    Optional<ProductInfo> findById(Long produtoId);

    class ProductInfo {
        private Long id;
        private String nome;
        private String foto;
        private BigDecimal precoUnitario;
        private Integer stock;
        private BigDecimal descontoPorItem; // pode ser zero

        // getters / setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getFoto() { return foto; }
        public void setFoto(String foto) { this.foto = foto; }
        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        public BigDecimal getDescontoPorItem() { return descontoPorItem; }
        public void setDescontoPorItem(BigDecimal descontoPorItem) { this.descontoPorItem = descontoPorItem; }
    }
}