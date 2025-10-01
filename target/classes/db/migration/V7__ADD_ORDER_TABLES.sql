create table orders
(
    id          BIGINT auto_increment
        primary key,
    customer_id BIGINT                               not null,
    status      VARCHAR(20)                          not null,
    created_at  DATETIME default CURRENT_TIMESTAMP() not null,
    total_price DECIMAL(10, 2)                       not null
);
create table order_items
(
    id          BIGINT auto_increment
        primary key,
    order_id    BIGINT                   not null,
    product_id  BIGINT                   null,
    unit_price  DECIMAL(10, 2) default 0 not null,
    quantity    INTEGER        default 0 not null,
    total_price DECIMAL(10, 2) default 0 not null,
    constraint order_items_orders_id_fk
        foreign key (order_id) references orders (id),
    constraint order_items_products_id_fk
        foreign key (product_id) references products (id)
);
