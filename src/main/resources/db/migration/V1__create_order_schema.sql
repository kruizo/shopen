-- Orders Table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    buyer_id BIGINT NOT NULL,
    order_status VARCHAR(50) NOT NULL CHECK (order_status IN ('PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED')),
    payment_status VARCHAR(50) NOT NULL CHECK (payment_status IN ('PENDING', 'COMPLETED', 'FAILED')),
    total_amount DECIMAL(12,2) NOT NULL CHECK (total_amount >= 0),
    placed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    shipping_address TEXT NOT NULL,
    billing_address TEXT NOT NULL,

    CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_orders_buyer_id ON orders(buyer_id);

-- Order Lines Table
CREATE TABLE order_lines (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(12,2) NOT NULL CHECK (unit_price >= 0),
    total_price DECIMAL(12,2) NOT NULL CHECK (total_price >= 0),

    CONSTRAINT fk_orderlines_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_orderlines_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

CREATE INDEX idx_orderlines_order_id ON order_lines(order_id);
CREATE INDEX idx_orderlines_product_id ON order_lines(product_id);