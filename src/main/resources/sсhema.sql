create table if not exists users
(
    user_id    bigint generated always as identity primary key,
    email varchar(320) UNIQUE,
    name  varchar(300)
);

create table if not exists items
(
    item_id           bigint generated always as identity primary key,
    name         varchar(300),
    description  varchar(300),
    is_available boolean,
    owner_id     bigint,
    request_id   bigint,
    constraint fk_items_to_users foreign key (owner_id) references users (user_id)
);
create table if not exists booking
(
    booking_id         bigint generated always as identity primary key,
    start_time TIMESTAMP,
    end_time   TIMESTAMP,
    item_id    BIGINT NOT NULL,
    booker_id  BIGINT,
    status     VARCHAR(20),
    constraint fk_booking_to_items foreign key (item_id) references items (item_id),
    constraint fk_booking_to_users foreign key (booker_id) references users (user_id)
);
create table if not exists comments
(
    comment_id        bigint generated always as identity primary key,
    text      varchar(320),
    item_id   bigint,
    author_id bigint,
    created   TIMESTAMP,
    constraint fk_comments_to_items foreign key (item_id) references items (item_id),
    constraint fk_comments_to_users foreign key (author_id) references users (user_id)
);