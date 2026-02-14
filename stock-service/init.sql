CREATE DATABASE stock_db;

\c stock_db;


CREATE TABLE categories (
    "id"            uuid NOT NULL,
    "category_key"   VARCHAR(255),
    "category_value" VARCHAR(255),
    "created_date"  TIMESTAMP,
    "modified_date" TIMESTAMP
);

CREATE TABLE products (
    "id"            uuid NOT NULL,
    "brand"        VARCHAR(255),
    "model"        VARCHAR(255),
    "category_id"  BIGINT NOT NULL,
    "unit"         INTEGER,
    "unit_price"   DOUBLE PRECISION,
    "is_active"    BOOLEAN NOT NULL DEFAULT FALSE,
    "created_date"  TIMESTAMP,
    "modified_date" TIMESTAMP
    CONSTRAINT fk_product_category
    FOREIGN KEY (category_id)
        REFERENCES categories (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT uq_product_category UNIQUE ("category_id")
);

-- İsteğe bağlı index (sorgu performansı için):
CREATE INDEX idx_products_is_active ON products ("is_active");
CREATE INDEX idx_products_brand_model ON products ("brand", "model");


CREATE TABLE "public"."outbox"
(
    "id"            uuid NOT NULL,
    "type"          character varying(255),
    "payload"       character varying(2000),
    CONSTRAINT "outbox_pkey" PRIMARY KEY ("id")
) WITH (oids = false);