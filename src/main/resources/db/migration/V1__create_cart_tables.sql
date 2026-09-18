-- Flyway migration example (ajuste esquema se necessário)
CREATE TABLE IF NOT EXISTS carts (
  id VARCHAR(36) PRIMARY KEY,
  owner_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  cart_id VARCHAR(36),
  produto_id BIGINT NOT NULL,
  produto_nome VARCHAR(512) NOT NULL,
  produto_foto VARCHAR(1024),
  valor_unitario DECIMAL(19,4) NOT NULL,
  quantidade INT NOT NULL,
  desconto DECIMAL(19,4) NOT NULL DEFAULT 0,
  CONSTRAINT fk_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX ux_cart_prod ON cart_items(cart_id, produto_id);