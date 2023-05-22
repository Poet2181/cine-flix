create table cart_item
(
    id          bigserial not null,
    quantity    integer,
    customer_id bigint,
    order_id    bigint,
    product_id  bigint,
    primary key (id)
);

create table customer
(
    id    bigserial not null,
    email varchar(255),
    name  varchar(255),
    primary key (id)
);

create table orders
(
    id           bigserial not null,
    date_created date,
    status       varchar(255),
    customer_id  bigint,
    primary key (id)
);

create table product
(
    id    bigserial not null,
    name  varchar(255),
    price float(53) not null,
    primary key (id)
);

alter table if exists cart_item
    add constraint FKfy7fubprxqguyp4km04eogy66
    foreign key (customer_id)
    references customer;

alter table if exists cart_item
    add constraint FK3mu9lcrqocn2rdcm6xhbqrg3b
    foreign key (order_id)
    references orders;

alter table if exists cart_item
    add constraint FKjcyd5wv4igqnw413rgxbfu4nv
    foreign key (product_id)
    references product;

alter table if exists orders
    add constraint FK624gtjin3po807j3vix093tlf
    foreign key (customer_id)
    references customer;