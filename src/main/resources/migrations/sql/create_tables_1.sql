--liquibase formatted sql

--changeset anekoss:1
-- comment: create categories table
create table if not exists categories
(
    id         bigint generated always as identity primary key,
    name   text                     not null unique
);
-- rollback drop table if exists categories;

--changeset anekoss:2
-- comment: create mccs table
create table if not exists mccs
(   id         bigint generated always as identity primary key,
    mcc char(4) not null unique
)
-- rollback drop table if exists mccs;

--changeset anekoss:3
-- comment: create month_enum
create type month_enum as enum (
    'january', 'february', 'march', 'april',
    'may', 'june', 'july', 'august',
    'september', 'october', 'november', 'december'
);
-- rollback drop type if exists month_enum;

--changeset anekoss:4
-- comment: create transactions table
create table if not exists transactions
(
    id         bigint generated always as identity primary key,
    value   bigint                     not null,
    month month_enum not null
);
-- rollback drop table if exists transactions;

--changeset anekoss:5
-- comment: create category_transactions table
create table if not exists category_transactions
(
    id         bigint generated always as identity primary key,
    category_id bigint not null references categories (id) on delete cascade,
    transaction_id bigint not null references transactions (id) on delete cascade,
);
-- rollback drop table if exists category_transactions;


--changeset anekoss:6
-- comment: create subcategories table
create table if not exists subcategories
(
    id         bigint generated always as identity primary key,
    category_id bigint not null references categories (id) on delete cascade,
    subcategory_id bigint not null references categories (id) on delete cascade,
    constraint different_categories check (category_id != subcategory_id)
    );
-- rollback drop table if exists subcategories;

--changeset anekoss:7
-- comment: create category_mccs table
create table if not exists category_mccs
(
    id         bigint generated always as identity primary key,
    category_id bigint not null references categories (id) on delete cascade,
    mcc_id bigint not null unique references mccs (id) on delete cascade
    );
-- rollback drop table if exists category_mccs




