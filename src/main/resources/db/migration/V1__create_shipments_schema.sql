CREATE TABLE shipments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    shipped_at TIMESTAMPTZ,
    delivery_status VARCHAR(50) NOT NULL CHECK (delivery_status IN ('PENDING', 'IN_TRANSIT', 'DELIVERED', 'RETURNED')),
    carrier VARCHAR(50),
    tracking_number VARCHAR(100),

    CONSTRAINT fk_shipments_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_shipments_order_id ON shipments(order_id);